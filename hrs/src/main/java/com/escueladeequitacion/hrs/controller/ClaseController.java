package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.ClaseDto;
import com.escueladeequitacion.hrs.dto.ClaseDto.AlActualizar;
import com.escueladeequitacion.hrs.dto.ClaseResponseDto;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Clase;

import com.escueladeequitacion.hrs.service.ClaseService;
import com.escueladeequitacion.hrs.service.AlumnoService;
import com.escueladeequitacion.hrs.service.InstructorService;
import com.escueladeequitacion.hrs.service.CaballoService;
import com.escueladeequitacion.hrs.utility.Mensaje;

import jakarta.validation.Valid;

import com.escueladeequitacion.hrs.utility.Constantes;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // ============================================================
    // ENDPOINTS BÁSICOS (sin detalles)
    // ============================================================

    // Endpoint GET para listar todos las clases
    /**
     * GET /api/v1/clases
     * Lista todas las clases (sin detalles de relaciones).
     */
    @GetMapping(Constantes.RESOURCE_CLASES)
    public ResponseEntity<List<Clase>> listarClases() {
        List<Clase> clases = claseService.listarClases();
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    // Endpoint GET para buscar una clase por ID
    /**
     * GET /api/v1/clases/{id}
     * Obtiene una clase por ID.
     */
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

        return ResponseEntity.status(HttpStatus.OK).body(clases.get(0));
    }

    // Endpoint DELETE para eliminar un clase por ID
    /**
     * DELETE /api/v1/clases/{id}
     * Elimina una clase (eliminación física).
     */
    @DeleteMapping(Constantes.RESOURCE_CLASES + "/{id}")
    public ResponseEntity<?> eliminarClase(@PathVariable Long id) {
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
    @PostMapping(Constantes.RESOURCE_CLASES)
    // public ResponseEntity<?> crearClase(@Validated(AlCrear.class) @RequestBody
    // ClaseDto claseDto) {
    public ResponseEntity<?> crearClase(@Valid @RequestBody ClaseDto claseDto) {

        Clase clase = claseService.crearClase(claseDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Clase creada correctamente con ID: " + clase.getId()));
    }

    // Endpoint PUT para actualizar un clase por ID
    /**
     * PUT /api/v1/clases/{id}
     * Actualiza una clase existente.
     */
    @PutMapping(Constantes.RESOURCE_CLASES + "/{id}")
    public ResponseEntity<?> actualizarClase(@PathVariable Long id,
            @Validated(AlActualizar.class) @RequestBody ClaseDto claseDto) {
        // El Service maneja todas las validaciones
        claseService.actualizarClase(id, claseDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Clase con ID " + id + " actualizada correctamente"));
    }

    // ============================================================
    // ENDPOINTS CON DETALLES (relaciones cargadas)
    // ============================================================

    /**
     * GET /api/v1/clases/detalles
     * Lista todas las clases con información de instructor, alumno y caballo.
     */
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
    public ResponseEntity<ClaseResponseDto> obtenerClaseConDetalles(@PathVariable Long id) {
        ClaseResponseDto clase = claseService.buscarClasePorIdConDetalles(id)
                .orElseThrow(
                        () -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Clase", "ID", id));

        return ResponseEntity.status(HttpStatus.OK).body(clase);
    }

    /**
     * GET /api/v1/clases/dia/{dia}/detalles
     * Busca clases por fecha con detalles.
     */
    @GetMapping(Constantes.RESOURCE_CLASES + "/dia/{dia}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorDiaConDetalles(@PathVariable LocalDate dia) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorDiaConDetalles(dia);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/instructor/{instructorId}/detalles
     * Obtiene todas las clases de un instructor específico.
     */
    @GetMapping(Constantes.RESOURCE_CLASES + "/instructor/{instructorId}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorInstructorConDetalles(
            @PathVariable Long instructorId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorInstructorConDetalles(instructorId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/detalles
     * Obtiene todas las clases de un alumno específico.
     */
    @GetMapping(Constantes.RESOURCE_CLASES + "/alumno/{alumnoId}/detalles")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesPorAlumnoConDetalles(@PathVariable Long alumnoId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorAlumnoConDetalles(alumnoId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    /**
     * GET /api/v1/clases/alumno/{alumnoId}/detalles
     * Obtiene todas las clases de un alumno específico.
     */
    @GetMapping(Constantes.RESOURCE_CLASES + "/caballo/{caballoId}/detalles")
    public ResponseEntity<?> obtenerClasesPorCaballoConDetalles(@PathVariable Long caballoId) {
        List<ClaseResponseDto> clases = claseService.buscarClasePorCaballoConDetalles(caballoId);
        return ResponseEntity.status(HttpStatus.OK).body(clases);
    }

    @GetMapping(Constantes.RESOURCE_CLASES + "/estado/{estado}/detalles")
    public ResponseEntity<?> obtenerClasesPorEstadoConDetalles(@PathVariable Estado estado) {
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
    @GetMapping(Constantes.RESOURCE_CLASES + "/alumno/{alumnoId}/completadas/count")
    public ResponseEntity<Map<String, Object>> contarClasesCompletadasPorAlumno(@PathVariable Long alumnoId) {
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
    @GetMapping(Constantes.RESOURCE_CLASES + "/instructor/{instructorId}/completadas/count")
    public ResponseEntity<Map<String, Object>> contarClasesCompletadasPorInstructor(@PathVariable Long instructorId) {
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
    @GetMapping("/alumno/{alumnoId}/prueba")
    public ResponseEntity<List<ClaseResponseDto>> obtenerClasesDePruebaPorAlumno(
            @PathVariable Long alumnoId) {

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
    @GetMapping("/alumno/{alumnoId}/tiene-prueba")
    public ResponseEntity<Map<String, Object>> verificarClaseDePrueba(@PathVariable Long alumnoId) {
        Map<String, Object> info = claseService.obtenerInfoClasesDePrueba(alumnoId);
        return ResponseEntity.ok(info);
    }

}
