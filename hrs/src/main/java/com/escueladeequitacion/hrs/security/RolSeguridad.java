package com.escueladeequitacion.hrs.security;

/**
 * Roles para el sistema de autenticación (independiente de la lógica de
 * negocio).
 */
public enum RolSeguridad {
    SUPERADMIN,    // Gestiona todo el sistema: coordinadores, cupos y configuración global
    COORDINADOR,   // Antes ADMIN: gestiona instructores y alumnos (no puede crear otros coordinadores)
    INSTRUCTOR,    // Gestiona sus propias clases
    ALUMNO         // Ve sus propias clases
}