package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.CaballoDto;
import com.escueladeequitacion.hrs.enums.Tipo;
import com.escueladeequitacion.hrs.model.Caballo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CaballoService {

    List<Caballo> listarCaballos();

    Optional<Caballo> buscarCaballoPorId(Long id);

    List<Caballo> buscarCaballoPorNombre(String nombre);

    List<Caballo> buscarCaballoPorEstado(Boolean disponible);

    List<Caballo> buscarCaballoPorTipo(Tipo tipo);

    Boolean existeCaballoPorId(Long id);

    Boolean existeCaballoPorNombre(String nombre);

    Boolean estadoCaballo(Long id);

    void guardarCaballo(Caballo caballo);

    void actualizarCaballo(Long id, Caballo caballo);

    void eliminarCaballo(Long id);

    void eliminarcaballoTemporalmente(Long id);

    Caballo crearCaballoDesdeDto(CaballoDto caballoDto);

    void actualizarCaballoDesdeDto(Long id, CaballoDto caballoDto);

    /**
     * Listado paginado con soporte de búsqueda por nombre (LIKE).
     */
    Page<Caballo> listarCaballos(Pageable pageable, Boolean disponible, Tipo tipo, String nombre);

    Optional<Caballo> buscarCaballoConAlumnosPorId(Long id);
}
