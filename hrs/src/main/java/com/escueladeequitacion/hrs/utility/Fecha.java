package com.escueladeequitacion.hrs.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Fecha {

    // Definimos los formatos como constantes para mejorar el rendimiento
    private static final DateTimeFormatter FORMATO_ENTRADA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_SALIDA = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * Convierte una fecha de formato dd/MM/yyyy a yyyy/MM/dd
     * 
     * @param fechaStr Fecha en formato texto (ej: "28/12/2025")
     * @return Fecha formateada para búsqueda (ej: "2025/12/28")
     */
    public static String formatearParaDB(String fechaStr) {
        if (fechaStr == null || fechaStr.isEmpty()) {
            return null;
        }
        // 1. Convertir String de entrada a LocalDate
        LocalDate fecha = LocalDate.parse(fechaStr, FORMATO_ENTRADA);

        // 2. Retornar el String con el nuevo formato
        return fecha.format(FORMATO_SALIDA);
    }
}
