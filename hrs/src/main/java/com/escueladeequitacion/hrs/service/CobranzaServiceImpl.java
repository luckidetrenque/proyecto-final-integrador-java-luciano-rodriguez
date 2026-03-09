package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Factura;
import com.escueladeequitacion.hrs.model.Pago;
import com.escueladeequitacion.hrs.enums.EstadoFactura;
import com.escueladeequitacion.hrs.dto.RegistrarPagoRequestDto;
import com.escueladeequitacion.hrs.repository.FacturaRepository;
import com.escueladeequitacion.hrs.repository.PagoRepository;
import com.escueladeequitacion.hrs.model.Alumno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CobranzaServiceImpl implements CobranzaService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    /**
     * Registra un pago para una factura.
     * Actualiza el saldo pendiente y estado de la factura.
     */
    @Transactional
    public Pago registrarPago(RegistrarPagoRequestDto request, String usuarioRegistrador) {

        // 1. Obtener factura
        Factura factura = facturaRepository.findById(request.getFacturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Factura", "ID", request.getFacturaId()));

        // 2. Validar estado de factura
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new BusinessException("La factura ya está completamente pagada");
        }

        if (factura.getEstado() == EstadoFactura.CANCELADA) {
            throw new BusinessException("No se puede registrar un pago en una factura cancelada");
        }

        // 3. Validar monto
        if (request.getMonto().compareTo(factura.getSaldoPendiente()) > 0) {
            throw new BusinessException(
                    String.format("El monto del pago ($%s) excede el saldo pendiente ($%s)",
                            request.getMonto(), factura.getSaldoPendiente()));
        }

        // 4. Crear pago
        Alumno alumno = factura.getAlumno();
        Pago pago = new Pago(
                factura,
                alumno,
                request.getFechaPago(),
                request.getMonto(),
                request.getFormaPago(),
                request.getNumeroComprobante());
        pago.setObservaciones(request.getObservaciones());
        pago.setRegistradoPor(usuarioRegistrador);

        // 5. Actualizar saldo de factura
        BigDecimal nuevoSaldo = factura.getSaldoPendiente().subtract(request.getMonto());
        factura.setSaldoPendiente(nuevoSaldo);

        // 6. Actualizar estado de factura
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
            factura.setEstado(EstadoFactura.PAGADA);
            factura.setFechaPago(request.getFechaPago());
        } else {
            factura.setEstado(EstadoFactura.PARCIALMENTE_PAGADA);
        }

        // 7. Guardar
        pagoRepository.save(pago);
        facturaRepository.save(factura);

        return pago;
    }

    /**
     * Lista pagos de una factura.
     */
    public List<Pago> listarPagosPorFactura(Long facturaId) {
        return pagoRepository.findByFacturaId(facturaId);
    }

    /**
     * Lista pagos de un alumno.
     */
    public List<Pago> listarPagosPorAlumno(Long alumnoId) {
        return pagoRepository.findByAlumnoId(alumnoId);
    }

    /**
     * Calcula total cobrado en un período.
     */
    public BigDecimal calcularTotalCobradoEnPeriodo(LocalDate desde, LocalDate hasta) {
        BigDecimal total = pagoRepository.sumMontoByFechaPagoBetween(desde, hasta);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Lista pagos en un rango de fechas.
     */
    public List<Pago> listarPagosEnPeriodo(LocalDate desde, LocalDate hasta) {
        return pagoRepository.findByFechaPagoBetween(desde, hasta);
    }
}
