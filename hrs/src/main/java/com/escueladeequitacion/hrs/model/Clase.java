package com.escueladeequitacion.hrs.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.escueladeequitacion.hrs.enums.Especialidades;
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

/**
 * Clase que representa a una Clase en la escuela de equitación.
 */
@Entity
@Table(name = "clases")
public class Clase {
    // Atributos específicos de Clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "especialidades", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private Especialidades especialidades;
    @Column(name = "DIA", nullable = false)
    private LocalDate dia;
    @Column(name = "hora", nullable = false)
    private LocalTime hora;
    @Column(name = "estado", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Column(name = "observaciones", nullable = true, length = 50)
    private String observaciones;

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
    public Clase(Instructor instructor, Alumno alumno, Caballo caballo, Especialidades especialidades,
            LocalDate dia, LocalTime hora, Estado estado) {
        this.instructor = instructor;
        this.alumno = alumno;
        this.caballo = caballo;
        this.especialidades = especialidades;
        this.dia = dia;
        this.hora = hora;
        this.estado = estado;
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

    public Especialidades getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Especialidades especialidades) {
        this.especialidades = especialidades;
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

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}