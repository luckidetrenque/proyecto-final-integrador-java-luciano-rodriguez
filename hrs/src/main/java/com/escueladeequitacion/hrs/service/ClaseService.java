package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.enums.Especialidades;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Clase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

// Interfaz para el servicio de Clase
public interface ClaseService {

    // Método para listar todas las clases
    public List<Clase> listarClases();

    // Métodos para buscar caballos por diferentes criterios
    public Optional<Clase> buscarClasePorId(Long id);

    public List<Clase> buscarClasePorEspecialidad(Especialidades especialidad);

    public List<Clase> buscarClasePorDia(LocalDate dia);

    public List<Clase> buscarClasePorHora(LocalTime hora);

    public List<Clase> buscarClasePorEstado(Estado estado);

    public List<Clase> buscarClasePorInstructor(Long instructor_id);

    public List<Clase> buscarClasePorAlumno(Long alumno_id);

    public List<Clase> buscarClasePorCaballo(Long caballo_id);

    // Métodos para verificar la existencia de clases por diferentes criterios
    public Boolean existeClasePorId(Long id);

    public Boolean existeClasePorEspecialidades(Especialidades especialidades);

    public Boolean existeClasePorDia(LocalDate fechas);

    public Boolean existeClasePorHora(LocalTime hora);

    public Boolean existeClasePorInstructor(Long instructor_id);

    public Boolean existeClasePorAlumno(Long alumno_id);

    public Boolean existeClasePorCaballo(Long caballo_id);

    // Método para verificar el estado ("Programada", "En curso", "Completada", "Cancelada") de una clase por su ID
    // public Estado estadoClase(Estado estado);

    // Método para guardar una nueva clase
    public void guardarClase(Clase clase);

    // Método para actualizar una clase existente
    public void actualizarClase(Long id, Clase clase);

    // Métodos para eliminar una clase (físicamente o lógicamente)
    public void eliminarClase(Long id);

    public void eliminarClaseTemporalmente(Long id);

}
