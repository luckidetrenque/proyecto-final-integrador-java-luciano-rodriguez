package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class CalendarioDto {
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaInicioOrigen;
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaInicioDestino;
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

}