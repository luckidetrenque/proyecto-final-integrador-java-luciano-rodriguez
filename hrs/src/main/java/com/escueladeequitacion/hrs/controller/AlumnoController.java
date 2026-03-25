package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.AlumnoDto;
import com.escueladeequitacion.hrs.dto.AlumnoListadoDto;
import com.escueladeequitacion.hrs.dto.ConversionAPlanRequest;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.service.AlumnoService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import jakarta.validation.Valid;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {

    @Autowired
    AlumnoService alumnoService;

    @Autowired
    com.escueladeequitacion.hrs.security.ClaseSecurityService claseSecurityService;

    /**
     * GET /api/v1/alumnos
     * - ADMIN: ve todos los alumnos con filtros completos.
     * - INSTRUCTOR: ve solo los alumnos de sus clases.
     *
     * BUG FIX: si el usuario es INSTRUCTOR pero no tiene perfil vinculado,
     * se retorna 403 en lugar de devolver todos los alumnos sin filtro.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping()
    public ResponseEntity<Page<AlumnoListadoDto>> listarAlumnos(
            @PageableDefault(size = 20, sort = "apellido") Pageable pageable,
            @RequestParam(name = "activo", required = false) Boolean activo,
            @RequestParam(name = "propietario", required = false) Boolean propietario,
            @RequestParam(name = "cantidadClases", required = false) Integer cantidadClases,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "apellido", required = false) String apellido,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Long instructorId = claseSecurityService.getInstructorId(authentication);

            // BUG FIX: antes si instructorId era null retornaba todos los alumnos
            if (instructorId == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Tu cuenta de instructor no está vinculada a un perfil. Contactá al administrador.");
            }

            Page<AlumnoListadoDto> alumnos = alumnoService
                    .listarAlumnosPorInstructorPaginado(instructorId, pageable);
            return ResponseEntity.ok(alumnos);
        }

        Page<AlumnoListadoDto> alumnos = alumnoService.listarAlumnosPaginado(
                pageable, activo, propietario, cantidadClases, nombre, apellido);
        return ResponseEntity.ok(alumnos);
    }

    /**
     * GET /api/v1/alumnos/{id}
     * - ADMIN e INSTRUCTOR: ven cualquier alumno.
     * - ALUMNO: solo su propio perfil.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#id, authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAlumnoPorId(@PathVariable("id") Long id) {
        Optional<AlumnoListadoDto> alumno = alumnoService.buscarAlumnoPorIdResumido(id);
        if (alumno.isPresent()) {
            return ResponseEntity.ok(alumno.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el alumno con ID " + id);
        }
    }

    /**
     * GET /api/v1/alumnos/dni/{dni}
     * - ADMIN e INSTRUCTOR: pueden buscar cualquier alumno por DNI.
     * - ALUMNO: solo si el DNI es el suyo.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumnoPorDni(#dni, authentication)")
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> obtenerAlumnoPorDni(@PathVariable("dni") String dni) {
        Alumno alumno = alumnoService.buscarAlumnoPorDni(dni)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException(
                        "Alumno", "DNI", dni));
        return ResponseEntity.status(HttpStatus.OK).body(alumno);
    }

    /**
     * POST /api/v1/alumnos
     * Solo ADMIN puede crear alumnos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<?> crearAlumno(@Valid @RequestBody AlumnoDto alumnoDto) {
        Alumno alumno = alumnoService.crearAlumnoDesdeDto(alumnoDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Alumno " + alumno.getNombreCompleto() + " creado correctamente"));
    }

    /**
     * PUT /api/v1/alumnos/{id}
     * Solo ADMIN puede actualizar alumnos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAlumno(
            @PathVariable("id") Long id,
            @Valid @RequestBody AlumnoDto alumnoDto) {
        alumnoService.actualizarAlumnoDesdeDto(id, alumnoDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Alumno con ID " + id + " actualizado correctamente"));
    }

    /**
     * DELETE /api/v1/alumnos/{id}
     * Solo ADMIN — eliminación física.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable("id") Long id) {
        alumnoService.eliminarAlumno(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Alumno con ID " + id + " eliminado correctamente"));
    }

    /**
     * DELETE /api/v1/alumnos/{id}/inactivar
     * Solo ADMIN — eliminación lógica.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/inactivar")
    public ResponseEntity<?> eliminarAlumnoTemporalmente(@PathVariable("id") Long id) {
        alumnoService.eliminarAlumnoTemporalmente(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Alumno con ID " + id + " inactivado correctamente"));
    }

    /**
     * GET /api/v1/alumnos/{id}/clases/completadas/count
     * ADMIN, INSTRUCTOR y el propio ALUMNO.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#id, authentication)")
    @GetMapping("/{id}/clases/completadas/count")
    public ResponseEntity<?> contarClasesCompletadas(@PathVariable("id") Long id) {
        long count = alumnoService.contarClasesCompletadas(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", id);
        response.put("clasesCompletadas", count);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * PUT /api/v1/alumnos/{id}/convertir-a-plan
     * Solo ADMIN.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/convertir-a-plan")
    public ResponseEntity<?> convertirAlumnoAPlan(
            @PathVariable("id") Long id,
            @RequestBody @Valid ConversionAPlanRequest request) {

        alumnoService.convertirAlumnoAPlan(id, request.getCantidadClases());

        return ResponseEntity.ok(Map.of(
                "mensaje", "Alumno convertido a plan regular exitosamente",
                "alumnoId", id,
                "cantidadClases", request.getCantidadClases()));
    }

    /**
     * GET /api/v1/alumnos/prueba
     * ADMIN e INSTRUCTOR.
     */
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/prueba")
    public ResponseEntity<List<Alumno>> listarAlumnosDePrueba() {
        List<Alumno> alumnosPrueba = alumnoService.buscarAlumnoPorEstado(false);
        return ResponseEntity.ok(alumnosPrueba);
    }

    /**
     * GET /api/v1/alumnos/me
     * Endpoint especial para que el ALUMNO logueado obtenga su propio perfil
     * sin necesitar conocer su ID de antemano.
     */
    @PreAuthorize("hasRole('ALUMNO')")
    @GetMapping("/me")
    public ResponseEntity<?> obtenerMiPerfilAlumno(Authentication authentication) {
        Long alumnoId = claseSecurityService.getAlumnoId(authentication);
        if (alumnoId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje(
                            "No se encontró el perfil de alumno asociado a tu cuenta. Contactá al administrador."));
        }
        Optional<AlumnoListadoDto> alumno = alumnoService.buscarAlumnoPorIdResumido(alumnoId);
        return alumno.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Mensaje("Alumno no encontrado")));
    }
}