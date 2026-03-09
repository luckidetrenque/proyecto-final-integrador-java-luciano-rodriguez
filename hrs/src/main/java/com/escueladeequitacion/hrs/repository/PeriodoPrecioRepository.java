package com.escueladeequitacion.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.escueladeequitacion.hrs.model.PeriodoPrecio;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PeriodoPrecioRepository extends JpaRepository<PeriodoPrecio, Long> {

    @Query("SELECT p FROM PeriodoPrecio p WHERE :fecha BETWEEN p.fechaInicio AND p.fechaFin AND p.activo = true")
    Optional<PeriodoPrecio> findByFechaVigente(@Param("fecha") LocalDate fecha);

    Optional<PeriodoPrecio> findByNombreAndActivoTrue(String nombre);
}
