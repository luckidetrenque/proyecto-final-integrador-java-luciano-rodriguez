package com.escueladeequitacion.hrs.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.escueladeequitacion.hrs.enums.Rol;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

/**
 * Clase que representa a un Alumno en la escuela de equitación.
 * Hereda de la clase Persona e incluye atributos específicos de un alumno.
 */
@Entity
@Table(name = "alumnos", indexes = {
        @Index(name = "idx_alumno_dni", columnList = "dni", unique = true),
        @Index(name = "idx_alumno_nombre_apellido", columnList = "nombre, apellido"),
        @Index(name = "idx_alumno_activo", columnList = "activo"),
        @Index(name = "idx_alumno_fecha_inscripcion", columnList = "fecha_inscripcion")
})
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "caballo_id", nullable = true)
    private Caballo caballoPropio;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Clase> clases = new ArrayList<>();

    // Constructor vacío
    public Alumno() {
        super();
    }

    // Constructor con parámetros
    public Alumno(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
            String email,
            LocalDate fechaInscripcion, Integer cantidadClases, Boolean activo, Boolean propietario) {
        super(dni, nombre, apellido, fechaNacimiento, telefono, email);
        this.fechaInscripcion = fechaInscripcion;
        this.cantidadClases = cantidadClases != null ? cantidadClases : 0;
        this.activo = activo != null ? activo : false; // false para clases de prueba
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

    public Caballo getCaballoPropio() {
        return caballoPropio;
    }

    public void setCaballoPropio(Caballo caballoPropio) {
        this.caballoPropio = caballoPropio;
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
    public double calcularPago() {// Cuota base mensual
        double cuotaBase = 1000.0;

        // Cálculo proporcional según cantidad de clases
        // 4 clases = 1x, 8 clases = 1.5x, 12 clases = 2x, 16 clases = 2.5x
        double multiplicador = switch (cantidadClases) {
            case 4 -> 1.0;
            case 8 -> 1.5;
            case 12 -> 2.0;
            case 16 -> 2.5;
            default -> 1.0;
        };

        // Si tiene caballo propio, se cobra mantenimiento adicional
        double cargoMantenimiento = propietario ? 500.0 : 0.0;

        return (cuotaBase * multiplicador) + cargoMantenimiento;
    }

}