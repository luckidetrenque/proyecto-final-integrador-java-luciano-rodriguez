package com.escueladeequitacion.hrs.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Clase;
import com.escueladeequitacion.hrs.repository.ClaseRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarioServiceImpl implements CalendarioService {

    @Autowired
    private ClaseRepository claseRepository;

    @Transactional
    public void copiarSemanaCompleta(LocalDate inicioOri, LocalDate inicioDes) {
        // 1. Definir rango de la semana origen (5 días)
        LocalDate finOri = inicioOri.plusDays(4);
        List<Clase> clasesExistentes = claseRepository.findByFechaBetween(inicioOri, finOri);

        // 2. Calcular cuántos días de diferencia hay entre semanas
        long diferenciaDias = ChronoUnit.DAYS.between(inicioOri, inicioDes);

        // 3. Clonar registros ajustando la nueva fecha
        List<Clase> nuevasClases = clasesExistentes.stream().map(claseOriginal -> {
            Clase nuevaClase = new Clase();
            // Copia todos los atributos excepto el ID
            BeanUtils.copyProperties(claseOriginal, nuevaClase, "id");
            // Ajustar a la nueva fecha
            nuevaClase.setDia(claseOriginal.getDia().plusDays(diferenciaDias));
            nuevaClase.setEstado(Estado.PROGRAMADA);
            nuevaClase.setObservaciones("");
            return nuevaClase;
        }).collect(Collectors.toList());

        // 4. Guardar todo en un solo proceso (Batch insert)
        if (!nuevasClases.isEmpty()) {
            claseRepository.saveAll(nuevasClases);
        }
    }
}
