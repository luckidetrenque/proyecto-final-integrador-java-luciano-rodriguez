package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.PlanAbono;
import com.escueladeequitacion.hrs.enums.ModalidadClase;
import com.escueladeequitacion.hrs.enums.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanAbonoRepository extends JpaRepository<PlanAbono, Long> {

    /**
     * Busca un plan específico por tipo, modalidad, cantidad y que esté vigente.
     */
    @Query("SELECT p FROM PlanAbono p WHERE " +
            "p.tipoClase = :tipo AND " +
            "p.modalidad = :modalidad AND " +
            "p.cantidadClases = :cantidad AND " +
            "p.activo = true AND " +
            ":fecha BETWEEN p.fechaVigenciaDesde AND p.fechaVigenciaHasta")
    Optional<PlanAbono> findPlanVigente(
            @Param("tipo") TipoClase tipo,
            @Param("modalidad") ModalidadClase modalidad,
            @Param("cantidad") Integer cantidad,
            @Param("fecha") LocalDate fecha);

    /**
     * Lista todos los planes vigentes en una fecha.
     */
    @Query("SELECT p FROM PlanAbono p WHERE " +
            "p.activo = true AND " +
            ":fecha BETWEEN p.fechaVigenciaDesde AND p.fechaVigenciaHasta " +
            "ORDER BY p.tipoClase, p.cantidadClases")
    List<PlanAbono> findAllVigentes(@Param("fecha") LocalDate fecha);

    /**
     * Lista planes por tipo de clase.
     */
    List<PlanAbono> findByTipoClaseAndActivoTrue(TipoClase tipoClase);
}
