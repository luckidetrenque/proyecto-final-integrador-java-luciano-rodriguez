package com.escueladeequitacion.hrs.enums;

public enum FormaPago {
    EFECTIVO("Efectivo"),
    TRANSFERENCIA("Transferencia"),
    TARJETA_DEBITO("Tarjeta de Débito"),
    TARJETA_CREDITO("Tarjeta de Crédito"),
    MERCADO_PAGO("Mercado Pago");

    private String descripcion;

    FormaPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}