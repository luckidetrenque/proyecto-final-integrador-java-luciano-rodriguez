package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.*;
import com.escueladeequitacion.hrs.service.FinanzasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
@RestController
@RequestMapping("/api/v1/finanzas")
public class FinanzasController {

    @Autowired
    private FinanzasService finanzasService;

    /**
     * GET /api/v1/finanzas/resumen?inicio=yyyy-MM-dd&fin=yyyy-MM-dd
     * Devuelve el resumen financiero completo del período.
     */
    @GetMapping("/resumen")
    public ResponseEntity<ResumenFinancieroDto> getResumen(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(value = "instructorId", required = false) Long instructorId) {
        return ResponseEntity.ok(finanzasService.calcularResumen(inicio, fin, instructorId));
    }

    /**
     * GET /api/v1/finanzas/alumnos?inicio=&fin=
     * Detalle de cuotas por alumno (clases tomadas vs. contratadas, monto).
     */
    @GetMapping("/alumnos")
    public ResponseEntity<CuotasAlumnosDto> getCuotasAlumnos(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(value = "instructorId", required = false) Long instructorId) {
        return ResponseEntity.ok(finanzasService.calcularCuotasAlumnos(inicio, fin, instructorId));
    }

    /**
     * GET /api/v1/finanzas/pensiones
     * Lista de alumnos con pensión activa y su monto mensual.
     */
    @GetMapping("/pensiones")
    public ResponseEntity<PensionesDto> getPensiones() {
        return ResponseEntity.ok(finanzasService.calcularPensiones());
    }

    /**
     * GET /api/v1/finanzas/honorarios?inicio=&fin=
     * Honorarios proyectados por instructor (clases dictadas × tarifa).
     */
    @GetMapping("/honorarios")
    public ResponseEntity<HonorariosDto> getHonorarios(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(value = "instructorId", required = false) Long instructorId) {
        return ResponseEntity.ok(finanzasService.calcularHonorarios(inicio, fin, instructorId));
    }

    /**
     * GET /api/v1/finanzas/configuracion
     * Devuelve la configuración de precios actual.
     */
    @GetMapping("/configuracion")
    public ResponseEntity<ConfiguracionPreciosDto> getConfiguracion() {
        return ResponseEntity.ok(finanzasService.getConfiguracion());
    }

    /**
     * PUT /api/v1/finanzas/configuracion
     * Actualiza la configuración de precios.
     */
    @PutMapping("/configuracion")
    public ResponseEntity<ConfiguracionPreciosDto> updateConfiguracion(
            @RequestBody ConfiguracionPreciosDto config) {
        return ResponseEntity.ok(finanzasService.updateConfiguracion(config));
    }
}