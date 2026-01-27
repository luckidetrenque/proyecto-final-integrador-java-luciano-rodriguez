package com.escueladeequitacion.hrs.service;

import java.time.LocalDate;

// Interfaz para el servicio de calendario
public interface CalendarioService {

    // Método para copiar las clases de un período a otro
    public void copiarClases(LocalDate inicioOri, LocalDate inicioDes, int cantidadSemanas);

    // Método para eliminar clases en un periodo
    public void eliminarClases(LocalDate fechaInicio, LocalDate inicioDes);
}