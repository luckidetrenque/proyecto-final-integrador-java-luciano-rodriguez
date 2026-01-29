package com.escueladeequitacion.hrs.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ConversionAPlanRequest {

    @NotNull(message = "La cantidad de clases es obligatoria")
    @Pattern(regexp = "^(4|8|12|16)$", message = "La cantidad de clases debe ser 4, 8, 12 o 16")
    private Integer cantidadClases;

    // Constructor vacío
    public ConversionAPlanRequest() {
    }

    // Constructor con parámetros
    public ConversionAPlanRequest(Integer cantidadClases) {
        this.cantidadClases = cantidadClases;
    }

    // Getters y Setters
    public Integer getCantidadClases() {
        return cantidadClases;
    }

    public void setCantidadClases(Integer cantidadClases) {
        this.cantidadClases = cantidadClases;
    }
}