package com.escueladeequitacion.hrs.enums;

public enum TipoItem {
    ABONO_CLASES("Abono de clases"),
    PENSION("Pensión de caballo"),
    CUOTA_SOCIO("Cuota de socio"),
    CLASE_SUELTA("Clase suelta"),
    RESERVA_CABALLO("Reserva de caballo"),
    RECARGO_MORA("Recargo por mora"),
    DESCUENTO("Descuento");

    private String descripcion;

    TipoItem(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}