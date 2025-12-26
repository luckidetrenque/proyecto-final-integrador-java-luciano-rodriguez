package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

// DTO para representar un alumno
public class AlumnoDto extends PersonaDto {

    @NotNull(message = "La fecha de inscripción no puede estar vacía")
    @PastOrPresent(message = "La fecha de inscripción no puede ser en el futuro")
    private LocalDate fechaInscripcion;
    @NotNull(message = "La cantidad de clases no puede estar vacía")
    private Integer cantidadClases;
    @NotNull(message = "El estado del alumno no puede estar vacío")
    private Boolean activo;
    @NotNull(message = "El estado del alumno no puede estar vacío")
    private Boolean propietario;

    // Constructores, getters y setters
    public AlumnoDto() {
        super();
    }

    public AlumnoDto(Integer dni, String nombre, String apellido, LocalDate fechaNacimiento, Integer telefono,
            String email,
            LocalDate fechaInscripcion, Integer cantidadClases, Boolean activo, Boolean propietario) {
        super(dni, nombre, apellido, fechaNacimiento, telefono, email);
        this.fechaInscripcion = fechaInscripcion;
        this.cantidadClases = cantidadClases;
        this.activo = activo;
        this.propietario = propietario;
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
}
