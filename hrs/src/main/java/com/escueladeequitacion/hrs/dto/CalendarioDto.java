package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CalendarioDto {
    @NotNull(message = "La fecha de inicio no puede estar vacía")
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaInicioOrigen;
    @NotNull(message = "La fecha de destino no puede estar vacía")
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaInicioDestino;
    @Positive(message = "La cantidad de semanas debe ser mayor a 0")
    private int cantidadSemanas; // Cantidad de semanas a copiar

    public LocalDate getDiaInicioOrigen() {
        return diaInicioOrigen;
    }

    public void setDiaInicioOrigen(LocalDate diaInicioOrigen) {
        this.diaInicioOrigen = diaInicioOrigen;
    }

    public LocalDate getDiaInicioDestino() {
        return diaInicioDestino;
    }

    public void setDiaInicioDestino(LocalDate diaInicioDestino) {
        this.diaInicioDestino = diaInicioDestino;
    }

    public int getCantidadSemanas() {
        return cantidadSemanas;
    }

    public void setCantidadSemanas(int cantidadSemanas) {
        this.cantidadSemanas = cantidadSemanas;
    }

    @AssertTrue(message = "La fecha de origen y destino no pueden ser iguales")
    public boolean isFechasDiferentes() {
        // Verificación de nulos para evitar HV000090 por NullPointerException
        if (diaInicioOrigen == null || diaInicioDestino == null) {
            return true;
        }
        return !diaInicioOrigen.equals(diaInicioDestino);
    }

}