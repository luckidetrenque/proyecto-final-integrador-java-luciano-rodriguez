package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.model.PlanAbono;
import com.escueladeequitacion.hrs.model.PlanPension;
import com.escueladeequitacion.hrs.dto.InscripcionRequestDto;
import com.escueladeequitacion.hrs.dto.PrecioCalculadoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface PrecioService {

    /**
     * Calcula el precio total de una inscripción según los parámetros.
     * Este es el método principal que usa todo el sistema.
     */
    public PrecioCalculadoDto calcularPrecioInscripcion(InscripcionRequestDto request);

    /**
     * Lista todos los planes de abono vigentes en una fecha.
     * Útil para mostrar catálogo de precios.
     */
    public List<PlanAbono> listarPlanesVigentes(LocalDate fecha);

    /**
     * Lista todos los planes de pensión vigentes.
     */
    public List<PlanPension> listarPlanesPensionVigentes(LocalDate fecha);
}