package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.AlumnoDto;
import com.escueladeequitacion.hrs.dto.AlumnoPruebaDto;
import com.escueladeequitacion.hrs.dto.ConversionAPlanRequest;
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
    /**
     * GET /api/v1/alumnos
     * Lista todos los alumnos.
     */
    @GetMapping(Constantes.RESOURCE_ALUMNOS)
    public ResponseEntity<List<Alumno>> listarAlumnos() {
        List<Alumno> alumnos = alumnoService.listarAlumnos();
        return ResponseEntity.status(HttpStatus.OK).body(alumnos);
    }

    // Endpoint GET para buscar un alumno por ID
    /**
     * GET /api/v1/alumnos/{id}
     * Obtiene un alumno por ID.
     */
    @GetMapping(Constantes.RESOURCE_ALUMNOS + "/{id}")
    public ResponseEntity<?> obtenerAlumnoPorId(@PathVariable("id") Long id) {

        Alumno alumno = alumnoService.buscarAlumnoPorId(id)
                .orElseThrow(
                        () -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Alumno", "ID", id));
        return ResponseEntity.status(HttpStatus.OK).body(alumno);
    }

    // Endpoint GET para buscar un alumno por DNI
    /**
     * GET /api/v1/alumnos/dni/{dni}
     * Obtiene un alumno por DNI.
     */
    @GetMapping(Constantes.RESOURCE_ALUMNOS + "/dni/{dni}")
    public ResponseEntity<?> obtenerAlumnoPorDni(@PathVariable("dni") String dni) {

        Alumno alumno = alumnoService.buscarAlumnoPorDni(dni)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Alumno", "DNI",
                        dni));
        return ResponseEntity.status(HttpStatus.OK).body(alumno);
    }

    // Endpoint POST para crear un nuevo alumno
    /**
     * POST /api/v1/alumnos
     * Crea un nuevo alumno.
     */
    @PostMapping(Constantes.RESOURCE_ALUMNOS)
    public ResponseEntity<?> crearAlumno(@Valid @RequestBody AlumnoDto alumnoDto) {
        // El Service valida:
        // 1. DNI duplicado
        // 2. Cantidad de clases (4, 8, 12, 16)
        Alumno alumno = alumnoService.crearAlumnoDesdeDto(alumnoDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Alumno " + alumno.getNombreCompleto() + " creado correctamente"));
    }

    // Endpoint PUT para actualizar un alumno por ID
    /**
     * PUT /api/v1/alumnos/{id}
     * Actualiza un alumno existente.
     */
    @PutMapping(Constantes.RESOURCE_ALUMNOS + "/{id}")
    public ResponseEntity<?> actualizarAlumno(@PathVariable("id") Long id, @Valid @RequestBody AlumnoDto alumnoDto) {
        // El Service valida:
        // 1. Alumno existe
        // 2. DNI no duplicado con otro alumno
        // 3. Cantidad de clases válida
        alumnoService.actualizarAlumnoDesdeDto(id, alumnoDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Alumno con ID " + id + " actualizado correctamente"));
    }

    // Endpoint DELETE para eliminar un alumno por ID (Eliminación Física)
    /**
     * DELETE /api/v1/alumnos/{id}
     * Elimina un alumno (eliminación física).
     */
    @DeleteMapping(Constantes.RESOURCE_ALUMNOS + "/{id}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable Long id) {
        // El Service valida que existe antes de eliminar
        alumnoService.eliminarAlumno(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Alumno con ID " + id + " eliminado correctamente"));
    }

    // Endpoint DELETE para eliminar un alumno por ID (Eliminación Lógica)
    /**
     * DELETE /api/v1/alumnos/{id}/inactivar
     * Inactiva un alumno (eliminación lógica).
     */
    @DeleteMapping(Constantes.RESOURCE_ALUMNOS + "/{id}/inactivar")
    public ResponseEntity<?> eliminarAlumnoTemporalmente(@PathVariable Long id) {
        // El Service valida:
        // 1. Alumno existe
        // 2. Alumno está activo
        alumnoService.eliminarAlumnoTemporalmente(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Alumno con ID " + id + " inactivado correctamente"));
    }

    // Endpoint GET para contar las clases completadas de un alumno
    @GetMapping(Constantes.RESOURCE_ALUMNOS + "/{id}/clases/completadas/count")
    public ResponseEntity<?> contarClasesCompletadas(@PathVariable Long id) {

        long count = alumnoService.contarClasesCompletadas(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", id);
        response.put("clasesCompletadas", count);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Endpoint GET para buscar por diferentes filtros
    /**
     * GET /api/v1/alumnos/buscar
     * Busca alumnos por múltiples criterios.
     */
    @GetMapping(Constantes.RESOURCE_ALUMNOS + "/buscar")
    public ResponseEntity<?> buscarAlumno(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Boolean propietario,
            @RequestParam(required = false) LocalDate fechaInscripcion,
            @RequestParam(required = false) LocalDate fechaNacimiento) {

        List<Alumno> alumnos = alumnoService.buscarAlumnosConFiltros(
                nombre, apellido, activo, propietario, fechaInscripcion, fechaNacimiento);

        if (!alumnos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(alumnos);
        }

        // Si no hay resultados, devolver todos con mensaje
        alumnos = alumnoService.listarAlumnos();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje",
                "No existen alumnos con los filtros de búsqueda ingresados, se retorna el listado completo.");
        respuesta.put("alumnos", alumnos);

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

    /**
     * Convierte un alumno de clase de prueba a alumno regular con plan de clases.
     * 
     * PUT /api/v1/alumnos/{id}/convertir-a-plan
     * 
     * @param id      - ID del alumno
     * @param request - Contiene cantidadClases (4, 8, 12 o 16)
     * @return ResponseEntity con mensaje de éxito
     */
    @PutMapping(Constantes.RESOURCE_ALUMNOS + "/{id}/convertir-a-plan")
    public ResponseEntity<?> convertirAlumnoAPlan(
            @PathVariable Long id,
            @RequestBody @Valid ConversionAPlanRequest request) {

        alumnoService.convertirAlumnoAPlan(id, request.getCantidadClases());

        return ResponseEntity.ok(Map.of(
                "mensaje", "Alumno convertido a plan regular exitosamente",
                "alumnoId", id,
                "cantidadClases", request.getCantidadClases()));
    }

    /**
     * Lista alumnos que están en estado de prueba (activo=false).
     * 
     * GET /api/v1/alumnos/prueba
     * 
     * @return Lista de alumnos inactivos (potenciales clases de prueba)
     */
    @GetMapping(Constantes.RESOURCE_ALUMNOS + "/prueba")
    public ResponseEntity<List<Alumno>> listarAlumnosDePrueba() {
        List<Alumno> alumnosPrueba = alumnoService.buscarAlumnoPorEstado(false);
        return ResponseEntity.ok(alumnosPrueba);
    }

    /**
     * Crea un alumno para clase de prueba (con valores por defecto).
     * 
     * POST /api/v1/alumnos/prueba
     * 
     * @param alumnoDto - Datos básicos del alumno (sin fechaInscripcion ni
     *                  cantidadClases)
     * @return Alumno creado
     */
    @PostMapping(Constantes.RESOURCE_ALUMNOS + "/prueba")
    public ResponseEntity<Alumno> crearAlumnoDePrueba(@RequestBody @Valid AlumnoPruebaDto alumnoDto) {
        Alumno alumno = alumnoService.crearAlumnoDePrueba(alumnoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(alumno);
    }

}
