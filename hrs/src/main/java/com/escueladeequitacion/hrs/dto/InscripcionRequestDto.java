package com.escueladeequitacion.hrs.dto;

import com.escueladeequitacion.hrs.enums.TipoPension;
import com.escueladeequitacion.hrs.enums.FormaPago;
import com.escueladeequitacion.hrs.enums.ModalidadClase;
import com.escueladeequitacion.hrs.enums.TipoClase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class InscripcionRequestDto {

    @NotNull(message = "El alumno es obligatorio")
    private Long alumnoId;

    private Integer edad; // Se puede calcular desde el alumno

    @NotNull(message = "Debe indicar si es socio")
    private Boolean esSocio;

    // Plan de clases
    private TipoClase tipoClase; // Si es null, se determina automáticamente por edad

    @NotNull(message = "La modalidad es obligatoria")
    private ModalidadClase modalidad;

    @NotNull(message = "La cantidad de clases es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidadClases;

    // Caballo y pensión
    @NotNull(message = "El tipo de pensión es obligatorio")
    private TipoPension tipoPension;

    private Long caballoId; // Obligatorio si tipoPension != SIN_CABALLO

    // Pago
    @NotNull(message = "La forma de pago es obligatoria")
    private FormaPago formaPago;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;

    // Constructores
    public InscripcionRequestDto() {
    }

    // Getters y Setters
    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Boolean getEsSocio() {
        return esSocio;
    }

    public void setEsSocio(Boolean esSocio) {
        this.esSocio = esSocio;
    }

    public TipoClase getTipoClase() {
        return tipoClase;
    }

    public void setTipoClase(TipoClase tipoClase) {
        this.tipoClase = tipoClase;
    }

    public ModalidadClase getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadClase modalidad) {
        this.modalidad = modalidad;
    }

    public Integer getCantidadClases() {
        return cantidadClases;
    }

    public void setCantidadClases(Integer cantidadClases) {
        this.cantidadClases = cantidadClases;
    }

    public TipoPension getTipoPension() {
        return tipoPension;
    }

    public void setTipoPension(TipoPension tipoPension) {
        this.tipoPension = tipoPension;
    }

    public Long getCaballoId() {
        return caballoId;
    }

    public void setCaballoId(Long caballoId) {
        this.caballoId = caballoId;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }
}
