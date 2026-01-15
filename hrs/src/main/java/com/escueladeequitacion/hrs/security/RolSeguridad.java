package com.escueladeequitacion.hrs.security;

/**
 * Roles para el sistema de autenticación (independiente de la lógica de negocio).
 */
public enum RolSeguridad {
    ROLE_ADMIN,      // Gestiona todo el sistema
    ROLE_INSTRUCTOR, // Gestiona sus clases
    ROLE_ALUMNO      // Ve sus propias clases
}