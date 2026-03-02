package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaFin;

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

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    @AssertTrue(message = "La fecha de origen y destino no pueden ser iguales")
    public boolean isFechasDiferentes() {
        if (diaInicioOrigen == null || diaInicioDestino == null) {
            return true;
        }
        return !diaInicioOrigen.equals(diaInicioDestino);
    }

    @AssertTrue(message = "La hora de inicio debe ser anterior a la hora de fin")
    public boolean isHorasValidas() {
        if (horaInicio == null || horaFin == null) {
            return true; // Si no se especifican, es válido (día completo)
        }
        return horaInicio.isBefore(horaFin);
    }

}