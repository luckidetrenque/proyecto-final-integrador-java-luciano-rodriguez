package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.PlanPension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanPensionRepository extends JpaRepository<PlanPension, Long> {

    /**
     * Busca un plan de pensión vigente por cantidad de clases.
     */
    @Query("SELECT p FROM PlanPension p WHERE " +
            "p.cantidadClases = :cantidad AND " +
            "p.activo = true AND " +
            ":fecha BETWEEN p.fechaVigenciaDesde AND p.fechaVigenciaHasta")
    Optional<PlanPension> findPlanVigente(
            @Param("cantidad") Integer cantidad,
            @Param("fecha") LocalDate fecha);

    /**
     * Lista todos los planes de pensión vigentes.
     */
    @Query("SELECT p FROM PlanPension p WHERE " +
            "p.activo = true AND " +
            ":fecha BETWEEN p.fechaVigenciaDesde AND p.fechaVigenciaHasta " +
            "ORDER BY p.cantidadClases")
    List<PlanPension> findAllVigentes(@Param("fecha") LocalDate fecha);
}