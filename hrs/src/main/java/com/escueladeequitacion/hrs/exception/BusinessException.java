package com.escueladeequitacion.hrs.exception;

/**
 * Excepción personalizada para violaciones de reglas de negocio.
 * Ejemplo: "El instructor no está activo", "El caballo no está disponible"
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String mensaje) {
        super(mensaje);
    }
}