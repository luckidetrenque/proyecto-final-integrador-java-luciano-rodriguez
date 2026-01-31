package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

// DTO para representar un instructor
public class InstructorDto extends PersonaDto {

    @NotNull(message = "El estado del instructor no puede estar vacío")
    private Boolean activo;
    @NotNull(message = "El color del instructor no puede estar vacío")
    private String color;

    // Constructores, getters y setters
    public InstructorDto() {
        super();
    }

    public InstructorDto(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
            String email,
            Boolean activo, String color) {
        super(dni, nombre, apellido, fechaNacimiento, telefono, email);
        this.activo = activo;
        this.color = color;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
