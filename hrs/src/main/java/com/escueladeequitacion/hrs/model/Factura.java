package com.escueladeequitacion.hrs.model;

import com.escueladeequitacion.hrs.enums.EstadoFactura;
import com.escueladeequitacion.hrs.enums.FormaPago;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas", indexes = {
        @Index(name = "idx_factura_numero", columnList = "numero_factura", unique = true),
        @Index(name = "idx_factura_alumno", columnList = "alumno_id"),
        @Index(name = "idx_factura_estado", columnList = "estado"),
        @Index(name = "idx_factura_vencimiento", columnList = "fecha_vencimiento")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", nullable = false, unique = true, length = 50)
    private String numeroFactura; // Ej: "FC-2025-00123"

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abono_id")
    private Abono abono; // Si es factura de abono mensual

    // Fechas
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    // Montos
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "descuentos", precision = 10, scale = 2)
    private BigDecimal descuentos = BigDecimal.ZERO;

    @Column(name = "recargos", precision = 10, scale = 2)
    private BigDecimal recargos = BigDecimal.ZERO;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(name = "saldo_pendiente", precision = 10, scale = 2, nullable = false)
    private BigDecimal saldoPendiente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoFactura estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", length = 30)
    private FormaPago formaPago;

    // Items de la factura
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemFactura> items = new ArrayList<>();

    // Pagos recibidos
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Pago> pagos = new ArrayList<>();

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // Constructores
    public Factura() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoFactura.PENDIENTE;
        this.descuentos = BigDecimal.ZERO;
        this.recargos = BigDecimal.ZERO;
    }

    // Métodos de utilidad
    public BigDecimal calcularTotal() {
        return subtotal.subtract(descuentos).add(recargos);
    }

    public boolean estaPaga() {
        return estado == EstadoFactura.PAGADA ||
                (saldoPendiente != null && saldoPendiente.compareTo(BigDecimal.ZERO) == 0);
    }

    public boolean estaVencida() {
        return estado == EstadoFactura.VENCIDA ||
                (!estaPaga() && LocalDate.now().isAfter(fechaVencimiento));
    }

    public void agregarItem(ItemFactura item) {
        items.add(item);
        item.setFactura(this);
    }

    public void recalcularSubtotal() {
        this.subtotal = items.stream()
                .map(ItemFactura::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.total = calcularTotal();
        this.saldoPendiente = this.total;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Abono getAbono() {
        return abono;
    }

    public void setAbono(Abono abono) {
        this.abono = abono;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(BigDecimal descuentos) {
        this.descuentos = descuentos;
    }

    public BigDecimal getRecargos() {
        return recargos;
    }

    public void setRecargos(BigDecimal recargos) {
        this.recargos = recargos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(BigDecimal saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}