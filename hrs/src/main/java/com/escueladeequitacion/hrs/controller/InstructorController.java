package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.InstructorDto;
import com.escueladeequitacion.hrs.model.Instructor;
import com.escueladeequitacion.hrs.service.InstructorService;
import com.escueladeequitacion.hrs.utility.Mensaje;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/v1/instructores")

// Controlador REST para gestionar los instructores
public class InstructorController {

    @Autowired
    InstructorService instructorService;

    @Autowired
    com.escueladeequitacion.hrs.security.ClaseSecurityService claseSecurityService;

    // Endpoint GET para listar todos los instructores
    /**
     * GET /api/v1/instructores
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN','INSTRUCTOR','ALUMNO')")
    @GetMapping()
    public ResponseEntity<Page<Instructor>> listarInstructores(
            @PageableDefault(size = 20, sort = "apellido") Pageable pageable,
            @RequestParam(name = "activo", required = false) Boolean activo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "apellido", required = false) String apellido) {

        Page<Instructor> instructores = instructorService.listarInstructoresPaginado(
                pageable, activo, nombre, apellido);
        return ResponseEntity.ok(instructores);
    }

    /**
     * GET /api/v1/instructores/me
     * Retorna el perfil del instructor logueado (si lo es).
     */
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/me")
    public ResponseEntity<?> obtenerMiPerfilInstructor(
            org.springframework.security.core.Authentication authentication) {
        Long instructorId = claseSecurityService.getInstructorId(authentication);
        if (instructorId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("No se encontró el perfil de instructor asociado a tu cuenta."));
        }
        Instructor instructor = instructorService.buscarInstructorPorId(instructorId)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Instructor",
                        "ID", instructorId));
        return ResponseEntity.ok(instructor);
    }

    // Endpoint GET para buscar un instructor por ID
    /**
     * GET /api/v1/instructores/{id}
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN') or @claseSecurityService.esElMismoInstructor(#id, authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInstructorPorId(@PathVariable("id") Long id) {

        Instructor instructor = instructorService.buscarInstructorPorId(id)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Instructor",
                        "ID", id));
        return ResponseEntity.status(HttpStatus.OK).body(instructor);
    }

    // Endpoint GET para buscar un instructor por DNI
    /**
     * GET /api/v1/instructores/dni/{dni}
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN','INSTRUCTOR')")
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> obtenerInstructorPorDni(@PathVariable("dni") String dni) {

        Instructor instructor = instructorService.buscarInstructorPorDni(dni)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Instructor",
                        "DNI", dni));
        return ResponseEntity.status(HttpStatus.OK).body(instructor);
    }

    // Endpoint POST para crear un nuevo instructor
    /**
     * POST /api/v1/instructores
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PostMapping()
    public ResponseEntity<?> crearInstructor(@Valid @RequestBody InstructorDto instructorDto) {

        Instructor instructor = instructorService.crearInstructorDesdeDto(instructorDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Instructor " + instructor.getNombreCompleto() + " creado correctamente"));
    }

    // Endpoint PUT para actualizar un instructor por ID
    /**
     * PUT /api/v1/instructores/{id}
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN') or @claseSecurityService.esElMismoInstructor(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInstructor(@PathVariable("id") Long id,
            @Valid @RequestBody InstructorDto instructorDto,
            org.springframework.security.core.Authentication authentication) {

        if (authentication != null
                && authentication.getAuthorities().stream().noneMatch(a ->
                        a.getAuthority().equals("ROLE_COORDINADOR")
                        || a.getAuthority().equals("ROLE_SUPERADMIN"))) {
            Instructor original = instructorService.buscarInstructorPorId(id).orElseThrow();
            instructorDto.setColor(original.getColor());
        }

        instructorService.actualizarInstructorDesdeDto(id, instructorDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Instructor con ID " + id + " actualizado correctamente"));

    }

    // Endpoint DELETE para eliminar un instructor por ID (Eliminación Física)
    /**
     * DELETE /api/v1/instructores/{id}
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInstructor(@PathVariable("id") Long id) {

        instructorService.eliminarInstructor(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Instructor con ID " + id + " eliminado correctamente"));
    }

    // Endpoint DELETE para eliminar un instructor por ID (Eliminación Lógica)
    /**
     * DELETE /api/v1/instructores/{id}/inactivar
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @DeleteMapping("/{id}/inactivar")
    public ResponseEntity<?> eliminarInstructorTemporalmente(@PathVariable("id") Long id) {
        instructorService.eliminarInstructorTemporalmente(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Instructor con ID " + id + " inactivado correctamente"));
    }

}