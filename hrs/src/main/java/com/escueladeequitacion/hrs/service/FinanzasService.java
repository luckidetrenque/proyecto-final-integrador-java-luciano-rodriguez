package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.*;
import java.time.LocalDate;

public interface FinanzasService {
    ResumenFinancieroDto calcularResumen(LocalDate inicio, LocalDate fin, Long instructorId);

    CuotasAlumnosDto calcularCuotasAlumnos(LocalDate inicio, LocalDate fin, Long instructorId);

    PensionesDto calcularPensiones();

    HonorariosDto calcularHonorarios(LocalDate inicio, LocalDate fin, Long instructorId);

    ConfiguracionPreciosDto getConfiguracion();

    ConfiguracionPreciosDto updateConfiguracion(ConfiguracionPreciosDto dto);
}