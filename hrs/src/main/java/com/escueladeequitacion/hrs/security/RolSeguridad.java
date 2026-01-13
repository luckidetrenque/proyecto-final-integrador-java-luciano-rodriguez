package com.escueladeequitacion.hrs.security;

/**
 * Roles para el sistema de autenticación (independiente de la lógica de negocio).
 */
public enum RolSeguridad {
    ADMIN,      // Gestiona todo el sistema
    INSTRUCTOR, // Gestiona sus clases
    ALUMNO      // Ve sus propias clases
}