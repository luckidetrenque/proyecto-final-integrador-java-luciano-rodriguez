package com.escueladeequitacion.hrs.model;

import jakarta.persistence.*;

/**
 * Tabla con una única fila (id = 1) que almacena todos los precios
 * configurables del sistema. El administrador los edita desde el
 * módulo de Finanzas; el backend los lee para calcular los reportes.
 */
@Entity
@Table(name = "configuracion_precios")
public class ConfiguracionPrecios {

    @Id
    @Column(name = "id")
    private Long id = 1L; // Siempre 1 — singleton

    // ── Cuotas ────────────────────────────────────────────────
    @Column(name = "cuota_4_clases", nullable = false)
    private Double cuota4Clases = 0.0;

    @Column(name = "cuota_8_clases", nullable = false)
    private Double cuota8Clases = 0.0;

    @Column(name = "cuota_12_clases", nullable = false)
    private Double cuota12Clases = 0.0;

    @Column(name = "cuota_16_clases", nullable = false)
    private Double cuota16Clases = 0.0;

    // ── Pensiones ─────────────────────────────────────────────
    @Column(name = "pension_entera", nullable = false)
    private Double pensionEntera = 0.0;

    @Column(name = "pension_media", nullable = false)
    private Double pensionMedia = 0.0;

    @Column(name = "pension_tercio", nullable = false)
    private Double pensionTercio = 0.0;

    // ── Reserva caballo de escuela ────────────────────────────
    @Column(name = "reserva_escuela", nullable = false)
    private Double reservaEscuela = 0.0;

    // ── Honorarios ────────────────────────────────────────────
    @Column(name = "honorario_por_clase", nullable = false)
    private Double honorarioPorClase = 0.0;

    @Column(name = "honorario_base_mensual", nullable = false)
    private Double honorarioBaseMensual = 0.0;

    // ── Constructores ─────────────────────────────────────────
    public ConfiguracionPrecios() {
    }

    // ── Getters & Setters ─────────────────────────────────────
    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        this.id = v;
    }

    public Double getCuota4Clases() {
        return cuota4Clases;
    }

    public void setCuota4Clases(Double v) {
        this.cuota4Clases = v;
    }

    public Double getCuota8Clases() {
        return cuota8Clases;
    }

    public void setCuota8Clases(Double v) {
        this.cuota8Clases = v;
    }

    public Double getCuota12Clases() {
        return cuota12Clases;
    }

    public void setCuota12Clases(Double v) {
        this.cuota12Clases = v;
    }

    public Double getCuota16Clases() {
        return cuota16Clases;
    }

    public void setCuota16Clases(Double v) {
        this.cuota16Clases = v;
    }

    public Double getPensionEntera() {
        return pensionEntera;
    }

    public void setPensionEntera(Double v) {
        this.pensionEntera = v;
    }

    public Double getPensionMedia() {
        return pensionMedia;
    }

    public void setPensionMedia(Double v) {
        this.pensionMedia = v;
    }

    public Double getPensionTercio() {
        return pensionTercio;
    }

    public void setPensionTercio(Double v) {
        this.pensionTercio = v;
    }

    public Double getReservaEscuela() {
        return reservaEscuela;
    }

    public void setReservaEscuela(Double v) {
        this.reservaEscuela = v;
    }

    public Double getHonorarioPorClase() {
        return honorarioPorClase;
    }

    public void setHonorarioPorClase(Double v) {
        this.honorarioPorClase = v;
    }

    public Double getHonorarioBaseMensual() {
        return honorarioBaseMensual;
    }

    public void setHonorarioBaseMensual(Double v) {
        this.honorarioBaseMensual = v;
    }
}