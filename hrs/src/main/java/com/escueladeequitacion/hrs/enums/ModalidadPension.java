package com.escueladeequitacion.hrs.enums;

public enum ModalidadPension {
    MARTES_A_SABADOS("Martes a Sábados");

    private String descripcion;

    ModalidadPension(String string) {
        this.descripcion = string;
    }

    public String getDescripcion() {
        return descripcion;
    }

}