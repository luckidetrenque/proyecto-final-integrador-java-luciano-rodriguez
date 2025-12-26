package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.escueladeequitacion.hrs.enums.Especialidades;
import com.escueladeequitacion.hrs.enums.Estado;

// import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// DTO para representar una clase
public class ClaseDto {

    private Especialidades especialidades;
    @NotNull(message = "El día no puede estar vacío")
    // @FutureOrPresent(message = "El día debe ser en el futuro")
    private LocalDate dia;
    @NotNull(message = "La hora no puede estar vacía")
    // @FutureOrPresent(message = "La hora debe ser en el futuro")
    private LocalTime hora;
    @NotNull(message = "El estado de la clase no puede estar vacío, debe ser Programada, En curso, Completada o Cancelada")
    private Estado estado;
    private String observaciones;
    @NotNull(message = "El instructorId no puede estar vacío")
    @Min(value = 1, message = "El instructorId debe ser un número positivo")
    private Long instructorId;
    @NotNull(message = "El alumnoId no puede estar vacío")
    @Min(value = 1, message = "El alumnoId debe ser un número positivo")
    private Long alumnoId;
    @NotNull(message = "El caballoId no puede estar vacío")
    @Min(value = 1, message = "El caballoId debe ser un número positivo")
    private Long caballoId;

    // Constructores, getters y setters
    public ClaseDto() {

    }

    public ClaseDto(Especialidades especialidades, LocalDate dia, LocalTime hora, Estado estado,
            String observaciones, Long instructorId, Long alumnoId, Long caballoId) {
        this.especialidades = especialidades;
        this.dia = dia;
        this.hora = hora;
        this.estado = estado;
        this.observaciones = observaciones;
        this.instructorId = instructorId;
        this.alumnoId = alumnoId;
        this.caballoId = caballoId;
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

        public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Long getCaballoId() {
        return caballoId;
    }

    public void setCaballoId(Long caballoId) {
        this.caballoId = caballoId;
    }
}
