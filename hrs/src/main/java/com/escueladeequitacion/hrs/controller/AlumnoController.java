package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.AlumnoDto;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.service.AlumnoService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import com.escueladeequitacion.hrs.utility.Constantes;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constantes.API_VERSION)
@CrossOrigin(origins = { Constantes.ORIGINS })

// Controlador REST para gestionar los alumnos
public class AlumnoController {

    @Autowired
    AlumnoService alumnoService;

    // Endpoint GET para listar todos los alumnos
    @GetMapping(Constantes.RESOURCE_ALUMNOS)
    public ResponseEntity<List<Alumno>> listarAlumnos() {
        List<Alumno> alumnos = alumnoService.listarAlumnos();
        return ResponseEntity.status(HttpStatus.OK).body(alumnos);
    }

    // Endpoint GET para buscar un alumno por ID
    @GetMapping(Constantes.RESOURCE_ALUMNOS + "/{id}")
    public ResponseEntity<?> obtenerAlumnoPorId(@PathVariable("id") Long id) {

        Alumno alumno = alumnoService.buscarAlumnoPorId(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(alumno);
    }

    // Endpoint GET para buscar un alumno por DNI
    @GetMapping(Constantes.RESOURCE_ALUMNOS + "/dni/{dni}")
    public ResponseEntity<?> obtenerAlumnoPorDni(@PathVariable("dni") Integer dni) {

        Alumno alumno = alumnoService.buscarAlumnoPorDni(dni).get();
        return ResponseEntity.status(HttpStatus.OK).body(alumno);
    }

    // Endpoint POST para crear un nuevo alumno
    @PostMapping(Constantes.RESOURCE_ALUMNOS)
    public ResponseEntity<?> crearAlumno(@Valid @RequestBody AlumnoDto alumnoDto) {

        int cantidadClases = alumnoDto.getCantidadClases();
        if (!Arrays.asList(Constantes.CANTIDAD_CLASES).contains(cantidadClases)) {
            throw new IllegalArgumentException("La cantidad de clases debe ser 4, 8, 12 o 16");
        }

        Alumno alumno = new Alumno(
                alumnoDto.getDni(),
                alumnoDto.getNombre(),
                alumnoDto.getApellido(),
                alumnoDto.getFechaNacimiento(),
                alumnoDto.getTelefono(),
                alumnoDto.getEmail(),
                alumnoDto.getFechaInscripcion(),
                alumnoDto.getCantidadClases(),
                alumnoDto.isActivo(),
                alumnoDto.isPropietario());

        alumnoService.guardarAlumno(alumno);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje(
                        "Alumno " + alumnoDto.getNombre() + " " + alumnoDto.getApellido() + " creado correctamente"));
    }

    // Endpoint PUT para actualizar un alumno por ID
    @PutMapping(Constantes.RESOURCE_ALUMNOS + "/{id}")
    public ResponseEntity<?> actualizarAlumno(@PathVariable("id") Long id, @Valid @RequestBody AlumnoDto alumnoDto) {

        Alumno alumno = alumnoService.buscarAlumnoPorId(id).get();
        alumno.setDni(alumnoDto.getDni());
        alumno.setNombre(alumnoDto.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setFechaNacimiento(alumnoDto.getFechaNacimiento());
        alumno.setTelefono(alumnoDto.getTelefono());
        alumno.setEmail(alumnoDto.getEmail());
        alumno.setFechaInscripcion(alumnoDto.getFechaInscripcion());
        alumno.setCantidadClases(alumnoDto.getCantidadClases());
        alumno.setActivo(alumnoDto.isActivo());
        alumno.setPropietario(alumnoDto.isPropietario());
        alumnoService.guardarAlumno(alumno);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Alumno con ID " + id + " actualizado correctamente"));

    }

    // Endpoint DELETE para eliminar un alumno por ID (Eliminación Física)
    @DeleteMapping(Constantes.RESOURCE_ALUMNOS + "/{id}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable Long id) {

        alumnoService.eliminarAlumno(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Alumno con ID " + id + " eliminado correctamente (forma física)"));

    }

    // Endpoint DELETE para eliminar un alumno por ID (Eliminación Lógica)
@DeleteMapping(Constantes.RESOURCE_ALUMNOS + "/{id}/inactivar")
public ResponseEntity<?> eliminarAlumnoTemporalmente(@PathVariable Long id) {
    // Las validaciones ahora están en el Service
    // GlobalExceptionHandler se encarga de capturar las excepciones
    alumnoService.eliminarAlumnoTemporalmente(id);
    
    return ResponseEntity.status(HttpStatus.OK)
            .body(new Mensaje("Alumno con ID " + id + " eliminado correctamente (forma lógica)"));
}
    // Endpoint GET para buscar por diferentes filtros
    @GetMapping(Constantes.RESOURCE_ALUMNOS + "/buscar")
    public ResponseEntity<?> buscarAlumno(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Boolean propietario,
            @RequestParam(required = false) LocalDate fechaInscripcion,
            @RequestParam(required = false) LocalDate fechaNacimiento) {

        List<Alumno> alumnos = new ArrayList<>();

        if (nombre != null && apellido != null) {
            alumnos = alumnoService.buscarAlumnoPorNombreYApellido(nombre, apellido);

        }

        if (nombre != null && apellido == null) {
            alumnos = alumnoService.buscarAlumnoPorNombre(nombre);

        }

        if (nombre == null && apellido != null) {
            alumnos = alumnoService.buscarAlumnoPorApellido(apellido);

        }

        if (activo != null && activo) {
            alumnos = alumnoService.buscarAlumnoPorEstado(true);

        }

        if (activo != null && !activo) {
            alumnos = alumnoService.buscarAlumnoPorEstado(false);

        }

        if (propietario != null && propietario) {
            alumnos = alumnoService.buscarAlumnoConCaballo(true);

        }

        if (propietario != null && !propietario) {
            alumnos = alumnoService.buscarAlumnoConCaballo(false);

        }

        if (fechaInscripcion != null) {
            alumnos = alumnoService.buscarPorFechaInscripcion(fechaInscripcion);

        }

        if (fechaNacimiento != null) {
            alumnos = alumnoService.buscarPorFechaNacimiento(fechaNacimiento);

        }

        if (!alumnos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(alumnos);

        }

        alumnos = alumnoService.listarAlumnos();

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje",
                "No existen alumnos con los filtros de búsqueda ingresados, se retorna el listado completo.");
        respuesta.put("alumnos", alumnos);

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body(new Mensaje("No existen alumnos con los filtros de búsqueda
        // ingresados"));
    }

}
