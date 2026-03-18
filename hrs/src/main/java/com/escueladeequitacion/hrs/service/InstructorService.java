package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.InstructorDto;
import com.escueladeequitacion.hrs.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InstructorService {

    List<Instructor> listarInstructores();

    Optional<Instructor> buscarInstructorPorId(Long id);

    Optional<Instructor> buscarInstructorPorDni(String dni);

    Optional<Instructor> buscarInstructorPorColor(String color);

    List<Instructor> buscarInstructorPorNombre(String nombre);

    List<Instructor> buscarInstructorPorApellido(String apellido);

    List<Instructor> buscarInstructorPorNombreYApellido(String nombre, String apellido);

    List<Instructor> buscarInstructorsPorEstado(Boolean activo);

    List<Instructor> buscarPorFechaNacimiento(LocalDate fechaNacimiento);

    Boolean existeInstructorPorId(Long id);

    Boolean existeInstructorPorDni(String dni);

    Boolean existeInstructorPorNombreYApellido(String nombre, String apellido);

    Boolean estadoInstructor(Long id);

    void guardarInstructor(Instructor instructor);

    void actualizarInstructor(Long id, Instructor instructor);

    void eliminarInstructor(Long id);

    void eliminarInstructorTemporalmente(Long id);

    Instructor crearInstructorDesdeDto(InstructorDto instructorDto);

    void actualizarInstructorDesdeDto(Long id, InstructorDto instructorDto);

    /**
     * Listado paginado con soporte de búsqueda por nombre y apellido.
     */
    Page<Instructor> listarInstructoresPaginado(
            Pageable pageable,
            Boolean activo,
            String nombre,
            String apellido);
}
