package com.escueladeequitacion.hrs.dto;

import com.escueladeequitacion.hrs.model.PlanAbono;
import com.escueladeequitacion.hrs.model.PlanPension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrecioCalculadoDto {
    private List<ConceptoPrecioDto> conceptos;
    private BigDecimal total;
    private PlanAbono planClases;
    private PlanPension planPension;
    private String mensajeAdicional;

    public PrecioCalculadoDto() {
        this.conceptos = new ArrayList<>();
    }

    public PrecioCalculadoDto(List<ConceptoPrecioDto> conceptos, BigDecimal total,
            PlanAbono planClases, PlanPension planPension) {
        this.conceptos = conceptos;
        this.total = total;
        this.planClases = planClases;
        this.planPension = planPension;
    }

    // Getters y Setters
    public List<ConceptoPrecioDto> getConceptos() {
        return conceptos;
    }

    public void setConceptos(List<ConceptoPrecioDto> conceptos) {
        this.conceptos = conceptos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public PlanAbono getPlanClases() {
        return planClases;
    }

    public void setPlanClases(PlanAbono planClases) {
        this.planClases = planClases;
    }

    public PlanPension getPlanPension() {
        return planPension;
    }

    public void setPlanPension(PlanPension planPension) {
        this.planPension = planPension;
    }

    public String getMensajeAdicional() {
        return mensajeAdicional;
    }

    public void setMensajeAdicional(String mensajeAdicional) {
        this.mensajeAdicional = mensajeAdicional;
    }
}