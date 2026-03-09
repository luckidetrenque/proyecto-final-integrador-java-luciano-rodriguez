package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.Factura;
import com.escueladeequitacion.hrs.enums.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    /**
     * Busca factura por número.
     */
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    /**
     * Lista facturas de un alumno.
     */
    @Query("SELECT f FROM Factura f WHERE f.alumno.id = :alumnoId ORDER BY f.fechaEmision DESC")
    List<Factura> findByAlumnoId(@Param("alumnoId") Long alumnoId);

    /**
     * Lista facturas de un abono.
     */
    List<Factura> findByAbonoId(Long abonoId);

    /**
     * Lista facturas por estado.
     */
    List<Factura> findByEstado(EstadoFactura estado);

    /**
     * Lista facturas pendientes de un alumno.
     */
    @Query("SELECT f FROM Factura f WHERE f.alumno.id = :alumnoId AND f.estado IN :estados ORDER BY f.fechaVencimiento")
    List<Factura> findFacturasPendientesByAlumno(
            @Param("alumnoId") Long alumnoId,
            @Param("estados") List<EstadoFactura> estados);

    /**
     * Lista facturas vencidas.
     */
    @Query("SELECT f FROM Factura f WHERE f.fechaVencimiento < :hoy AND f.estado IN ('PENDIENTE', 'PARCIALMENTE_PAGADA')")
    List<Factura> findFacturasVencidas(@Param("hoy") LocalDate hoy);

    /**
     * Cuenta facturas por estado.
     */
    long countByEstado(EstadoFactura estado);

    /**
     * Obtiene el último número de factura para generar el siguiente.
     */
    @Query("SELECT MAX(f.numeroFactura) FROM Factura f WHERE f.numeroFactura LIKE :patron")
    Optional<String> findUltimoNumeroFactura(@Param("patron") String patron);
}