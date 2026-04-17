package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.model.Abono;
import com.escueladeequitacion.hrs.dto.InscripcionRequestDto;
import com.escueladeequitacion.hrs.dto.PrecioCalculadoDto;
import com.escueladeequitacion.hrs.service.AbonoService;
import com.escueladeequitacion.hrs.service.PrecioService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/abonos")
public class AbonoController {

    @Autowired
    private AbonoService abonoService;

    @Autowired
    private PrecioService precioService;

    /**
     * POST /api/v1/finanzas/abonos/calcular-precio
     * Calcula el precio de una inscripción SIN crear el abono.
     * Útil para mostrar al usuario cuánto va a pagar antes de confirmar.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN') or @claseSecurityService.esElMismoAlumno(#request.alumnoId, authentication)")
    @PostMapping("/calcular-precio")
    public ResponseEntity<PrecioCalculadoDto> calcularPrecio(@Valid @RequestBody InscripcionRequestDto request) {
        PrecioCalculadoDto precio = precioService.calcularPrecioInscripcion(request);
        return ResponseEntity.ok(precio);
    }

    /**
     * POST /api/v1/finanzas/abonos
     * Crea un nuevo abono (inscripción completa).
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PostMapping
    public ResponseEntity<?> crearAbono(@Valid @RequestBody InscripcionRequestDto request) {
        Abono abono = abonoService.crearAbonoDesdeInscripcion(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Abono creado correctamente con ID: " + abono.getId()));
    }

    /**
     * GET /api/v1/finanzas/abonos/alumno/{alumnoId}/activo
     * Obtiene el abono activo de un alumno.
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}/activo")
    public ResponseEntity<?> obtenerAbonoActivo(@PathVariable Long alumnoId) {
        Optional<Abono> abono = abonoService.obtenerAbonoActivo(alumnoId);

        if (abono.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("El alumno no tiene un abono activo"));
        }

        return ResponseEntity.ok(abono.get());
    }

    /**
     * GET /api/v1/finanzas/abonos/alumno/{alumnoId}
     * Lista todos los abonos de un alumno (historial).
     */
    @PreAuthorize("@claseSecurityService.esElMismoAlumno(#alumnoId, authentication)")
    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<Abono>> listarAbonosPorAlumno(@PathVariable Long alumnoId) {
        List<Abono> abonos = abonoService.listarAbonosPorAlumno(alumnoId);
        return ResponseEntity.ok(abonos);
    }

    /**
     * PUT /api/v1/finanzas/abonos/{id}/cancelar
     * Cancela un abono.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Mensaje> cancelarAbono(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo) {

        abonoService.cancelarAbono(id, motivo);
        return ResponseEntity.ok(new Mensaje("Abono cancelado correctamente"));
    }

    /**
     * PUT /api/v1/finanzas/abonos/{id}/pausar
     * Pausa un abono temporalmente.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PutMapping("/{id}/pausar")
    public ResponseEntity<Mensaje> pausarAbono(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo) {

        abonoService.pausarAbono(id, motivo);
        return ResponseEntity.ok(new Mensaje("Abono pausado correctamente"));
    }

    /**
     * PUT /api/v1/finanzas/abonos/{id}/reactivar
     * Reactiva un abono pausado.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<Mensaje> reactivarAbono(@PathVariable Long id) {
        abonoService.reactivarAbono(id);
        return ResponseEntity.ok(new Mensaje("Abono reactivado correctamente"));
    }

    /**
     * GET /api/v1/abonos
     * Lista todos los abonos del sistema con filtros opcionales.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @GetMapping
    public ResponseEntity<List<Abono>> listarAbonos(
            @RequestParam(required = false) com.escueladeequitacion.hrs.enums.EstadoAbono estado) {
        
        List<Abono> abonos;
        if (estado != null) {
            abonos = abonoService.listarAbonosPorEstado(estado);
        } else {
            // Por defecto traemos los activos para la gestión diaria
            abonos = abonoService.listarAbonosPorEstado(com.escueladeequitacion.hrs.enums.EstadoAbono.ACTIVO);
        }
        return ResponseEntity.ok(abonos);
    }
}

