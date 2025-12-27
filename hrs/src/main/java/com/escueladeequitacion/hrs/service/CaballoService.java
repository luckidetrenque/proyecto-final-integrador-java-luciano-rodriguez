package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.enums.TipoCaballo;
import com.escueladeequitacion.hrs.model.Caballo;

import java.util.List;
import java.util.Optional;

// Interfaz para el servicio de Caballo
public interface CaballoService {

    // Método para listar todos los caballos
    public List<Caballo> listarCaballos();

    // Métodos para buscar caballos por diferentes criterios
    public Optional<Caballo> buscarCaballoPorId(Long id);

    public List<Caballo> buscarCaballoPorNombre(String nombre);

    public List<Caballo> buscarCaballoPorEstado(Boolean disponible);

    public List<Caballo> buscarCaballoPorTipo(TipoCaballo tipoCaballo);

    // Métodos para verificar la existencia de caballos por diferentes criterios
    public Boolean existeCaballoPorId(Long id);

    public Boolean existeCaballoPorNombre(String nombre);

    // Método para verificar el estado (disponible/no disponible) de un caballo por
    // su ID
    public Boolean estadoCaballo(Long id);

    // Método para guardar un nuevo caballo
    public void guardarCaballo(Caballo caballo);

    // Método para actualizar un caballo existente
    public void actualizarCaballo(Long id, Caballo caballo);

    // Métodos para eliminar un alumno (físicamente o lógicamente)
    public void eliminarCaballo(Long id);

    public void eliminarCaballoTemporalmente(Long id);

}
