package com.escueladeequitacion.hrs.model;

import com.escueladeequitacion.hrs.enums.EstadoAbono;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "abonos", indexes = {
        @Index(name = "idx_abono_alumno", columnList = "alumno_id"),
        @Index(name = "idx_abono_estado", columnList = "estado"),
        @Index(name = "idx_abono_vigencia", columnList = "fecha_vencimiento")
})
public class Abono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_abono_id", nullable = false)
    private PlanAbono planAbono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_pension_id")
    private PlanPension planPension; // Solo si tiene caballo con pensión

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caballo_id")
    private Caballo caballo; // Si tiene caballo asignado (propio o reservado)

    // Datos del abono
    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "clases_totales", nullable = false)
    private Integer clasesTotales;

    @Column(name = "clases_restantes", nullable = false)
    private Integer clasesRestantes;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAbono estado;

    // Precios contratados (histórico - no cambian aunque cambien los planes)
    @Column(name = "precio_contratado_clases", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioContratadoClases;

    @Column(name = "precio_contratado_pension", precision = 10, scale = 2)
    private BigDecimal precioContratadoPension;

    @Column(name = "cuota_socio_contratada", precision = 10, scale = 2)
    private BigDecimal cuotaSocioContratada;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    // Constructor vacío
    public Abono() {
        this.estado = EstadoAbono.ACTIVO;
    }

    // Constructor completo
    public Abono(Alumno alumno, PlanAbono planAbono, PlanPension planPension,
            Caballo caballo, LocalDate fechaContratacion, LocalDate fechaVencimiento,
            Integer clasesTotales, BigDecimal precioContratadoClases,
            BigDecimal precioContratadoPension, BigDecimal cuotaSocioContratada) {
        this.alumno = alumno;
        this.planAbono = planAbono;
        this.planPension = planPension;
        this.caballo = caballo;
        this.fechaContratacion = fechaContratacion;
        this.fechaVencimiento = fechaVencimiento;
        this.clasesTotales = clasesTotales;
        this.clasesRestantes = clasesTotales;
        this.estado = EstadoAbono.ACTIVO;
        this.precioContratadoClases = precioContratadoClases;
        this.precioContratadoPension = precioContratadoPension;
        this.cuotaSocioContratada = cuotaSocioContratada;
    }

    // Métodos de utilidad
    public BigDecimal getPrecioTotal() {
        BigDecimal total = precioContratadoClases != null ? precioContratadoClases : BigDecimal.ZERO;

        if (precioContratadoPension != null) {
            total = total.add(precioContratadoPension);
        }

        if (cuotaSocioContratada != null) {
            total = total.add(cuotaSocioContratada);
        }

        return total;
    }

    public boolean puedeTomarClase() {
        return estado == EstadoAbono.ACTIVO && clasesRestantes > 0;
    }

    public void descontarClase() {
        if (!puedeTomarClase()) {
            throw new IllegalStateException("El abono no permite tomar más clases");
        }
        this.clasesRestantes--;

        if (this.clasesRestantes == 0) {
            this.estado = EstadoAbono.FINALIZADO;
        }
    }

    public boolean estaVencido() {
        return LocalDate.now().isAfter(fechaVencimiento);
    }

    public boolean incluyePension() {
        return planPension != null && precioContratadoPension != null;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public PlanAbono getPlanAbono() {
        return planAbono;
    }

    public void setPlanAbono(PlanAbono planAbono) {
        this.planAbono = planAbono;
    }

    public PlanPension getPlanPension() {
        return planPension;
    }

    public void setPlanPension(PlanPension planPension) {
        this.planPension = planPension;
    }

    public Caballo getCaballo() {
        return caballo;
    }

    public void setCaballo(Caballo caballo) {
        this.caballo = caballo;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getClasesTotales() {
        return clasesTotales;
    }

    public void setClasesTotales(Integer clasesTotales) {
        this.clasesTotales = clasesTotales;
    }

    public Integer getClasesRestantes() {
        return clasesRestantes;
    }

    public void setClasesRestantes(Integer clasesRestantes) {
        this.clasesRestantes = clasesRestantes;
    }

    public EstadoAbono getEstado() {
        return estado;
    }

    public void setEstado(EstadoAbono estado) {
        this.estado = estado;
    }

    public BigDecimal getPrecioContratadoClases() {
        return precioContratadoClases;
    }

    public void setPrecioContratadoClases(BigDecimal precioContratadoClases) {
        this.precioContratadoClases = precioContratadoClases;
    }

    public BigDecimal getPrecioContratadoPension() {
        return precioContratadoPension;
    }

    public void setPrecioContratadoPension(BigDecimal precioContratadoPension) {
        this.precioContratadoPension = precioContratadoPension;
    }

    public BigDecimal getCuotaSocioContratada() {
        return cuotaSocioContratada;
    }

    public void setCuotaSocioContratada(BigDecimal cuotaSocioContratada) {
        this.cuotaSocioContratada = cuotaSocioContratada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}