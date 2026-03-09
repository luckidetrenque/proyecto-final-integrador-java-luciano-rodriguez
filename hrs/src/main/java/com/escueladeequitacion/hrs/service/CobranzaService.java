package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.model.Pago;
import com.escueladeequitacion.hrs.dto.RegistrarPagoRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public interface CobranzaService {

    /**
     * Registra un pago para una factura.
     * Actualiza el saldo pendiente y estado de la factura.
     */
    @Transactional
    public Pago registrarPago(RegistrarPagoRequestDto request, String usuarioRegistrador);

    /**
     * Lista pagos de una factura.
     */
    public List<Pago> listarPagosPorFactura(Long facturaId);

    /**
     * Lista pagos de un alumno.
     */
    public List<Pago> listarPagosPorAlumno(Long alumnoId);

    /**
     * Calcula total cobrado en un período.
     */
    public BigDecimal calcularTotalCobradoEnPeriodo(LocalDate desde, LocalDate hasta);

    /**
     * Lista pagos en un rango de fechas.
     */
    public List<Pago> listarPagosEnPeriodo(LocalDate desde, LocalDate hasta);
}
