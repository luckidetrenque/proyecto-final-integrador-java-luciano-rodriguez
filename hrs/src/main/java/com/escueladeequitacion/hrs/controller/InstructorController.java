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
import java.util.ArrayList;
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
    @GetMapping(Constantes.RESOURCE_INSTRUCTORES)
    public ResponseEntity<List<Instructor>> listarInstructores() {
        List<Instructor> instructores = instructorService.listarInstructores();
        return ResponseEntity.status(HttpStatus.OK).body(instructores);
    }

    // Endpoint GET para buscar un instructor por ID
    @GetMapping(Constantes.RESOURCE_INSTRUCTORES + "/{id}")
    public ResponseEntity<?> obtenerInstructorPorId(@PathVariable("id") Long id) {

        Instructor instructor = instructorService.buscarInstructorPorId(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(instructor);
    }

    // Endpoint GET para buscar un instructor por DNI
    @GetMapping(Constantes.RESOURCE_INSTRUCTORES + "/dni/{dni}")
    public ResponseEntity<?> obtenerInstructorPorDni(@PathVariable("dni") Integer dni) {

        Instructor instructor = instructorService.buscarInstructorPorDni(dni).get();
        return ResponseEntity.status(HttpStatus.OK).body(instructor);
    }

    // Endpoint POST para crear un nuevo instructor
    @PostMapping(Constantes.RESOURCE_INSTRUCTORES)
    public ResponseEntity<?> crearInstructor(@Valid @RequestBody InstructorDto instructorDto) {

        Instructor instructor = new Instructor(instructorDto.getDni(), instructorDto.getNombre(),
                instructorDto.getApellido(),
                instructorDto.getFechaNacimiento(), instructorDto.getTelefono(), instructorDto.getEmail(),
                instructorDto.isActivo());
        instructorService.guardarInstructor(instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje(
                "Instructor " + instructorDto.getNombre() + " " + instructorDto.getApellido()
                        + " creado correctamente"));
    }

    // Endpoint PUT para actualizar un instructor por ID
    @PutMapping(Constantes.RESOURCE_INSTRUCTORES + "/{id}")
    public ResponseEntity<?> actualizarInstructor(@PathVariable("id") Long id,
            @Valid @RequestBody InstructorDto instructorDto) {

        Instructor instructor = instructorService.buscarInstructorPorId(id).get();
        instructor.setDni(instructorDto.getDni());
        instructor.setNombre(instructorDto.getNombre());
        instructor.setApellido(instructorDto.getApellido());
        instructor.setFechaNacimiento(instructorDto.getFechaNacimiento());
        instructor.setTelefono(instructorDto.getTelefono());
        instructor.setEmail(instructorDto.getEmail());
        instructor.setActivo(instructorDto.isActivo());
        instructorService.guardarInstructor(instructor);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Instructor con ID " + id + " actualizado correctamente"));

    }

    // Endpoint DELETE para eliminar un instructor por ID (Eliminación Física)
    @DeleteMapping(Constantes.RESOURCE_INSTRUCTORES + "/{id}")
    public ResponseEntity<?> eliminarInstructor(@PathVariable Long id) {

        instructorService.eliminarInstructor(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Instructor con ID " + id + " eliminado correctamente (forma física)"));

    }

    // Endpoint DELETE para eliminar un instructor por ID (Eliminación Lógica)
@DeleteMapping(Constantes.RESOURCE_INSTRUCTORES + "/{id}/inactivar")
public ResponseEntity<?> eliminarInstructorTemporalmente(@PathVariable Long id) {
    // El Service maneja todas las validaciones
    instructorService.eliminarInstructorTemporalmente(id);
    
    return ResponseEntity.status(HttpStatus.OK)
            .body(new Mensaje("Instructor con ID " + id + " eliminado correctamente (forma lógica)"));
}

// Endpoint GET para buscar por diferentes filtros
    @GetMapping(Constantes.RESOURCE_INSTRUCTORES + "/buscar")
    public ResponseEntity<?> buscarInstructor(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) LocalDate fechaNacimiento) {

        List<Instructor> instructores = new ArrayList<>();

        if (nombre != null && apellido != null) {
            instructores = instructorService.buscarInstructorPorNombreYApellido(nombre, apellido);

        }

        if (nombre != null && apellido == null) {
            instructores = instructorService.buscarInstructorPorNombre(nombre);

        }

        if (nombre == null && apellido != null) {
            instructores = instructorService.buscarInstructorPorApellido(apellido);

        }

        if (activo != null && activo) {
            instructores = instructorService.buscarInstructorsPorEstado(true);

        }

        if (activo != null && !activo) {
            instructores = instructorService.buscarInstructorsPorEstado(false);

        }

        if (fechaNacimiento != null) {
            instructores = instructorService.buscarPorFechaNacimiento(fechaNacimiento);

        }

        if (!instructores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(instructores);

        }

        instructores = instructorService.listarInstructores();

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje",
                "No existen instructores con los filtros de búsqueda ingresados, se retorna el listado completo.");
        respuesta.put("instructores", instructores);

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body(new Mensaje("No existen alumnos con los filtros de búsqueda
        // ingresados"));
    }
}
