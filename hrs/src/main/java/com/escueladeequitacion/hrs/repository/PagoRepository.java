package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    /**
     * Lista pagos de una factura.
     */
    List<Pago> findByFacturaId(Long facturaId);

    /**
     * Lista pagos de un alumno.
     */
    @Query("SELECT p FROM Pago p WHERE p.alumno.id = :alumnoId ORDER BY p.fechaPago DESC")
    List<Pago> findByAlumnoId(@Param("alumnoId") Long alumnoId);

    /**
     * Lista pagos en un rango de fechas.
     */
    @Query("SELECT p FROM Pago p WHERE p.fechaPago BETWEEN :desde AND :hasta ORDER BY p.fechaPago")
    List<Pago> findByFechaPagoBetween(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    /**
     * Suma total de pagos en un rango de fechas.
     */
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fechaPago BETWEEN :desde AND :hasta")
    java.math.BigDecimal sumMontoByFechaPagoBetween(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}