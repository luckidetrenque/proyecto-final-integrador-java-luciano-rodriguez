package com.escueladeequitacion.hrs.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.escueladeequitacion.hrs.enums.Especialidad;
import com.escueladeequitacion.hrs.enums.Estado;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

/**
 * Clase que representa a una Clase en la escuela de equitación.
 */
@Entity
@Table(name = "clases", indexes = {
        @Index(name = "idx_clase_dia_hora", columnList = "dia, hora"),
        @Index(name = "idx_clase_instructor_dia", columnList = "instructor_id, dia"),
        @Index(name = "idx_clase_alumno_dia", columnList = "alumno_id, dia"),
        @Index(name = "idx_clase_caballo_dia", columnList = "caballo_id, dia"),
        @Index(name = "idx_clase_estado", columnList = "estado"),
        @Index(name = "idx_clase_especialidad", columnList = "especialidad")
})
public class Clase {
    // Atributos específicos de Clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "especialidad", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    @Column(name = "dia", nullable = false)
    private LocalDate dia;
    @Column(name = "hora", nullable = false)
    private LocalTime hora;
    @Column(name = "estado", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Column(name = "observaciones", nullable = true, length = 50)
    private String observaciones;
    @Column(name = "es_prueba", nullable = false)
    private Boolean esPrueba = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "caballo_id", nullable = false)
    private Caballo caballo;

    // Constructor vacío
    public Clase() {
    }

    // Constructor con parámetros
    public Clase(Instructor instructor, Alumno alumno, Caballo caballo, Especialidad especialidad,
            LocalDate dia, LocalTime hora, Estado estado, Boolean esPrueba) {
        this.instructor = instructor;
        this.alumno = alumno;
        this.caballo = caballo;
        this.especialidad = especialidad;
        this.dia = dia;
        this.hora = hora;
        this.estado = estado;
        this.esPrueba = esPrueba != null ? esPrueba : false;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Caballo getCaballo() {
        return caballo;
    }

    public void setCaballo(Caballo caballo) {
        this.caballo = caballo;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean isEsPrueba() {
        return esPrueba;
    }

    public void setEsPrueba(Boolean esPrueba) {
        this.esPrueba = esPrueba;
    }

    /**
     * Retorna la fecha y hora combinadas en formato ISO-8601 con zona UTC.
     * Ejemplo: "2026-03-15T10:00:00Z"
     * 
     * Este método NO se persiste en la BD (es solo para serialización JSON).
     * Jackson lo incluirá automáticamente en las respuestas JSON.
     * 
     * @return OffsetDateTime en UTC, o null si dia u hora son null
     */
    public OffsetDateTime getDiaHoraCompleto() {
        if (dia == null || hora == null) {
            return null;
        }
        return OffsetDateTime.of(dia, hora, ZoneOffset.UTC);
    }

}