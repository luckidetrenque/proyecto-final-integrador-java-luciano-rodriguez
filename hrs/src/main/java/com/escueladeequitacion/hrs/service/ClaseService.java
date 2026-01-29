package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.ClaseDto;
import com.escueladeequitacion.hrs.dto.ClaseResponseDto;
import com.escueladeequitacion.hrs.enums.Especialidad;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Clase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Interfaz para el servicio de Clase
public interface ClaseService {

    // Método para listar todas las clases
    public List<Clase> listarClases();

    // Métodos para buscar caballos por diferentes criterios
    public Optional<Clase> buscarClasePorId(Long id);

    public List<Clase> buscarClasePorEspecialidad(Especialidad especialidad);

    public List<Clase> buscarClasePorDia(LocalDate dia);

    public List<Clase> buscarClasePorHora(LocalTime hora);

    public List<Clase> buscarClasePorEstado(Estado estado);

    public List<Clase> buscarClasePorInstructor(Long instructor_id);

    public List<Clase> buscarClasePorAlumno(Long alumno_id);

    public List<Clase> buscarClasePorCaballo(Long caballo_id);

    // Métodos para verificar la existencia de clases por diferentes criterios
    public Boolean existeClasePorId(Long id);

    public Boolean existeClasePorEspecialidad(Especialidad especialidad);

    public Boolean existeClasePorDia(LocalDate fechas);

    public Boolean existeClasePorHora(LocalTime hora);

    public Boolean existeClasePorEstado(Estado estado);

    public Boolean existeClasePorInstructor(Long instructor_id);

    public Boolean existeClasePorAlumno(Long alumno_id);

    public Boolean existeClasePorCaballo(Long caballo_id);

    // Método para verificar el estado ("Programada", "Iniciada", "Completada",
    // "Cancelada") de una clase por su ID
    // public Estado estadoClase(Estado estado);

    /**
     * Método para crear una clase desde un DTO, validando todas las reglas de
     * negocio.
     */
    public Clase crearClase(ClaseDto claseDto);

    /**
     * Método para actualizar una clase desde un DTO, validando todas las reglas de
     * negocio.
     */
    public void actualizarClase(Long id, ClaseDto claseDto);

    // Métodos para eliminar una clase (físicamente o lógicamente)
    public void eliminarClase(Long id);

    public void eliminarClaseTemporalmente(Long id);

    // ============================================================
    // MÉTODOS NUEVOS - Retornan DTOs con datos relacionados
    // ============================================================

    /**
     * Lista todas las clases con detalles de instructor, alumno y caballo.
     * 
     * @return Lista de ClaseResponseDto
     */
    public List<ClaseResponseDto> listarClasesConDetalles();

    /**
     * Busca una clase por ID con todos sus detalles.
     * 
     * @param id - ID de la clase
     * @return Optional con ClaseResponseDto
     */
    public Optional<ClaseResponseDto> buscarClasePorIdConDetalles(Long id);

    /**
     * Busca clases por día con detalles.
     */
    public List<ClaseResponseDto> buscarClasePorDiaConDetalles(LocalDate dia);

    /**
     * Busca clases por instructor con detalles.
     */
    public List<ClaseResponseDto> buscarClasePorInstructorConDetalles(Long instructorId);

    /**
     * Busca clases por alumno con detalles.
     */
    public List<ClaseResponseDto> buscarClasePorAlumnoConDetalles(Long alumnoId);

    /**
     * Busca clases por caballo con detalles.
     */
    public List<ClaseResponseDto> buscarClasePorCaballoConDetalles(Long caballoId);

    /**
     * Busca clases por estado con detalles.
     */
    public List<ClaseResponseDto> buscarClasePorEstadoConDetalles(Estado estado);

    /**
     * Cuenta clases completadas por alumno.
     */
    public long contarClasesCompletadasPorAlumno(Long alumnoId);

    /**
     * Cuenta clases completadas por instructor.
     */
    public long contarClasesCompletadasPorInstructor(Long instructorId);

    /**
     * Cuenta clases completadas por caballo.
     */
    public long contarClasesCompletadasPorCaballo(Long caballoId);

    public void actualizarEstadosDeClases();

    // ============================================================
    // MÉTODOS PARA CLASES DE PRUEBA
    // ============================================================

    /**
     * Verifica si un alumno ya tomó al menos una clase de prueba.
     * 
     * @param alumnoId - ID del alumno
     * @return true si tiene clase de prueba, false si no
     */
    boolean alumnoTieneClaseDePrueba(Long alumnoId);

    /**
     * Cuenta cuántas clases de prueba ha tomado un alumno.
     * 
     * @param alumnoId - ID del alumno
     * @return Cantidad de clases de prueba
     */
    long contarClasesDePruebaPorAlumno(Long alumnoId);

    /**
     * Obtiene información completa sobre clases de prueba de un alumno.
     * 
     * @param alumnoId - ID del alumno
     * @return Map con alumnoId, tienePrueba y cantidadClasesPrueba
     */
    Map<String, Object> obtenerInfoClasesDePrueba(Long alumnoId);

    /**
     * Lista las clases de prueba de un alumno con detalles.
     * 
     * @param alumnoId - ID del alumno
     * @return Lista de ClaseResponseDto de clases de prueba
     */
    List<ClaseResponseDto> listarClasesDePruebaPorAlumno(Long alumnoId);

    /**
     * Lista todas las clases de prueba del sistema con detalles.
     * 
     * @return Lista de ClaseResponseDto de todas las clases de prueba
     */
    List<ClaseResponseDto> listarTodasLasClasesDePrueba();
}