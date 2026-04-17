package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.model.Abono;
import com.escueladeequitacion.hrs.model.Factura;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public interface FacturacionService {

    /**
     * Genera factura mensual automáticamente para un abono.
     * Se llama desde el AbonoService al crear el abono.
     */
    @Transactional
    public Factura generarFacturaMensual(Abono abono, LocalDate fechaEmision);

    /**
     * Aplica recargo por mora si la factura está vencida.
     * Se ejecuta automáticamente por tarea programada.
     */
    @Transactional
    public void aplicarRecargoMora(Factura factura);

    /**
     * Lista facturas de un alumno.
     */
    public List<Factura> listarFacturasPorAlumno(Long alumnoId);

    /**
     * Lista facturas pendientes de un alumno.
     */
    public List<Factura> listarFacturasPendientesPorAlumno(Long alumnoId);

    /**
     * Tarea programada: Actualizar facturas vencidas.
     * Se ejecuta diariamente a las 00:00.
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Argentina/Buenos_Aires")
    @Transactional
    public void actualizarFacturasVencidas();

    /**
     * Lista todas las facturas pendientes o parciales del sistema.
     * Solo para administradores.
     */
    public List<Factura> listarTodasLasPendientes();

    /**
     * Cancela una factura.
     */
    @Transactional
    public void cancelarFactura(Long facturaId, String motivo);
}