package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// DTO para representar una persona
public class PersonaDto {

    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(min = 7, max = 9, message = "El DNI debe tener entre 7 y 9 dígitos")
    @Pattern(regexp = "^[0-9]+$", message = "El DNI solo debe contener números")
    private String dni;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;
    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;
    @NotBlank(message = "El codigo de area no puede estar vacío")
    @Size(min = 2, max = 8, message = "El codigo de area debe tener entre 2 y 8 caracteres")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "El codigo de area solo puede contener números y opcionalmente el prefijo '+'")
    @Column(name = "CodigoArea", length = 8, nullable = false)
    private String codigoArea;
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(min = 5, max = 8, message = "El teléfono debe tener entre 5 y 8 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El teléfono solo puede contener números")
    @Column(name = "telefono", length = 8, nullable = false)
    private String telefono;
    @Email(message = "El email debe tener un formato válido")
    private String email;

    // Constructores, getters y setters
    public PersonaDto() {
    }

    public PersonaDto(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String codigoArea,
            String telefono,
            String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.codigoArea = codigoArea;
        this.telefono = telefono;
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
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

    public String getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(String codigoArea) {
        this.codigoArea = codigoArea;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
