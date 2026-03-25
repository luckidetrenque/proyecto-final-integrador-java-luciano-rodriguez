package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.ClaseDto;
import com.escueladeequitacion.hrs.dto.ClaseDto.AlActualizar;
import com.escueladeequitacion.hrs.dto.ClaseResponseDto;
import com.escueladeequitacion.hrs.enums.Especialidad;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Clase;
import com.escueladeequitacion.hrs.security.ClaseSecurityService;
import com.escueladeequitacion.hrs.service.AlumnoService;
import com.escueladeequitacion.hrs.service.CaballoService;
import com.escueladeequitacion.hrs.service.ClaseService;
import com.escueladeequitacion.hrs.service.InstructorService;
import com.escueladeequitacion.hrs.utility.Mensaje;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clases")
public class ClaseController {

    @Autowired
    ClaseService claseService;

    @Autowired
    AlumnoService alumnoService;

    @Autowired
    InstructorService instructorService;

    @Autowired
    CaballoService caballoService;

    @Autowired
    ClaseSecurityService claseSecurityService;

    // ============================================================
    // ENDPOINTS BÁSICOS
    // ============================================================

    /**
     * GET /api/v1/clases
     * - ADMIN: ve todas las clases
     * - INSTRUCTOR: ve solo sus clases (filtrado por instructorId automáticamente)
     * - ALUMNO: no tiene acceso (usar /alumno/{id}/detalles)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping()
    public ResponseEntity<Page<ClaseResponseDto>> listarClases(
            @PageableDefault(size = 20, sort = "dia", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "estado", required = false) Estado estado,
            @RequestParam(name = "especialidad", required = false) Especialidad especialidad,
            Authentication authentication) {

        // Si es INSTRUCTOR, filtrar automáticamente por su ID
        Long instructorId = null;
        if (authentication != null && authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            instructorId = claseSecurityService.getInstructorId(authentication);
        }

        Page<ClaseResponseDto> clases = claseService.listarClasesPaginado(
                pageable, estado, especialidad, instructorId);
        return ResponseEntity.ok(clases);
    }

    /**
     * GET /api/v1/clases/{id}
     * ADMIN e INSTRUCTOR pueden ver cualquier clase.
     * ALUMNO no tiene acceso directo.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClasePorId(@PathVariable("id") Long id) {
        Clase clase = claseService.buscarClasePorId(id)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException(
                        "Clase", "ID", id));
        return ResponseEntity.status(HttpStatus.OK).body(clase);
    }

    /**
     * GET /api/v1/clases/dia/{dia}
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/dia/{dia}")
    public ResponseEntity<?> obtenerClasePorDia(@PathVariable("dia") LocalDate dia) {
        List<Clase> clases = claseService.buscarClasePorDia(dia);

        if (clases.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("La clase con día " + dia + " no existe en la base de datos"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(clases.get(0));
    }

    /**
     * DELETE /api/v1/clases/{id}
     * Solo ADMIN puede eliminar clases físicamente.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarClase(@PathVariable("id") Long id) {
        claseService.eliminarClase(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Clase con ID " + id + " eliminada correctamente"));
    }

    /**
     * POST /api/v1/clases
     * ADMIN e INSTRUCTOR pueden crear clases.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping()
    public ResponseEntity<?> crearClase(@Valid @RequestBody ClaseDto claseDto) {
        Clase clase = claseService.crearClase(claseDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Clase creada correctamente con ID: " + clase.getId()));
    }

    /**
     * PUT /api/v1/clases/{id}
     * ADMIN puede editar cualquier clase.
     * INSTRUCTOR puede editar solo sus propias clases.
     */
    @PreAuthorize("hasRole('ADMIN') or @claseSecurityService.puedeModificar(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarClase(
            @PathVariable("id") Long id,
            @Validated(AlActualizar.class) @RequestBody ClaseDto claseDto) {
        claseService.actualizarClase(id, claseDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Clase con ID " + id + " actualizada correctamente"));
    }

    /**
     * PATCH /api/v1/clases/{id}/estado
     * ADMIN puede cambiar el estado de cualquier clase.
     * INSTRUCTOR puede cambiar el estado solo de sus propias clases.
     */
    @PreAuthorize("hasRole('ADMIN') or @claseSecurityService.puedeModificar(#id, authentication)")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ClaseResponseDto> cambiarEstadoClase(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {

        String estadoStr = body.get("estado");
        String observaciones = body.getOrDefault("observaciones", null);

        if (estadoStr == null || estadoStr.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Estado nuevoEstado;
        try {
            nuevoEstado = Estado.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        ClaseResponseDto resultado = claseService.cambiarEstado(id, nuevoEstado, observaciones);
        return ResponseEntity.ok(resultado);
    }

    // ============================================================
    // ENDPOINTS CON DETALLES (relaciones cargadas)
    // ============================================================

    /**
     * GET /api/v1/clases/detalles
     * Solo ADMIN.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detalles")
    public ResponseEntity<List<ClaseResponseDto>> listarClasesConDetalles() {
        List<ClaseResponseDto> clases = claseService.listarClasesConDetalles();
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/{id}/detalles
     * ADMIN e INSTRUCTOR (solo si es su clase).
     */
    @PreAuthorize("hasRole('ADMIN') or @claseSecurityService.puedeModificar(#id, authentication)")
    @GetMapping("/{id}/detalles")
    public ResponseEntity<ClaseResponseDto> obtenerClaseConDetalles(@PathVariable("id") Long id) {
        ClaseResponseDto clase = claseService.buscarClasePorIdConDetalles(id)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException(
                        "Clase", "ID", id));
        return ResponseEntity.status(HttpStatus.OK).body(clase);
    }

    /**
     * GET /api/v1/clases/dia/{dia}/detalles
     * ADMIN e INSTRUCTOR.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/dia/{dia}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorDiaConDetalles(
            @PathVariable("dia") LocalDate dia) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorDiaConDetalles(dia);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/instructor/{instructorId}/detalles
     * ADMIN puede ver cualquier instructor.
     * INSTRUCTOR solo puede ver sus propias clases.
     */
    @PreAuthorize("hasRole('ADMIN') or @claseSecurityService.esElMismoInstructor(#instructorId, authentication)")
    @GetMapping("/instructor/{instructorId}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorInstructorConDetalles(
            @PathVariable("instructorId") Long instructorId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorInstructorConDetalles(instructorId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/detalles
     * ADMIN, INSTRUCTOR y el propio ALUMNO pueden ver este endpoint.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorAlumnoConDetalles(
            @PathVariable("alumnoId") Long alumnoId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorAlumnoConDetalles(alumnoId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/caballo/{caballoId}/detalles
     * ADMIN e INSTRUCTOR.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/caballo/{caballoId}/detalles")
    public ResponseEntity<?> obtenerClasesPorCaballoConDetalles(
            @PathVariable("caballoId") Long caballoId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorCaballoConDetalles(caballoId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/estado/{estado}/detalles
     * Solo ADMIN.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/estado/{estado}/detalles")
    public ResponseEntity<?> obtenerClasesPorEstadoConDetalles(@PathVariable("estado") Estado estado) {
        if (!claseService.existeClasePorEstado(estado)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("No existen clases " + estado));
        }
        List<ClaseResponseDto> clases = claseService.buscarClasePorEstadoConDetalles(estado);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    // ============================================================
    // ENDPOINTS DE CONTEO
    // ============================================================

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/completadas/count
     * ADMIN, INSTRUCTOR y el propio ALUMNO.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/completadas/count")
    public ResponseEntity<Map<String, Object>> contarClasesCompletadasPorAlumno(
            @PathVariable("alumnoId") Long alumnoId) {
        long count = claseService.contarClasesCompletadasPorAlumno(alumnoId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("alumnoId", alumnoId);
        response.put("clasesCompletadas", count);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/clases/instructor/{instructorId}/completadas/count
     * ADMIN o el propio INSTRUCTOR.
     */
    @PreAuthorize("hasRole('ADMIN') or @claseSecurityService.esElMismoInstructor(#instructorId, authentication)")
    @GetMapping("/instructor/{instructorId}/completadas/count")
    public ResponseEntity<Map<String, Object>> contarClasesCompletadasPorInstructor(
            @PathVariable("instructorId") Long instructorId) {
        long count = claseService.contarClasesCompletadasPorInstructor(instructorId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("instructorId", instructorId);
        response.put("clasesCompletadas", count);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // ENDPOINTS PARA CLASES DE PRUEBA
    // ============================================================

    /**
     * POST /api/v1/clases/prueba
     * ADMIN e INSTRUCTOR pueden crear clases de prueba.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/prueba")
    public ResponseEntity<Clase> crearClaseDePrueba(@RequestBody @Valid ClaseDto claseDto) {
        claseDto.setEsPrueba(true);
        Clase clase = claseService.crearClase(claseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clase);
    }

    /**
     * GET /api/v1/clases/prueba
     * ADMIN e INSTRUCTOR.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping("/prueba")
    public ResponseEntity<List<ClaseResponseDto>> listarClasesDePrueba() {
        List<ClaseResponseDto> clases = claseService.listarTodasLasClasesDePrueba();
        return ResponseEntity.ok(clases);
    }

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/prueba
     * ADMIN, INSTRUCTOR y el propio ALUMNO.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/prueba")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesDePruebaPorAlumno(
            @PathVariable("alumnoId") Long alumnoId) {
        List<ClaseResponseDto> clases = claseService.listarClasesDePruebaPorAlumno(alumnoId);
        return ResponseEntity.ok(clases);
    }

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/tiene-prueba
     * ADMIN, INSTRUCTOR y el propio ALUMNO.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/tiene-prueba")
    public ResponseEntity<Map<String, Object>> verificarClaseDePrueba(
            @PathVariable("alumnoId") Long alumnoId) {
        Map<String, Object> info = claseService.obtenerInfoClasesDePrueba(alumnoId);
        return ResponseEntity.ok(info);
    }
}