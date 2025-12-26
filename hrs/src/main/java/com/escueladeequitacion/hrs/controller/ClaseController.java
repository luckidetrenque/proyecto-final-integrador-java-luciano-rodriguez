package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.ClaseDto;
import com.escueladeequitacion.hrs.model.Clase;
// import com.escueladeequitacion.hrs.model.Instructor;
import com.escueladeequitacion.hrs.service.ClaseService;
import com.escueladeequitacion.hrs.service.AlumnoService;
import com.escueladeequitacion.hrs.service.InstructorService;
import com.escueladeequitacion.hrs.service.CaballoService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import com.escueladeequitacion.hrs.utility.Constantes;

// import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constantes.API_VERSION)
@CrossOrigin(origins = { Constantes.ORIGINS })

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

    // Endpoint GET para listar todos las clases
    @GetMapping(Constantes.RESOURCE_CLASES)
    public ResponseEntity<List<Clase>> listarClases() {
        List<Clase> clases = claseService.listarClases();
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

        // Endpoint GET para buscar una clase por ID
    @GetMapping(Constantes.RESOURCE_CLASES + "/{id}")
    public ResponseEntity<?> obtenerClasePorId(@PathVariable("id") Long id) {
        if (!claseService.existeClasePorId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("La clase con ID " + id + " no existe en la base de datos"));
        }

        Clase clase = claseService.buscarClasePorId(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(clase);
    }

    // Endpoint GET para buscar una clase por fecha
@GetMapping(Constantes.RESOURCE_CLASES + "/dia/{dia}")
public ResponseEntity<?> obtenerClasePorDia(@PathVariable("dia") LocalDate dia) {
    List<Clase> clases = claseService.buscarClasePorDia(dia);
    
    if (clases.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Mensaje("La clase con día " + dia + " no existe en la base de datos"));
    }
    
    return ResponseEntity.ok(clases.get(0));
}
    

    // Endpoint DELETE para eliminar un clase por ID
    @DeleteMapping(Constantes.RESOURCE_CLASES  + "/{id}")
    public ResponseEntity<?> eliminarClase(@PathVariable Long id) {
        if (!claseService.existeClasePorId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el clase con ese ID"));
        }
        claseService.eliminarClase(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Clase eliminada correctamente"));

    }

    // Endpoint POST para crear una nueva clase
    @PostMapping(Constantes.RESOURCE_CLASES)
    public ResponseEntity<?> crearClase(@Valid @RequestBody ClaseDto claseDto) {
        try {
        // Validar que instructor, alumno y caballo existan
        var instructorOpt = instructorService.buscarInstructorPorId(claseDto.getInstructorId());
        var alumnoOpt = alumnoService.buscarAlumnoPorId(claseDto.getAlumnoId());
        var caballoOpt = caballoService.buscarCaballoPorId(claseDto.getCaballoId());

            if (instructorOpt.isEmpty() || alumnoOpt.isEmpty() || caballoOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Mensaje("Instructor, alumno o caballo no encontrados"));
            }

            // Validar el alumno y el instructor estén activos y el caballo esté disponible
                    if (!instructorService.estadoInstructor(claseDto.getInstructorId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Mensaje("El instructor no está activo"));
        }  
                            if (!caballoService.estadoCaballo(claseDto.getCaballoId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Mensaje("El caballo no está disponible"));
        }
                            if (!alumnoService.estadoAlumno(claseDto.getAlumnoId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Mensaje("El alumno no está activo"));
        }

        // Validar que el dia de la clase sea válido
        LocalDate hoy = LocalDate.now();
        if (!claseDto.getDia().isEqual(hoy) && !claseDto.getDia().isAfter(hoy)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Mensaje("El día de la clase debe ser hoy o posterior"));
        } else if (claseDto.getDia().isEqual(hoy)) {
            // Si es hoy solamente se puede crear una clase con  60 minutos de anticipacion
             LocalTime ahora = LocalTime.now();
             if (claseDto.getHora().isBefore(ahora.plusMinutes(60))) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Mensaje("La clase se debe asignar con 60 minutos de anticipacion"));
             }
        }

        // que no exista una clase ya asignada con los mismos datos
                if (claseService.existeClasePorAlumno(claseDto.getAlumnoId()) &&
                    claseService.existeClasePorInstructor(claseDto.getInstructorId()) &&
                    claseService.existeClasePorCaballo(claseDto.getCaballoId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Mensaje("Ya existe una clase del alumno en la base de datos"));
        }

            Clase clase = new Clase();
            clase.setEspecialidades(claseDto.getEspecialidades());
            clase.setDia(claseDto.getDia());
            clase.setHora(claseDto.getHora());
            clase.setEstado(claseDto.getEstado());
            clase.setObservaciones(claseDto.getObservaciones());
            clase.setInstructor(instructorOpt.get());
            clase.setAlumno(alumnoOpt.get());
            clase.setCaballo(caballoOpt.get());

            claseService.guardarClase(clase);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("Clase: Alumno: " + alumnoOpt.get() + " Caballo: " + caballoOpt.get() + " Instructor: " + instructorOpt.get() + " Día: " + claseDto.getHora() + " Hora: " + claseDto.getHora() + " creada correctamente"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(ex.getMessage()));
        }
    }

    // Endpoint PUT para actualizar un clase por ID
    @PutMapping(Constantes.RESOURCE_CLASES + "/{id}")
    public ResponseEntity<?> actualizarOnstructor(@PathVariable("id") Long id,
            @Valid @RequestBody ClaseDto claseDto) {
        if (!claseService.existeClasePorId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe la clase con ese ID"));
        }
        try {
            var claseOpt = claseService.buscarClasePorId(id);
            if (claseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe la clase con ese ID"));
            }

            var clase = claseOpt.get();
            // resolver nuevas asociaciones si vienen en DTO
            if (claseDto.getInstructorId() != null) {
                var instr = instructorService.buscarInstructorPorId(claseDto.getInstructorId());
                if (instr.isEmpty())
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Instructor no encontrado"));
                clase.setInstructor(instr.get());
            }
            if (claseDto.getAlumnoId() != null) {
                var alum = alumnoService.buscarAlumnoPorId(claseDto.getAlumnoId());
                if (alum.isEmpty())
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Alumno no encontrado"));
                clase.setAlumno(alum.get());
            }
            if (claseDto.getCaballoId() != null) {
                var cab = caballoService.buscarCaballoPorId(claseDto.getCaballoId());
                if (cab.isEmpty())
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Caballo no encontrado"));
                clase.setCaballo(cab.get());
            }

            if (claseDto.getEspecialidades() != null)
                clase.setEspecialidades(claseDto.getEspecialidades());
            if (claseDto.getDia() != null)
                clase.setDia(claseDto.getDia());
            if (claseDto.getEstado() != null)
                clase.setEstado(claseDto.getEstado());

            claseService.guardarClase(clase);
            return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Clase modificada correctamente"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(ex.getMessage()));
        }

    }
}
