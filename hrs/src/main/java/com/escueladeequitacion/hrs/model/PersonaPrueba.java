package com.escueladeequitacion.hrs.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

/**
 * Clase de prueba para representar una persona con atributos básicos.
 * Esta clase se utiliza para pruebas y no tiene relación directa con la lógica
 * de negocio.
 */
@Entity
@Table(name = "personas_prueba", indexes = {
        @Index(name = "idx_persona_prueba_nombre", columnList = "nombre, apellido")
})
public class PersonaPrueba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro = LocalDate.now();

    // Constructor vacío
    public PersonaPrueba() {
    }

    public PersonaPrueba(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaRegistro = LocalDate.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}