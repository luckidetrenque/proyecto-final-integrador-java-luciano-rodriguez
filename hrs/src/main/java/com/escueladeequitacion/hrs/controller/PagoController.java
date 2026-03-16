package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.model.Pago;
import com.escueladeequitacion.hrs.dto.RegistrarPagoRequestDto;
import com.escueladeequitacion.hrs.service.CobranzaService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/finanzas/pagos")
public class PagoController {

    @Autowired
    private CobranzaService cobranzaService;

    /**
     * POST /api/v1/finanzas/pagos
     * Registra un nuevo pago.
     */
    @PostMapping
    public ResponseEntity<?> registrarPago(
            @Valid @RequestBody RegistrarPagoRequestDto request,
            Principal principal) {

        String usuario = principal != null ? principal.getName() : "Sistema";
        Pago pago = cobranzaService.registrarPago(request, usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Pago registrado correctamente con ID: " + pago.getId()));
    }

    /**
     * GET /api/v1/finanzas/pagos/factura/{facturaId}
     * Lista pagos de una factura.
     */
    @GetMapping("/factura/{facturaId}")
    public ResponseEntity<List<Pago>> listarPagosPorFactura(@PathVariable Long facturaId) {
        List<Pago> pagos = cobranzaService.listarPagosPorFactura(facturaId);
        return ResponseEntity.ok(pagos);
    }

    /**
     * GET /api/v1/finanzas/pagos/alumno/{alumnoId}
     * Lista pagos de un alumno.
     */
    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<Pago>> listarPagosPorAlumno(@PathVariable Long alumnoId) {
        List<Pago> pagos = cobranzaService.listarPagosPorAlumno(alumnoId);
        return ResponseEntity.ok(pagos);
    }

    /**
     * GET /api/v1/finanzas/pagos/periodo
     * Calcula total cobrado en un período.
     */
    @GetMapping("/periodo")
    public ResponseEntity<Map<String, Object>> calcularTotalCobrado(
            @RequestParam("desde") LocalDate desde,
            @RequestParam("hasta") LocalDate hasta) {

        BigDecimal total = cobranzaService.calcularTotalCobradoEnPeriodo(desde, hasta);
        List<Pago> pagos = cobranzaService.listarPagosEnPeriodo(desde, hasta);

        return ResponseEntity.ok(Map.of(
                "desde", desde,
                "hasta", hasta,
                "totalCobrado", total,
                "cantidadPagos", pagos.size(),
                "pagos", pagos));
    }
}