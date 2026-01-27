package com.escueladeequitacion.hrs.model;

import java.time.LocalDate;
import java.time.Period;

import com.escueladeequitacion.hrs.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Clase abstracta que representa a una Persona en la escuela de equitación.
 * Contiene atributos y métodos comunes a todas las personas (alumnos e
 * instructores).
 */
@MappedSuperclass
public abstract class Persona {

    // Atributos comunes a todas las personas
    @Column(name = "dni", nullable = false, unique = true, length = 15)
    private String dni;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "apellido", nullable = false)
    private String apellido;
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    @Column(name = "telefono", nullable = false, length = 13)
    private String telefono;
    @Column(name = "email", nullable = true)
    private String email;

    // Constructor vacío
    public Persona() {
    }

    // Constructor con parámetros, excepto el email que es opcional
    public Persona(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
            String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y Setters
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Método para retornar nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    // Método para calcular la edad a partir de la fecha de nacimiento
    public Integer getEdad() {
        LocalDate fechaActual = LocalDate.now();
        Period periodo = Period.between(fechaNacimiento, fechaActual);
        int edad = periodo.getYears();
        return edad;
    }

    // Métodos abstractos que deben implementar las subclases Alumno e Instructor
    public abstract Rol getRol();

    public abstract double calcularPago();

}
