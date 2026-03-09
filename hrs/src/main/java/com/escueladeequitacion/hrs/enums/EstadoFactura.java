package com.escueladeequitacion.hrs.enums;

public enum EstadoFactura {
    PENDIENTE("Pendiente"),
    PAGADA("Pagada"),
    PARCIALMENTE_PAGADA("Parcialmente Pagada"),
    VENCIDA("Vencida"),
    CANCELADA("Cancelada");

    private String descripcion;

    EstadoFactura(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}