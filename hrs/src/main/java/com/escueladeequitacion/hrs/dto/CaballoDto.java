package com.escueladeequitacion.hrs.dto;

import com.escueladeequitacion.hrs.enums.TipoCaballo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO para representar un caballo
public class CaballoDto {

    @NotBlank(message = "El nombre del caballo no puede estar vacío")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    private String nombre;
    @NotNull(message = "El estado del caballo no puede estar vacío")
    private Boolean disponible;
    @NotNull(message = "El tipo de caballo no puede estar vacío")
    private TipoCaballo tipoCaballo;

    // Constructores, getters y setters
    public CaballoDto() {

    }

    public CaballoDto(String nombre, Boolean disponible, TipoCaballo tipoCaballo) {
        this.nombre = nombre;
        this.disponible = disponible;
        this.tipoCaballo = tipoCaballo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public TipoCaballo getTipoCaballo() {
        return tipoCaballo;
    }

    public void setTipoCaballo(TipoCaballo tipoCaballo) {
        this.tipoCaballo = tipoCaballo;
    }
}
