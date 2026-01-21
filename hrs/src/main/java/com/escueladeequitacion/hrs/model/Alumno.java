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
 * Clase que representa a un Alumno en la escuela de equitación.
 * Hereda de la clase Persona e incluye atributos específicos de un alumno.
 */
@Entity
@Table(name = "alumnos")
public class Alumno extends Persona {
    // Atributos específicos de Alumno
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;
    @Column(name = "cantidad_clases", nullable = false)
    private Integer cantidadClases;
    @Column(name = "activo", nullable = false)
    private Boolean activo; // Para indicar si el alumno está activo o inactivo
    @Column(name = "propietario", nullable = false)
    private Boolean propietario; // Para indicar si el alumno tiene caballo propio o no

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Clase> clases = new ArrayList<>();

    // Constructor vacío
    public Alumno() {
        super();
    }

    // Constructor con parámetros
    public Alumno(Integer dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
            String email,
            LocalDate fechaInscripcion, Integer cantidadClases, Boolean activo, Boolean propietario) {
        super(dni, nombre, apellido, fechaNacimiento, telefono, email);
        this.fechaInscripcion = fechaInscripcion;
        this.cantidadClases = cantidadClases;
        this.activo = true;
        this.propietario = propietario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getCantidadClases() {
        return cantidadClases;
    }

    public void setCantidadClases(Integer cantidadClases) {
        this.cantidadClases = cantidadClases;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean isPropietario() {
        return propietario;
    }

    public void setPropietario(Boolean propietario) {
        this.propietario = propietario;
    }

    // Métodos para la lista de clases
    public void agregarClase(Clase clase) {
        clases.add(clase);
        clase.setAlumno(this);
    }

    public void removerClase(Clase clase) {
        clases.remove(clase);
        clase.setAlumno(null);
    }

    // Métodos sobrescritos de Persona
    @Override
    public Rol getRol() {
        return Rol.ALUMNO;
    }

    @Override
    public double calcularPago() {
        throw new UnsupportedOperationException("Unimplemented method 'calcularPago'");
    };

    // @Override
    // public double calcularPago() {
    // double cuotaBase = 1000.0; // Pago base mensual
    // if (caballo != null) { // Si tiene caballo propio
    // cuotaBase += 500.0; // Cargo adicional por mantenimiento
    // }
    // return cuotaBase;
    // };

}