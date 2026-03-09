package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.*;
import java.time.LocalDate;

public interface FinanzasService {
    ResumenFinancieroDto calcularResumen(LocalDate inicio, LocalDate fin);

    CuotasAlumnosDto calcularCuotasAlumnos(LocalDate inicio, LocalDate fin);

    PensionesDto calcularPensiones();

    HonorariosDto calcularHonorarios(LocalDate inicio, LocalDate fin);

    ConfiguracionPreciosDto getConfiguracion();

    ConfiguracionPreciosDto updateConfiguracion(ConfiguracionPreciosDto dto);
}