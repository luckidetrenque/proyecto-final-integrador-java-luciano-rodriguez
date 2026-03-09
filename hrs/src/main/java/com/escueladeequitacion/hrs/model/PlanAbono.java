package com.escueladeequitacion.hrs.model;

import com.escueladeequitacion.hrs.enums.ModalidadClase;
import com.escueladeequitacion.hrs.enums.TipoClase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "planes_abono", indexes = {
        @Index(name = "idx_plan_abono_tipo_modalidad", columnList = "tipo_clase, modalidad"),
        @Index(name = "idx_plan_abono_vigencia", columnList = "fecha_vigencia_desde, fecha_vigencia_hasta"),
        @Index(name = "idx_plan_abono_activo", columnList = "activo")
})
public class PlanAbono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificación del plan
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_clase", nullable = false, length = 30)
    private TipoClase tipoClase;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidad", nullable = false, length = 30)
    private ModalidadClase modalidad;

    @Column(name = "cantidad_clases", nullable = false)
    private Integer cantidadClases;

    @Column(name = "descripcion", length = 100)
    private String descripcion; // "4 SESIONES 30'"

    // Precios según forma de pago y fecha
    @Column(name = "precio_efectivo_1al15", precision = 10, scale = 2)
    private BigDecimal precioEfectivo1al15;

    @Column(name = "precio_transferencia_1al15", precision = 10, scale = 2)
    private BigDecimal precioTransferencia1al15;

    @Column(name = "precio_efectivo_despues15", precision = 10, scale = 2)
    private BigDecimal precioEfectivoDespues15;

    @Column(name = "precio_transferencia_despues15", precision = 10, scale = 2)
    private BigDecimal precioTransferenciaDespues15;

    // Cuota socio (si aplica)
    @Column(name = "cuota_socio", precision = 10, scale = 2)
    private BigDecimal cuotaSocio;

    // Vigencia del precio
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
    public PlanAbono() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoClase getTipoClase() {
        return tipoClase;
    }

    public void setTipoClase(TipoClase tipoClase) {
        this.tipoClase = tipoClase;
    }

    public ModalidadClase getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadClase modalidad) {
        this.modalidad = modalidad;
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

    public BigDecimal getCuotaSocio() {
        return cuotaSocio;
    }

    public void setCuotaSocio(BigDecimal cuotaSocio) {
        this.cuotaSocio = cuotaSocio;
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