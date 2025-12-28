package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

public class CalendarioDto {
    private LocalDate fechaInicioOrigen; // El martes de la semana que quieres copiar
    private LocalDate fechaInicioDestino;
    public LocalDate getFechaInicioOrigen() {
        return fechaInicioOrigen;
    }
    public void setFechaInicioOrigen(LocalDate fechaInicioOrigen) {
        this.fechaInicioOrigen = fechaInicioOrigen;
    }
    public LocalDate getFechaInicioDestino() {
        return fechaInicioDestino;
    }
    public void setFechaInicioDestino(LocalDate fechaInicioDestino) {
        this.fechaInicioDestino = fechaInicioDestino;
    } // El martes de la semana donde quieres pegar

}