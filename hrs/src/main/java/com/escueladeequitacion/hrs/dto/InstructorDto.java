package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

// DTO para representar un instructor
public class InstructorDto extends PersonaDto {

    @NotNull(message = "El estado del instructor no puede estar vacío")
    private Boolean activo;

    // Constructores, getters y setters
    public InstructorDto() {
        super();
    }

    public InstructorDto(Integer dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
            String email,
            Boolean activo) {
        super(dni, nombre, apellido, fechaNacimiento, telefono, email);
        this.activo = activo;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
