package com.escueladeequitacion.hrs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "planes_pension", indexes = {
        @Index(name = "idx_plan_pension_cantidad", columnList = "cantidad_clases"),
        @Index(name = "idx_plan_pension_vigencia", columnList = "fecha_vigencia_desde, fecha_vigencia_hasta"),
        @Index(name = "idx_plan_pension_activo", columnList = "activo")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "periodoVigencia"})
public class PlanPension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad_clases", nullable = false)
    private Integer cantidadClases; // 8, 12, 16

    @Column(name = "descripcion", length = 100)
    private String descripcion; // "8 Clases de Martes a Sábados"

    // Precios según forma de pago y fecha
    @Column(name = "precio_efectivo_1al15", precision = 10, scale = 2)
    private BigDecimal precioEfectivo1al15;

    @Column(name = "precio_transferencia_1al15", precision = 10, scale = 2)
    private BigDecimal precioTransferencia1al15;

    @Column(name = "precio_efectivo_despues15", precision = 10, scale = 2)
    private BigDecimal precioEfectivoDespues15;

    @Column(name = "precio_transferencia_despues15", precision = 10, scale = 2)
    private BigDecimal precioTransferenciaDespues15;

    // Vigencia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_precio_id")
    private PeriodoPrecio periodoVigencia;

    @Column(name = "fecha_vigencia_desde")
    private LocalDate fechaVigenciaDesde;

    @Column(name = "fecha_vigencia_hasta")
    private LocalDate fechaVigenciaHasta;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // Constructores
    public PlanPension() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadClases() {
        return cantidadClases;
    }

    public void setCantidadClases(Integer cantidadClases) {
        this.cantidadClases = cantidadClases;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioEfectivo1al15() {
        return precioEfectivo1al15;
    }

    public void setPrecioEfectivo1al15(BigDecimal precioEfectivo1al15) {
        this.precioEfectivo1al15 = precioEfectivo1al15;
    }

    public BigDecimal getPrecioTransferencia1al15() {
        return precioTransferencia1al15;
    }

    public void setPrecioTransferencia1al15(BigDecimal precioTransferencia1al15) {
        this.precioTransferencia1al15 = precioTransferencia1al15;
    }

    public BigDecimal getPrecioEfectivoDespues15() {
        return precioEfectivoDespues15;
    }

    public void setPrecioEfectivoDespues15(BigDecimal precioEfectivoDespues15) {
        this.precioEfectivoDespues15 = precioEfectivoDespues15;
    }

    public BigDecimal getPrecioTransferenciaDespues15() {
        return precioTransferenciaDespues15;
    }

    public void setPrecioTransferenciaDespues15(BigDecimal precioTransferenciaDespues15) {
        this.precioTransferenciaDespues15 = precioTransferenciaDespues15;
    }

    public PeriodoPrecio getPeriodoVigencia() {
        return periodoVigencia;
    }

    public void setPeriodoVigencia(PeriodoPrecio periodoVigencia) {
        this.periodoVigencia = periodoVigencia;
    }

    public LocalDate getFechaVigenciaDesde() {
        return fechaVigenciaDesde;
    }

    public void setFechaVigenciaDesde(LocalDate fechaVigenciaDesde) {
        this.fechaVigenciaDesde = fechaVigenciaDesde;
    }

    public LocalDate getFechaVigenciaHasta() {
        return fechaVigenciaHasta;
    }

    public void setFechaVigenciaHasta(LocalDate fechaVigenciaHasta) {
        this.fechaVigenciaHasta = fechaVigenciaHasta;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}