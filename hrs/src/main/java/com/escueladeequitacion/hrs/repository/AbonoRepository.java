package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.Abono;
import com.escueladeequitacion.hrs.enums.EstadoAbono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long> {

    /**
     * Busca el abono activo de un alumno.
     * Un alumno solo puede tener UN abono activo a la vez.
     */
    @Query("SELECT a FROM Abono a WHERE a.alumno.id = :alumnoId AND a.estado = 'ACTIVO'")
    Optional<Abono> findAbonoActivoByAlumnoId(@Param("alumnoId") Long alumnoId);

    /**
     * Lista todos los abonos de un alumno (historial).
     */
    @Query("SELECT a FROM Abono a WHERE a.alumno.id = :alumnoId ORDER BY a.fechaContratacion DESC")
    List<Abono> findAllByAlumnoId(@Param("alumnoId") Long alumnoId);

    /**
     * Lista abonos por estado.
     */
    List<Abono> findByEstado(EstadoAbono estado);

    /**
     * Lista abonos que vencen en un rango de fechas.
     */
    @Query("SELECT a FROM Abono a WHERE a.fechaVencimiento BETWEEN :desde AND :hasta AND a.estado = 'ACTIVO'")
    List<Abono> findAbonosPorVencer(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    /**
     * Lista abonos vencidos que aún están marcados como ACTIVO (para tarea
     * programada).
     */
    @Query("SELECT a FROM Abono a WHERE a.fechaVencimiento < :hoy AND a.estado = 'ACTIVO'")
    List<Abono> findAbonosVencidos(@Param("hoy") LocalDate hoy);

    /**
     * Cuenta abonos activos.
     */
    long countByEstado(EstadoAbono estado);
}