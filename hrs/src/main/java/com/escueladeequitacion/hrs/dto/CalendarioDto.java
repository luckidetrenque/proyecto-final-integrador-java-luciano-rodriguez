package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class CalendarioDto {
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaInicioOrigen; // El martes de la semana que quieres copiar
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaInicioDestino;

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
    } // El martes de la semana donde quieres pegar

}