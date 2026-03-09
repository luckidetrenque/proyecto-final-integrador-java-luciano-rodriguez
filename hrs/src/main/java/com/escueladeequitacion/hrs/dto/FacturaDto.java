package com.escueladeequitacion.hrs.dto;

import com.escueladeequitacion.hrs.enums.EstadoFactura;
import com.escueladeequitacion.hrs.enums.FormaPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FacturaDto {
    private Long id;
    private String numeroFactura;
    private Long alumnoId;
    private String alumnoNombre;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private BigDecimal subtotal;
    private BigDecimal descuentos;
    private BigDecimal recargos;
    private BigDecimal total;
    private BigDecimal saldoPendiente;
    private EstadoFactura estado;
    private FormaPago formaPago;
    private List<ItemFacturaDto> items;
    private String observaciones;

    // Constructor, Getters y Set

    // Getters y Setters c
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getAlumnoNombre() {
        return alumnoNombre;
    }

    public void setAlumnoNombre(String alumnoNombre) {
        this.alumnoNombre = alumnoNombre;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(BigDecimal descuentos) {
        this.descuentos = descuentos;
    }

    public BigDecimal getRecargos() {
        return recargos;
    }

    public void setRecargos(BigDecimal recargos) {
        this.recargos = recargos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(BigDecimal saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public List<ItemFacturaDto> getItems() {
        return items;
    }

    public void setItems(List<ItemFacturaDto> items) {
        this.items = items;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}