package com.escueladeequitacion.hrs.exception;

/**
 * Excepción personalizada para conflictos de recursos duplicados.
 * Ejemplo: "Ya existe un alumno con el DNI 12345678"
 * HTTP Status: 409 CONFLICT
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String mensaje) {
        super(mensaje);
    }

    public ConflictException(String recurso, String campo, Object valor) {
        super(String.format("Ya existe %s con %s: '%s'", recurso, campo, valor));
    }
}