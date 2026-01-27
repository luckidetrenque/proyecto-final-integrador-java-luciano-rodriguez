package com.escueladeequitacion.hrs.exception;

/**
 * Excepción personalizada para errores de validación que no son capturados
 * por @Valid.
 * Ejemplo: "La cantidad de clases debe ser 4, 8, 12 o 16"
 * HTTP Status: 400 BAD REQUEST
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String mensaje) {
        super(mensaje);
    }

    public ValidationException(String campo, String mensaje) {
        super(String.format("Error de validación en %s: %s", campo, mensaje));
    }
}