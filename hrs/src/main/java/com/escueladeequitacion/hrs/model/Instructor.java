package com.escueladeequitacion.hrs.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.escueladeequitacion.hrs.enums.Rol;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase que representa a un Instructor en la escuela de equitación.
 * Hereda de la clase Persona e incluye atributos específicos de un instructor.
 */
@Entity
@Table(name = "instructores")
public class Instructor extends Persona {
    // Atributos específicos de Instructor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "activo", nullable = false)
    private Boolean activo; // Para indicar si el alumno está activo o inactivo

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Clase> clases = new ArrayList<>();

    // Constructor vacío
    public Instructor() {
        super();
    }

    // Constructor con parámetros
    public Instructor(Integer dni, String nombre, String apellido, LocalDate fechaNacimiento, Integer telefono, String email,
            Boolean activo) {
        super(dni, nombre, apellido, fechaNacimiento, telefono, email);
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // Métodos para la lista de clases
    public void agregarClase(Clase clase) {
        clases.add(clase);
        clase.setInstructor(this);
    }
    
    public void removerClase(Clase clase) {
        clases.remove(clase);
        clase.setInstructor(null);
    }

    // Métodos sobrescritos de Persona
    @Override
    public Rol getRol() {
        return Rol.INSTRUCTOR;
    }

    @Override
    public double calcularPago() {
        return 100; // Verificar con el equipo cómo se calcula el pago de un instructor
    }
}