package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.escueladeequitacion.hrs.enums.Especialidad;
import com.escueladeequitacion.hrs.enums.Estado;

// import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// DTO para representar una clase
public class ClaseDto {

    // Interfaces para validación condicional
    public interface AlCrear {
    }

    public interface AlActualizar {
    }

    private Especialidad especialidad;
    @NotNull(message = "El día no puede estar vacío")
    // @FutureOrPresent(message = "El día debe ser en el futuro", groups =
    // AlCrear.class)
    // @FutureOrPresent(message = "El día debe ser en el futuro")
    private LocalDate dia;
    @NotNull(message = "La hora no puede estar vacía")
    // @FutureOrPresent(message = "La hora debe ser en el futuro")
    private LocalTime hora;
    @NotNull(message = "La duración no puede estar vacía")
    @Min(value = 30, message = "La duración mínima es 30 minutos")
    private Integer duracion = 30;
    @NotNull(message = "El estado de la clase no puede estar vacío, debe ser Programada, Iniciada, Completada o Cancelada")
    private Estado estado;
    private String observaciones;

    private Long instructorId;
    private Long personaPruebaId;
    private Long alumnoId;
    @NotNull(message = "El caballoId no puede estar vacío")
    @Min(value = 1, message = "El caballoId debe ser un número positivo")
    private Long caballoId;
    private Boolean esPrueba;

    // Constructores, getters y setters
    public ClaseDto() {

    }

    public ClaseDto(Especialidad especialidad, LocalDate dia, LocalTime hora, Integer duracion, Estado estado,
            String observaciones, Long instructorId, Long alumnoId, Long caballoId, Boolean esPrueba) {
        this.especialidad = especialidad;
        this.dia = dia;
        this.hora = hora;
        this.duracion = duracion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.instructorId = instructorId;
        this.alumnoId = alumnoId;
        this.caballoId = caballoId;
        this.esPrueba = esPrueba;
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

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
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

    public Boolean isEsPrueba() {
        return esPrueba;
    }

    public void setEsPrueba(Boolean esPrueba) {
        this.esPrueba = esPrueba;
    }

    public Long getPersonaPruebaId() {
        return personaPruebaId;
    }

    public void setPersonaPruebaId(Long personaPruebaId) {
        this.personaPruebaId = personaPruebaId;
    }

    /**
     * Retorna la fecha y hora combinadas en formato ISO-8601 con zona UTC.
     * Ejemplo: "2026-03-15T10:00:00Z"
     * 
     * @return OffsetDateTime en UTC, o null si dia u hora son null
     */
    public OffsetDateTime getDiaHoraCompleto() {
        if (dia == null || hora == null) {
            return null;
        }
        // Combinar LocalDate + LocalTime y convertir a UTC
        return OffsetDateTime.of(dia, hora, ZoneOffset.UTC);
    }

}
