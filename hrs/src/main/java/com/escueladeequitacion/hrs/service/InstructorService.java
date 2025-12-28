package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.InstructorDto;
import com.escueladeequitacion.hrs.model.Instructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Interfaz para el servicio de Instructor
public interface InstructorService {

    // Método para listar todos los instructores
    public List<Instructor> listarInstructores();

    // Métodos para buscar instructores por diferentes criterios
    public Optional<Instructor> buscarInstructorPorId(Long id);

    public Optional<Instructor> buscarInstructorPorDni(Integer dni);

    public List<Instructor> buscarInstructorPorNombre(String nombre);

    public List<Instructor> buscarInstructorPorApellido(String apellido);

    public List<Instructor> buscarInstructorPorNombreYApellido(String nombre, String apellido);

    public List<Instructor> buscarInstructorsPorEstado(Boolean activo);

    public List<Instructor> buscarPorFechaNacimiento(LocalDate fechaNacimiento);

    // Métodos para verificar la existencia de instructores por diferentes criterios
    public Boolean existeInstructorPorId(Long id);

    public Boolean existeInstructorPorDni(Integer dni);

    public Boolean existeInstructorPorNombreYApellido(String nombre, String apellido);

    // Método para verificar el estado (activo/inactivo) de un instructor por su ID
    public Boolean estadoInstructor(Long id);

    // Método para guardar un nuevo instructor
    public void guardarInstructor(Instructor instructor);

    // Método para actualizar un instructor existente
    public void actualizarInstructor(Long id, Instructor instructor);

    // Métodos para eliminar un instructor (físicamente o lógicamente)
    public void eliminarInstructor(Long id);

    public void eliminarInstructorTemporalmente(Long id);

    /**
     * Crea un instructor desde un DTO.
     */
    public Instructor crearInstructorDesdeDto(InstructorDto instructorDto);

    /**
     * Actualiza un instructor desde un DTO.
     */
    public void actualizarInstructorDesdeDto(Long id, InstructorDto instructorDto);

    /**
     * Busca instructores con múltiples filtros.
     */
    public List<Instructor> buscarInstructoresConFiltros(String nombre, String apellido,
            Boolean activo, LocalDate fechaNacimiento);
}
