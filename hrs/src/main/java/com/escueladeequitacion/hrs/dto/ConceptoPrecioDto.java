package com.escueladeequitacion.hrs.dto;

import java.math.BigDecimal;

public class ConceptoPrecioDto {
    private String concepto;
    private BigDecimal monto;

    public ConceptoPrecioDto() {
    }

    public ConceptoPrecioDto(String concepto, BigDecimal monto) {
        this.concepto = concepto;
        this.monto = monto;
    }

    // Getters y Setters
    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}