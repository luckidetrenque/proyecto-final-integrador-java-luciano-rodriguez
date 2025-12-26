package com.escueladeequitacion.hrs.service;

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

    public Optional<Alumno> buscarAlumnoPorDni(Integer dni);

    public List<Alumno> buscarAlumnoPorNombre(String nombre);

    public List<Alumno> buscarAlumnoPorApellido(String apellido);

    public List<Alumno> buscarAlumnoPorNombreYApellido(String nombre, String apellido);

    public List<Alumno> buscarAlumnoPorEstado(Boolean activo);

    public List<Alumno> buscarAlumnoConCaballo(Boolean propietario);

    public List<Alumno> buscarPorFechaInscripcion(LocalDate fechaInscripcion);

    public List<Alumno> buscarPorFechaNacimiento(LocalDate fechaNacimiento);

    // Métodos para verificar la existencia de alumnos por diferentes criterios
    public Boolean existeAlumnoPorId(Long id);

    public Boolean existeAlumnoPorDni(Integer dni);

    public Boolean existeAlumnoPorNombreYApellido(String nombre, String apellido);

    // Método para verificar el estado (activo/inactivo) de un alumno por su ID
    public Boolean estadoAlumno(Long id);

    // Método para guardar un nuevo alumno
    public void guardarAlumno(Alumno alumno);

    // Método para actualizar un alumno existente
    public void actualizarAlumno(Long id, Alumno alumno);

    // Métodos para eliminar un alumno (físicamente o lógicamente)
    public void eliminarAlumno(Long id);

    public void eliminarAlumnoTemporalmente(Long id);
}
