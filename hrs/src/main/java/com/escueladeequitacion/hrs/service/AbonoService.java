package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.model.Abono;
import com.escueladeequitacion.hrs.enums.EstadoAbono;
import com.escueladeequitacion.hrs.dto.InscripcionRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface AbonoService {

    /**
     * Crea un nuevo abono desde una inscripción.
     * Este es el método principal que se llama cuando un alumno se inscribe.
     */
    @Transactional
    public Abono crearAbonoDesdeInscripcion(InscripcionRequestDto request);

    /**
     * Obtiene el abono activo de un alumno.
     */
    public Optional<Abono> obtenerAbonoActivo(Long alumnoId);

    /**
     * Descuenta una clase del abono cuando el alumno toma una clase.
     * Este método se llama desde ClaseService al completar una clase.
     */
    @Transactional
    public void descontarClase(Long abonoId);

    /**
     * Cancela un abono.
     */
    @Transactional
    public void cancelarAbono(Long abonoId, String motivo);

    /**
     * Pausa un abono temporalmente.
     */
    @Transactional
    public void pausarAbono(Long abonoId, String motivo);

    /**
     * Reactiva un abono pausado.
     */
    @Transactional
    public void reactivarAbono(Long abonoId);

    /**
     * Lista todos los abonos de un alumno (historial).
     */
    public List<Abono> listarAbonosPorAlumno(Long alumnoId);

    /**
     * Lista abonos por estado.
     */
    public List<Abono> listarAbonosPorEstado(EstadoAbono estado);

    /**
     * Actualiza abonos vencidos (tarea programada).
     * Cambia el estado de ACTIVO a VENCIDO si la fecha de vencimiento ya pasó.
     */
    @Transactional
    public void actualizarAbonosVencidos();
}