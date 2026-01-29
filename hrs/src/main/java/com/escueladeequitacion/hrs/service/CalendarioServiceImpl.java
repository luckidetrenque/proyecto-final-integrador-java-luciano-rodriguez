package com.escueladeequitacion.hrs.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.exception.ValidationException;
import com.escueladeequitacion.hrs.model.Clase;
import com.escueladeequitacion.hrs.repository.ClaseRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CalendarioServiceImpl implements CalendarioService {

    @Autowired
    private ClaseRepository claseRepository;

    @Transactional
    public void copiarClases(LocalDate inicioOri, LocalDate inicioDes, int cantidadSemanas) {

        for (int i = 0; i < cantidadSemanas; i++) {
            // Calculamos el desfase para cada iteración (0 días, 7 días, 14 días...)
            long offsetDias = i * 7L;
            LocalDate semanaOriActual = inicioOri.plusDays(offsetDias);
            LocalDate semanaDesActual = inicioDes.plusDays(offsetDias);

            // Reutilizamos la lógica de copiar una semana (refactorizada)
            ejecutarCopiaSemana(semanaOriActual, semanaDesActual);
        }
    }

    private void ejecutarCopiaSemana(LocalDate inicioOri, LocalDate inicioDes) {
        LocalDate finOri = inicioOri.plusDays(6);
        LocalDate finDes = inicioDes.plusDays(6);

        // 1. Borrar solo lo programado en la semana destino actual
        claseRepository.deleteByDiaBetweenAndEstado(inicioDes, finDes, Estado.PROGRAMADA);
        claseRepository.flush();

        // 2. Obtener origen y lo que sobrevivió en destino
        List<Clase> clasesOrigen = claseRepository.findByDiaBetween(inicioOri, finOri);
        List<Clase> destinoRestante = claseRepository.findByDiaBetween(inicioDes, finDes);

        if (clasesOrigen.isEmpty())
            return;

        long diferenciaEntreSemanas = ChronoUnit.DAYS.between(inicioOri, inicioDes);

        List<Clase> nuevasClases = clasesOrigen.stream()
                .map(claseOri -> {
                    LocalDate nuevaFecha = claseOri.getDia().plusDays(diferenciaEntreSemanas);

                    // Evitar duplicar si ya existe una clase (ej. COMPLETADA)
                    boolean existe = destinoRestante.stream()
                            .anyMatch(d -> d.getDia().equals(nuevaFecha) && d.getHora().equals(claseOri.getHora()) &&
                                    d.getAlumno().equals(claseOri.getAlumno()));

                    if (existe)
                        return null;

                    return clonarEntidad(claseOri, nuevaFecha);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        claseRepository.saveAll(nuevasClases);
    }

    private Clase clonarEntidad(Clase origen, LocalDate nuevaFecha) {
        Clase nueva = new Clase();
        BeanUtils.copyProperties(origen, nueva, "id");
        nueva.setDia(nuevaFecha);
        nueva.setEstado(Estado.PROGRAMADA);
        nueva.setObservaciones("");
        return nueva;
    }

    @Transactional
    public void eliminarClases(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new ValidationException("fechas", "Las fechas de origen y destino no pueden estar vacías");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new ValidationException("fechaInicio", "La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        // Solo eliminamos lo que aún no ha sucedido o no se ha procesado
        claseRepository.deleteByDiaBetweenAndEstado(fechaInicio, fechaFin, Estado.PROGRAMADA);
    }
}
