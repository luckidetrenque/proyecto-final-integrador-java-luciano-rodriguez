package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.InstructorDto;
import com.escueladeequitacion.hrs.model.Instructor;
import com.escueladeequitacion.hrs.service.InstructorService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import com.escueladeequitacion.hrs.utility.Constantes;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constantes.API_VERSION)
@CrossOrigin(origins = { Constantes.ORIGINS })

// Controlador REST para gestionar los instructores
public class InstructorController {

    @Autowired
    InstructorService instructorService;

    // Endpoint GET para listar todos los instructores
    /**
     * GET /api/v1/instructores
     */
    @GetMapping(Constantes.RESOURCE_INSTRUCTORES)
    public ResponseEntity<List<Instructor>> listarInstructores() {
        List<Instructor> instructores = instructorService.listarInstructores();
        return ResponseEntity.status(HttpStatus.OK).body(instructores);
    }

    // Endpoint GET para buscar un instructor por ID
    /**
     * GET /api/v1/instructores/{id}
     */
    @GetMapping(Constantes.RESOURCE_INSTRUCTORES + "/{id}")
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
    @GetMapping(Constantes.RESOURCE_INSTRUCTORES + "/dni/{dni}")
    public ResponseEntity<?> obtenerInstructorPorDni(@PathVariable("dni") Integer dni) {

        Instructor instructor = instructorService.buscarInstructorPorDni(dni)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Instructor",
                        "DNI", dni));
        return ResponseEntity.status(HttpStatus.OK).body(instructor);
    }

    // Endpoint POST para crear un nuevo instructor
    /**
     * POST /api/v1/instructores
     */
    @PostMapping(Constantes.RESOURCE_INSTRUCTORES)
    public ResponseEntity<?> crearInstructor(@Valid @RequestBody InstructorDto instructorDto) {

        Instructor instructor = instructorService.crearInstructorDesdeDto(instructorDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Instructor " + instructor.getNombreCompleto() + " creado correctamente"));
    }

    // Endpoint PUT para actualizar un instructor por ID
    /**
     * PUT /api/v1/instructores/{id}
     */
    @PutMapping(Constantes.RESOURCE_INSTRUCTORES + "/{id}")
    public ResponseEntity<?> actualizarInstructor(@PathVariable("id") Long id,
            @Valid @RequestBody InstructorDto instructorDto) {

        instructorService.actualizarInstructorDesdeDto(id, instructorDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Instructor con ID " + id + " actualizado correctamente"));

    }

    // Endpoint DELETE para eliminar un instructor por ID (Eliminación Física)
    /**
     * DELETE /api/v1/instructores/{id}
     */
    @DeleteMapping(Constantes.RESOURCE_INSTRUCTORES + "/{id}")
    public ResponseEntity<?> eliminarInstructor(@PathVariable Long id) {

        instructorService.eliminarInstructor(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Instructor con ID " + id + " eliminado correctamente"));
    }

    // Endpoint DELETE para eliminar un instructor por ID (Eliminación Lógica)
    /**
     * DELETE /api/v1/instructores/{id}/inactivar
     */
    @DeleteMapping(Constantes.RESOURCE_INSTRUCTORES + "/{id}/inactivar")
    public ResponseEntity<?> eliminarInstructorTemporalmente(@PathVariable Long id) {
        instructorService.eliminarInstructorTemporalmente(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Instructor con ID " + id + " inactivado correctamente"));
    }

    // Endpoint GET para buscar por diferentes filtros
    /**
     * GET /api/v1/instructores/buscar
     */
    @GetMapping(Constantes.RESOURCE_INSTRUCTORES + "/buscar")
    public ResponseEntity<?> buscarInstructor(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) LocalDate fechaNacimiento) {

        List<Instructor> instructores = instructorService.buscarInstructoresConFiltros(
                nombre, apellido, activo, fechaNacimiento);

        if (!instructores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(instructores);
        }

        instructores = instructorService.listarInstructores();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje",
                "No existen instructores con los filtros de búsqueda ingresados, se retorna el listado completo.");
        respuesta.put("instructores", instructores);

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }
}
