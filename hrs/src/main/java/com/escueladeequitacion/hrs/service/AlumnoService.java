package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.AlumnoDto;
import com.escueladeequitacion.hrs.dto.AlumnoPruebaDto;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Alumno;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Interfaz para el servicio de Alumno
public interface AlumnoService {

    // Método para listar todos los alumnos
    public List<Alumno> listarAlumnos();

    // Métodos para buscar alumnos por diferentes criterios
    public Optional<Alumno> buscarAlumnoPorId(Long id);

    public Optional<Alumno> buscarAlumnoPorDni(String dni);

    public List<Alumno> buscarAlumnoPorNombre(String nombre);

    public List<Alumno> buscarAlumnoPorApellido(String apellido);

    public List<Alumno> buscarAlumnoPorNombreYApellido(String nombre, String apellido);

    public List<Alumno> buscarAlumnoPorEstado(Boolean activo);

    public List<Alumno> buscarAlumnoConCaballo(Boolean propietario);

    public List<Alumno> buscarPorFechaInscripcion(LocalDate fechaInscripcion);

    public List<Alumno> buscarPorFechaNacimiento(LocalDate fechaNacimiento);

    // Métodos para verificar la existencia de alumnos por diferentes criterios
    public Boolean existeAlumnoPorId(Long id);

    public Boolean existeAlumnoPorDni(String dni);

    public Boolean existeAlumnoPorNombreYApellido(String nombre, String apellido);

    // Métodos para contar las clases completadas de un alumno
    public long contarClasesCompletadas(Long alumnoId);

    // Métodos para contar las clases a recuperar (canceladas o ausente con aviso)
    // de un alumno
    public long contarClasesARecuperar(Long alumnoId, List<Estado> estados);

    // Método para verificar el estado (activo/inactivo) de un alumno por su ID
    public Boolean estadoAlumno(Long id);

    // Método para guardar un nuevo alumno
    public void guardarAlumno(Alumno alumno);

    // Método para actualizar un alumno existente
    public void actualizarAlumno(Long id, Alumno alumno);

    // Métodos para eliminar un alumno (físicamente o lógicamente)
    public void eliminarAlumno(Long id);

    public void eliminarAlumnoTemporalmente(Long id);

    /**
     * Crea un alumno desde un DTO con validaciones.
     */
    public Alumno crearAlumnoDesdeDto(AlumnoDto alumnoDto);

    /**
     * Actualiza un alumno desde un DTO con validaciones.
     */
    public void actualizarAlumnoDesdeDto(Long id, AlumnoDto alumnoDto);

    /**
     * Busca alumnos con múltiples filtros.
     */
    public List<Alumno> buscarAlumnosConFiltros(String nombre, String apellido, Boolean activo,
            Boolean propietario, LocalDate fechaInscripcion,
            LocalDate fechaNacimiento);

    public void convertirAlumnoAPlan(Long alumnoId, Integer cantidadClases);

    /**
     * Crea un alumno para clase de prueba (activo=false, cantidadClases=0).
     */
    Alumno crearAlumnoDePrueba(AlumnoPruebaDto alumnoDto);
}
