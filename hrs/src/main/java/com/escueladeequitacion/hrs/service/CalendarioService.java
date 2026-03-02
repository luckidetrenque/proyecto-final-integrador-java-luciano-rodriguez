package com.escueladeequitacion.hrs.service;

import java.time.LocalDate;
import java.time.LocalTime;

// Interfaz para el servicio de calendario
public interface CalendarioService {

    // Método para copiar las clases de un período a otro
    public void copiarClases(LocalDate inicioOri, LocalDate inicioDes, int cantidadSemanas);

    // Método para eliminar clases en un periodo
    public void eliminarClases(LocalDate fechaInicio, LocalDate inicioDes, LocalTime horaInicio, LocalTime horaFin);
}