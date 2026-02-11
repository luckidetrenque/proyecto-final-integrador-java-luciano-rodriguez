package com.escueladeequitacion.hrs.security;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad User para autenticación (independiente de Persona).
 * Un User representa una cuenta del sistema.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolSeguridad rol;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // Campos opcionales para vincular con Persona (sin relación JPA)
    @Column(name = "persona_dni")
    private Integer personaDni; // DNI de Alumno o Instructor (opcional)

    @Column(name = "persona_tipo", length = 20)
    private String personaTipo; // "ALUMNO" o "INSTRUCTOR" (opcional)

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl; // URL del avatar del usuario

    // Constructores
    public User() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }

    public User(String username, String email, String password, RolSeguridad rol) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolSeguridad getRol() {
        return rol;
    }

    public void setRol(RolSeguridad rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getPersonaDni() {
        return personaDni;
    }

    public void setPersonaDni(Integer personaDni) {
        this.personaDni = personaDni;
    }

    public String getPersonaTipo() {
        return personaTipo;
    }

    public void setPersonaTipo(String personaTipo) {
        this.personaTipo = personaTipo;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}