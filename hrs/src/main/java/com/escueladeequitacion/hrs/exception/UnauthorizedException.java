package com.escueladeequitacion.hrs.exception;

/**
 * Excepción personalizada para errores de autenticación y autorización.
 * Ejemplo: "Usuario no encontrado", "Credenciales inválidas", "Token expirado"
 * HTTP Status: 401 UNAUTHORIZED
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }

    public UnauthorizedException(String recurso, String campo, Object valor) {
        super(String.format("%s no autorizado con %s: '%s'", recurso, campo, valor));
    }
}