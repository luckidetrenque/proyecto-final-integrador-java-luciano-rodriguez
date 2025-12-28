package com.escueladeequitacion.hrs.utility;

public class Constantes {

    // API Endpoints
    public static final String API_VERSION = "api/v1";
    public static final String RESOURCE_ALUMNOS = "/alumnos";
    public static final String RESOURCE_CABALLOS = "/caballos";
    public static final String RESOURCE_INSTRUCTORES = "/instructores";
    public static final String RESOURCE_CLASES = "/clases";
    public static final String RESOURCE_CALENDARIO = "/calendario";

    // CrossOrigin
    public static final String ORIGINS = "http://localhost:4200";

    // Canttidad de clases disponibles
    public static final Integer[] CANTIDAD_CLASES = { 4, 8, 12, 16 };

    // Nombres de meses y días por mes
    public static final String[] MESES = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre",
            "Octubre", "Noviembre", "Diciembre" };
    public static final int[] DIAS_POR_MES = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    // Para evitar instanciación
    private Constantes() {
    }
}
