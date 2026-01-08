package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

// DTO para representar una persona
public class PersonaDto {

    @NotNull(message = "El DNI no puede estar vacío")
    @Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
    @Max(value = 999999999, message = "El DNI no debe exceder los 9 dígitos")
    private Integer dni;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;
    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;
    @NotNull(message = "El teléfono no puede estar vacío")
    @Min(value = 1000000, message = "El teléfono debe tener al menos 7 dígitos")
    @Max(value = 999999999, message = "El teléfono no debe exceder los 10 dígitos")
    private Integer telefono;
    @Email(message = "El email debe tener un formato válido")
    private String email;

    // Constructores, getters y setters
    public PersonaDto() {
    }

    public PersonaDto(Integer dni, String nombre, String apellido, LocalDate fechaNacimiento, Integer telefono,
            String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
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

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
