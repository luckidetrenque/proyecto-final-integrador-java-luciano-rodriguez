package com.escueladeequitacion.hrs.dto;

/**
 * Configuración de precios del sistema.
 * Permite al administrador ajustar todos los valores monetarios
 * sin tocar código.
 */
public class ConfiguracionPreciosDto {

    // ── Cuotas por plan de clases ────────────────────────────
    private Double cuota4Clases = 0.0; // $ plan 4 clases/mes
    private Double cuota8Clases = 0.0; // $ plan 8 clases/mes
    private Double cuota12Clases = 0.0; // $ plan 12 clases/mes
    private Double cuota16Clases = 0.0; // $ plan 16 clases/mes

    // ── Pensiones ────────────────────────────────────────────
    private Double pensionEntera = 0.0;
    private Double pensionMedia = 0.0;
    private Double pensionTercio = 0.0;

    // ── Reserva de caballo de escuela ────────────────────────
    private Double reservaEscuela = 0.0;

    // ── Honorarios instructores ──────────────────────────────
    private Double honorarioPorClase = 0.0; // $ por clase dictada
    private Double honorarioBaseMensual = 0.0; // $ fijo mensual

    // ── Constructores ────────────────────────────────────────
    public ConfiguracionPreciosDto() {
    }

    // ── Getters & Setters ─────────────────────────────────────
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