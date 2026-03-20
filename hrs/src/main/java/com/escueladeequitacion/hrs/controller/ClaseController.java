package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.ClaseDto;
import com.escueladeequitacion.hrs.dto.ClaseDto.AlActualizar;
import com.escueladeequitacion.hrs.dto.ClaseResponseDto;
import com.escueladeequitacion.hrs.enums.Especialidad;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Clase;

import com.escueladeequitacion.hrs.service.ClaseService;
import com.escueladeequitacion.hrs.service.AlumnoService;
import com.escueladeequitacion.hrs.service.InstructorService;
import com.escueladeequitacion.hrs.service.CaballoService;
import com.escueladeequitacion.hrs.utility.Mensaje;

import jakarta.validation.Valid;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clases")

// Controlador REST para gestionar las clases
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
    com.escueladeequitacion.hrs.security.ClaseSecurityService claseSecurityService;

    // ============================================================
    // ENDPOINTS BÁSICOS (sin detalles)
    // ============================================================

    // Endpoint GET para listar todos las clases
    /**
     * GET /api/v1/clases
     * Lista todas las clases (sin detalles de relaciones).
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping()
    public ResponseEntity<Page<ClaseResponseDto>> listarClases(
            @PageableDefault(size = 20, sort = "dia", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "estado", required = false) Estado estado,
            @RequestParam(name = "especialidad", required = false) Especialidad especialidad,
            org.springframework.security.core.Authentication authentication) {

        Long instructorId = null;
        if (authentication != null && authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            instructorId = claseSecurityService.getInstructorId(authentication);
        }

        Page<ClaseResponseDto> clases = claseService.listarClasesPaginado(pageable, estado, especialidad, instructorId);
        return ResponseEntity.ok(clases);
    }

    // Endpoint GET para buscar una clase por ID
    /**
     * GET /api/v1/clases/{id}
     * Obtiene una clase por ID.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClasePorId(@PathVariable("id") Long id) {

        Clase clase = claseService.buscarClasePorId(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(clase);
    }

    // Endpoint GET para buscar una clase por fecha
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/dia/{dia}")
    public ResponseEntity<?> obtenerClasePorDia(@PathVariable("dia") LocalDate dia) {
        List<Clase> clases = claseService.buscarClasePorDia(dia);

        if (clases.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("La clase con día " + dia + " no existe en la base de datos"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(clases.get(0));
    }

    // Endpoint DELETE para eliminar un clase por ID
    /**
     * DELETE /api/v1/clases/{id}
     * Elimina una clase (eliminación física).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarClase(@PathVariable("id") Long id) {
        // El Service valida que existe antes de eliminar
        claseService.eliminarClase(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Clase con ID " + id + " eliminada correctamente"));
    }

    // Endpoint POST para crear una nueva clase
    /**
     * POST /api/v1/clases
     * Crea una nueva clase.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping()
    public ResponseEntity<?> crearClase(@Valid @RequestBody ClaseDto claseDto, org.springframework.security.core.Authentication authentication) {

        if (authentication != null && authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            Long myId = claseSecurityService.getInstructorId(authentication);
            if (myId != null) {
                claseDto.setInstructorId(myId);
            }
        }

        Clase clase = claseService.crearClase(claseDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Clase creada correctamente con ID: " + clase.getId()));
    }

    // Endpoint PUT para actualizar un clase por ID
    /**
     * PUT /api/v1/clases/{id}
     * Actualiza una clase existente.
     */
    @PreAuthorize("hasRole('ADMIN') or @claseSecurityService.puedeModificar(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarClase(@PathVariable("id") Long id,
            @Validated(AlActualizar.class) @RequestBody ClaseDto claseDto,
            org.springframework.security.core.Authentication authentication) {
            
        if (authentication != null && authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            Long myId = claseSecurityService.getInstructorId(authentication);
            if (myId != null) {
                claseDto.setInstructorId(myId);
            }
        }

        // El Service maneja todas las validaciones
        claseService.actualizarClase(id, claseDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Clase con ID " + id + " actualizada correctamente"));
    }

    /**
     * PATCH /api/v1/clases/{id}/estado
     * Cambia solo el estado de una clase (sin tocar otros campos ni validaciones).
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
     * Lista todas las clases con información de instructor, alumno y caballo.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/detalles")
    public ResponseEntity<List<ClaseResponseDto>> listarClasesConDetalles() {
        List<ClaseResponseDto> clases = claseService.listarClasesConDetalles();
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/{id}/detalles
     * Obtiene una clase específica con todos sus detalles.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/{id}/detalles")
    public ResponseEntity<ClaseResponseDto> obtenerClaseConDetalles(@PathVariable("id") Long id) {
        ClaseResponseDto clase = claseService.buscarClasePorIdConDetalles(id)
                .orElseThrow(
                        () -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Clase", "ID", id));

        return ResponseEntity.status(HttpStatus.OK).body(clase);
    }

    /**
     * GET /api/v1/clases/dia/{dia}/detalles
     * Busca clases por fecha con detalles.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/dia/{dia}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorDiaConDetalles(@PathVariable("dia") LocalDate dia) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorDiaConDetalles(dia);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/instructor/{instructorId}/detalles
     * Obtiene todas las clases de un instructor específico.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/instructor/{instructorId}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorInstructorConDetalles(
            @PathVariable("instructorId") Long instructorId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorInstructorConDetalles(instructorId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/detalles
     * Obtiene todas las clases de un alumno específico.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorAlumnoConDetalles(
            @PathVariable("alumnoId") Long alumnoId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorAlumnoConDetalles(alumnoId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/detalles
     * Obtiene todas las clases de un alumno específico.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/caballo/{caballoId}/detalles")
    public ResponseEntity<?> obtenerClasesPorCaballoConDetalles(@PathVariable("caballoId") Long caballoId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorCaballoConDetalles(caballoId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
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
     * Cuenta las clases completadas de un alumno.
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
     * Cuenta las clases completadas de un instructor.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
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
     * Crea una clase de prueba.
     * 
     * POST /api/v1/clases/prueba
     * 
     * @param claseDto - Debe incluir esPrueba=true
     * @return Clase creada
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/prueba")
    public ResponseEntity<Clase> crearClaseDePrueba(@RequestBody @Valid ClaseDto claseDto) {
        // Forzar que sea clase de prueba
        claseDto.setEsPrueba(true);

        Clase clase = claseService.crearClase(claseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clase);
    }

    /**
     * Lista todas las clases de prueba del sistema.
     * 
     * GET /api/v1/clases/prueba
     * 
     * @return Lista de clases marcadas como prueba
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/prueba")
    public ResponseEntity<List<ClaseResponseDto>> listarClasesDePrueba() {
        List<ClaseResponseDto> clases = claseService.listarTodasLasClasesDePrueba();
        return ResponseEntity.ok(clases);
    }

    /**
     * Obtiene las clases de prueba de un alumno específico.
     * 
     * GET /api/v1/clases/alumno/{alumnoId}/prueba
     * 
     * @param alumnoId - ID del alumno
     * @return Lista de clases de prueba del alumno
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/prueba")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesDePruebaPorAlumno(
            @PathVariable("alumnoId") Long alumnoId) {

        List<ClaseResponseDto> clases = claseService.listarClasesDePruebaPorAlumno(alumnoId);
        return ResponseEntity.ok(clases);
    }

    /**
     * Verifica si un alumno ya tomó clase de prueba.
     * 
     * GET /api/v1/clases/alumno/{alumnoId}/tiene-prueba
     * 
     * @param alumnoId - ID del alumno
     * @return {alumnoId: N, tienePrueba: true/false, cantidadClasesPrueba: N}
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/tiene-prueba")
    public ResponseEntity<Map<String, Object>> verificarClaseDePrueba(@PathVariable("alumnoId") Long alumnoId) {
        Map<String, Object> info = claseService.obtenerInfoClasesDePrueba(alumnoId);
        return ResponseEntity.ok(info);
    }

}