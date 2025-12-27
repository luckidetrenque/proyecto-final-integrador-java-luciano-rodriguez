package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.ClaseDto;
import com.escueladeequitacion.hrs.dto.ClaseResponseDto;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @DeleteMapping(Constantes.RESOURCE_CLASES + "/{id}")
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
        // Las validaciones ahora están en el Service
        // Si hay error, GlobalExceptionHandler lo captura automáticamente

        var instructorOpt = instructorService.buscarInstructorPorId(claseDto.getInstructorId());
        var alumnoOpt = alumnoService.buscarAlumnoPorId(claseDto.getAlumnoId());
        var caballoOpt = caballoService.buscarCaballoPorId(claseDto.getCaballoId());

        // Crear la clase
        Clase clase = new Clase();
        clase.setEspecialidades(claseDto.getEspecialidades());
        clase.setDia(claseDto.getDia());
        clase.setHora(claseDto.getHora());
        clase.setEstado(claseDto.getEstado());
        clase.setObservaciones(claseDto.getObservaciones());
        clase.setInstructor(instructorOpt.get());
        clase.setAlumno(alumnoOpt.get());
        clase.setCaballo(caballoOpt.get());

        // El Service se encarga de las validaciones y lanza excepciones si hay problemas
        claseService.guardarClase(clase);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Clase creada correctamente"));
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

    // ============================================================
    // ENDPOINTS NUEVOS - Retornan clases con detalles relacionados
    // ============================================================

    @GetMapping(Constantes.RESOURCE_CLASES + "/detalles")
    public ResponseEntity<List<ClaseResponseDto>> listarClasesConDetalles() {
        List<ClaseResponseDto> clases = claseService.listarClasesConDetalles();
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/{id}/detalles
     * Obtiene una clase específica con todos sus detalles.
     */
    @GetMapping(Constantes.RESOURCE_CLASES + "/{id}/detalles")
    public ResponseEntity<?> obtenerClaseConDetalles(@PathVariable Long id) {
        Optional<ClaseResponseDto> claseOpt = claseService.buscarClasePorIdConDetalles(id);

        if (claseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("La clase con ID " + id + " no existe en la base de datos"));
        }

        return ResponseEntity.ok(claseOpt.get());
    }

    /**
     * GET /api/v1/clases/dia/{dia}/detalles
     * Busca clases por fecha con detalles.
     * Ejemplo: /api/v1/clases/dia/2025-12-26/detalles
     */
    @GetMapping(Constantes.RESOURCE_CLASES + "/dia/{dia}/detalles")
    public ResponseEntity<?> obtenerClasesPorDiaConDetalles(@PathVariable LocalDate dia) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorDiaConDetalles(dia);

        if (clases.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("No hay clases programadas para el día " + dia));
        }

        return ResponseEntity.ok(clases);
    }

    /**
     * GET /api/v1/clases/instructor/{instructorId}/detalles
     * Obtiene todas las clases de un instructor específico.
     * Útil para que el instructor vea su agenda.
     */
    @GetMapping(Constantes.RESOURCE_CLASES + "/instructor/{instructorId}/detalles")
    public ResponseEntity<?> obtenerClasesPorInstructorConDetalles(@PathVariable Long instructorId) {
        if (!instructorService.existeInstructorPorId(instructorId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("El instructor con ID " + instructorId + " no existe"));
        }

        List<ClaseResponseDto> clases = claseService.buscarClasePorInstructorConDetalles(instructorId);
        return ResponseEntity.ok(clases);
    }

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/detalles
     * Obtiene todas las clases de un alumno específico.
     * Útil para que el alumno vea sus clases programadas.
     */
    @GetMapping(Constantes.RESOURCE_CLASES + "/alumno/{alumnoId}/detalles")
    public ResponseEntity<?> obtenerClasesPorAlumnoConDetalles(@PathVariable Long alumnoId) {
        if (!alumnoService.existeAlumnoPorId(alumnoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("El alumno con ID " + alumnoId + " no existe"));
        }

        List<ClaseResponseDto> clases = claseService.buscarClasePorAlumnoConDetalles(alumnoId);
        return ResponseEntity.ok(clases);
    }
}
