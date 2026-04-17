package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.model.Factura;
import com.escueladeequitacion.hrs.service.FacturacionService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

    @Autowired
    private FacturacionService facturacionService;

    /**
     * GET /api/v1/facturas/pendientes
     * Lista todas las facturas pendientes o parciales del sistema.
     * Solo ADMIN.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @GetMapping("/pendientes")
    public ResponseEntity<List<Factura>> listarTodasLasPendientes() {
        List<Factura> facturas = facturacionService.listarTodasLasPendientes();
        return ResponseEntity.ok(facturas);
    }

    /**
     * GET /api/v1/finanzas/facturas/alumno/{alumnoId}
     * Lista todas las facturas de un alumno.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<Factura>> listarFacturasPorAlumno(@PathVariable Long alumnoId) {
        List<Factura> facturas = facturacionService.listarFacturasPorAlumno(alumnoId);
        return ResponseEntity.ok(facturas);
    }

    /**
     * GET /api/v1/finanzas/facturas/alumno/{alumnoId}/pendientes
     * Lista facturas pendientes de un alumno.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/pendientes")
    public ResponseEntity<List<Factura>> listarFacturasPendientes(@PathVariable Long alumnoId) {
        List<Factura> facturas = facturacionService.listarFacturasPendientesPorAlumno(alumnoId);
        return ResponseEntity.ok(facturas);
    }

    /**
     * PUT /api/v1/finanzas/facturas/{id}/cancelar
     * Cancela una factura.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Mensaje> cancelarFactura(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo) {

        facturacionService.cancelarFactura(id, motivo);
        return ResponseEntity.ok(new Mensaje("Factura cancelada correctamente"));
    }
}