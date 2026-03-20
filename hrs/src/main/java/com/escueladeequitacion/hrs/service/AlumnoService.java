package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.AlumnoDto;
import com.escueladeequitacion.hrs.dto.AlumnoListadoDto;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Alumno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AlumnoService {

    List<Alumno> listarAlumnos();

    List<AlumnoListadoDto> listarAlumnosListado();

    Optional<Alumno> buscarAlumnoPorId(Long id);

    Optional<AlumnoListadoDto> buscarAlumnoPorIdResumido(Long id);

    Optional<Alumno> buscarAlumnoPorDni(String dni);

    List<Alumno> buscarAlumnoPorNombre(String nombre);

    List<Alumno> buscarAlumnoPorApellido(String apellido);

    List<Alumno> buscarAlumnoPorNombreYApellido(String nombre, String apellido);

    List<Alumno> buscarAlumnoPorEstado(Boolean activo);

    List<Alumno> buscarAlumnoConCaballo(Boolean propietario);

    List<Alumno> buscarPorFechaInscripcion(LocalDate fechaInscripcion);

    List<Alumno> buscarPorFechaNacimiento(LocalDate fechaNacimiento);

    Optional<Alumno> buscarAlumnoConCaballoPorId(Long id);

    Boolean existeAlumnoPorId(Long id);

    Boolean existeAlumnoPorDni(String dni);

    Boolean existeAlumnoPorNombreYApellido(String nombre, String apellido);

    long contarClasesCompletadas(Long alumnoId);

    long contarClasesARecuperar(Long alumnoId, List<Estado> estados);

    Boolean estadoAlumno(Long id);

    void guardarAlumno(Alumno alumno);

    void actualizarAlumno(Long id, Alumno alumno);

    void eliminarAlumno(Long id);

    void eliminarAlumnoTemporalmente(Long id);

    Alumno crearAlumnoDesdeDto(AlumnoDto alumnoDto);

    void actualizarAlumnoDesdeDto(Long id, AlumnoDto alumnoDto);

    /**
     * Listado paginado con soporte de búsqueda por nombre y apellido.
     * Los parámetros nombre y apellido se evalúan con LIKE %valor% (case-insensitive).
     */
    Page<AlumnoListadoDto> listarAlumnosPaginado(
            Pageable pageable,
            Boolean activo,
            Boolean propietario,
            Integer cantidadClases,
            String nombre,
            String apellido);

    Page<AlumnoListadoDto> listarAlumnosPorInstructorPaginado(Long instructorId, Pageable pageable);

    void convertirAlumnoAPlan(Long alumnoId, Integer cantidadClases);
}
