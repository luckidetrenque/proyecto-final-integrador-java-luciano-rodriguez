package com.escueladeequitacion.hrs.enums;

public enum ModalidadClase {
    SESION("Sesión"),
    SESIONES_30("Sesiones 30'"),
    SESIONES_SABADOS_30("Sesiones Sábados 30'"),
    SEMANA("Semana"),
    SABADOS("Sábados"),
    SEMANA_SABADOS("Semana/Sábados");

    private String descripcion;

    ModalidadClase(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}