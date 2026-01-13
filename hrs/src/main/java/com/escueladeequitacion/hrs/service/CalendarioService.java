package com.escueladeequitacion.hrs.service;

import java.time.LocalDate;

// Interfaz para el servicio de calendario
public interface CalendarioService {

    // Método para copiar una semana completa de clases
    public void copiarSemanaCompleta(LocalDate inicioOri, LocalDate inicioDes);
}