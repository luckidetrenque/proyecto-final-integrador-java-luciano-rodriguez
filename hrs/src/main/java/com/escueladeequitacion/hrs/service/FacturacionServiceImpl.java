package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Abono;
import com.escueladeequitacion.hrs.model.Factura;
import com.escueladeequitacion.hrs.model.ItemFactura;
import com.escueladeequitacion.hrs.enums.EstadoFactura;
import com.escueladeequitacion.hrs.enums.TipoItem;
import com.escueladeequitacion.hrs.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FacturacionServiceImpl implements FacturacionService {

    @Autowired
    private FacturaRepository facturaRepository;

    /**
     * Genera factura mensual automáticamente para un abono.
     * Se llama desde el AbonoService al crear el abono.
     */
    @Transactional
    public Factura generarFacturaMensual(Abono abono, LocalDate fechaEmision) {

        Factura factura = new Factura();
        factura.setNumeroFactura(generarNumeroFactura());
        factura.setAlumno(abono.getAlumno());
        factura.setAbono(abono);
        factura.setFechaEmision(fechaEmision);
        factura.setFechaVencimiento(calcularFechaVencimiento(fechaEmision));
        factura.setEstado(EstadoFactura.PENDIENTE);

        // Items de la factura

        // Item 1: Abono de clases
        ItemFactura itemClases = new ItemFactura(
                TipoItem.ABONO_CLASES,
                abono.getPlanAbono().getDescripcion(),
                1,
                abono.getPrecioContratadoClases());
        factura.agregarItem(itemClases);

        // Item 2: Pensión (si aplica)
        if (abono.getPlanPension() != null && abono.getPrecioContratadoPension() != null) {
            ItemFactura itemPension = new ItemFactura(
                    TipoItem.PENSION,
                    "Pensión de caballo - " + abono.getCaballo().getNombre(),
                    1,
                    abono.getPrecioContratadoPension());
            factura.agregarItem(itemPension);
        }

        // Item 3: Cuota socio (si aplica)
        if (abono.getCuotaSocioContratada() != null) {
            ItemFactura itemSocio = new ItemFactura(
                    TipoItem.CUOTA_SOCIO,
                    "Cuota de socio",
                    1,
                    abono.getCuotaSocioContratada());
            factura.agregarItem(itemSocio);
        }

        // Calcular totales
        factura.recalcularSubtotal();

        return facturaRepository.save(factura);
    }

    /**
     * Aplica recargo por mora si la factura está vencida.
     * Se ejecuta automáticamente por tarea programada.
     */
    @Transactional
    public void aplicarRecargoMora(Factura factura) {
        if (factura.getEstado() != EstadoFactura.VENCIDA &&
                factura.getEstado() != EstadoFactura.PENDIENTE) {
            return;
        }

        if (!factura.estaVencida()) {
            return;
        }

        long diasVencidos = ChronoUnit.DAYS.between(factura.getFechaVencimiento(), LocalDate.now());

        if (diasVencidos > 0) {
            // Recargo del 3% por cada 7 días de atraso (ajusta según tu política)
            int periodos = (int) Math.ceil(diasVencidos / 7.0);
            BigDecimal porcentajeRecargo = BigDecimal.valueOf(0.03 * periodos);
            BigDecimal recargo = factura.getSubtotal().multiply(porcentajeRecargo);

            factura.setRecargos(recargo);
            factura.setTotal(factura.calcularTotal());
            factura.setSaldoPendiente(factura.getTotal());
            factura.setEstado(EstadoFactura.VENCIDA);

            facturaRepository.save(factura);
        }
    }

    /**
     * Genera el número de factura siguiente.
     * Formato: FC-YYYY-NNNNN
     */
    private String generarNumeroFactura() {
        int anio = LocalDate.now().getYear();
        String patron = "FC-" + anio + "-%";

        String ultimoNumero = facturaRepository.findUltimoNumeroFactura(patron).orElse(null);

        int siguiente = 1;
        if (ultimoNumero != null) {
            // Extraer el número del formato FC-2025-00123
            String[] partes = ultimoNumero.split("-");
            if (partes.length == 3) {
                siguiente = Integer.parseInt(partes[2]) + 1;
            }
        }

        return String.format("FC-%d-%05d", anio, siguiente);
    }

    /**
     * Calcula la fecha de vencimiento (10 días después de la emisión).
     */
    private LocalDate calcularFechaVencimiento(LocalDate fechaEmision) {
        return fechaEmision.plusDays(10);
    }

    /**
     * Lista facturas de un alumno.
     */
    public List<Factura> listarFacturasPorAlumno(Long alumnoId) {
        return facturaRepository.findByAlumnoId(alumnoId);
    }

    /**
     * Lista facturas pendientes de un alumno.
     */
    public List<Factura> listarFacturasPendientesPorAlumno(Long alumnoId) {
        return facturaRepository.findFacturasPendientesByAlumno(
                alumnoId,
                List.of(EstadoFactura.PENDIENTE, EstadoFactura.PARCIALMENTE_PAGADA, EstadoFactura.VENCIDA));
    }

    /**
     * Tarea programada: Actualizar facturas vencidas.
     * Se ejecuta diariamente a las 00:00.
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Argentina/Buenos_Aires")
    @Transactional
    public void actualizarFacturasVencidas() {
        List<Factura> facturasVencidas = facturaRepository.findFacturasVencidas(LocalDate.now());

        for (Factura factura : facturasVencidas) {
            aplicarRecargoMora(factura);
        }
    }

    /**
     * Lista todas las facturas pendientes o parciales del sistema.
     * Solo para administradores.
     */
    public List<Factura> listarTodasLasPendientes() {
        return facturaRepository.findByEstadoIn(
                List.of(EstadoFactura.PENDIENTE, EstadoFactura.PARCIALMENTE_PAGADA, EstadoFactura.VENCIDA));
    }

    /**
     * Cancela una factura.
     */
    @Transactional
    public void cancelarFactura(Long facturaId, String motivo) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Factura", "ID", facturaId));

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new BusinessException("No se puede cancelar una factura pagada");
        }

        factura.setEstado(EstadoFactura.CANCELADA);
        factura.setObservaciones(motivo);
        facturaRepository.save(factura);
    }
}