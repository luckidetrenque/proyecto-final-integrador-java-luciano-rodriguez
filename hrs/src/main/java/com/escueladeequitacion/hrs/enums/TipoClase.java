package com.escueladeequitacion.hrs.enums;

public enum TipoClase {
    EQUINOTERAPIA("Equinoterapia"),
    ESCUELA_MENOR_6("Escuela (menor a 6 años)"),
    ESCUELA_MAYOR_6("Escuela (6 años en adelante)"),
    PENSIONADOS_PRIVADOS("Pensionados / Privados"),
    OTROS("Otros");

    private String descripcion;

    TipoClase(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
