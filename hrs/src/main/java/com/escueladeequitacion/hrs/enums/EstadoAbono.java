package com.escueladeequitacion.hrs.enums;

public enum EstadoAbono {
    ACTIVO("Activo"),
    VENCIDO("Vencido"),
    CANCELADO("Cancelado"),
    PAUSADO("Pausado"),
    FINALIZADO("Finalizado");

    private String descripcion;

    EstadoAbono(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}