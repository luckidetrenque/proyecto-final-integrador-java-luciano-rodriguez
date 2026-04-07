package com.escueladeequitacion.hrs.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.escueladeequitacion.hrs.enums.CuotaPension;
import com.escueladeequitacion.hrs.enums.Rol;
import com.escueladeequitacion.hrs.enums.TipoPension;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

/**
 * Clase que representa a un Alumno en la escuela de equitación.
 * Hereda de la clase Persona e incluye atributos específicos de un alumno.
 */
@Entity
@Table(name = "alumnos", indexes = {
        @Index(name = "idx_alumno_dni", columnList = "dni", unique = true),
        @Index(name = "idx_alumno_nombre_apellido", columnList = "nombre, apellido"),
        @Index(name = "idx_alumno_activo", columnList = "activo"),
        @Index(name = "idx_alumno_fecha_inscripcion", columnList = "fecha_inscripcion")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Alumno extends Persona {
    // Atributos específicos de Alumno
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;
    @Column(name = "cantidad_clases", nullable = false)
    private Integer cantidadClases;
    @Column(name = "activo", nullable = false)
    private Boolean activo; // Para indicar si el alumno está activo o inactivo
    @Column(name = "propietario", nullable = false)
    private Boolean propietario; // Para indicar si el alumno tiene caballo propio o no
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "caballo_id", nullable = true)
    private Caballo caballoPropio;
    @Column(name = "tipo_pension", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TipoPension tipoPension = TipoPension.SIN_CABALLO;
    @Column(name = "cuota_pension", nullable = true, length = 10)
    @Enumerated(EnumType.STRING)
    private CuotaPension cuotaPension; // null si tipoPension = SIN_CABALLO o RESERVA_ESCUELA

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Clase> clases = new ArrayList<>();

    // ── Campos de invitación para registro de usuario ─────────────────────────
    @Column(name = "codigo_invitacion", length = 36, unique = true)
    private String codigoInvitacion; // UUID generado al invitar al alumno

    @Column(name = "codigo_usado")
    private Boolean codigoUsado = false;

    @Column(name = "fecha_expiracion_codigo")
    private LocalDateTime fechaExpiracionCodigo; // null = no expira / fecha = vence ese día

    // Constructor vacío
    public Alumno() {
        super();
    }

    // Constructor con parámetros
    public Alumno(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String codigoArea,
            String telefono,
            String email,
            LocalDate fechaInscripcion, Integer cantidadClases, Boolean activo, Boolean propietario,
            Caballo caballoPropio, TipoPension tipoPension, CuotaPension cuotaPension) {
        super(dni, nombre, apellido, fechaNacimiento, codigoArea, telefono, email);
        this.fechaInscripcion = fechaInscripcion;
        this.cantidadClases = cantidadClases != null ? cantidadClases : 0;
        this.activo = activo != null ? activo : false;
        this.propietario = propietario;
        this.tipoPension = tipoPension;
        this.cuotaPension = cuotaPension;
        this.caballoPropio = null;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getCantidadClases() {
        return cantidadClases;
    }

    public void setCantidadClases(Integer cantidadClases) {
        this.cantidadClases = cantidadClases;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean isPropietario() {
        return propietario;
    }

    public void setPropietario(Boolean propietario) {
        this.propietario = propietario;
    }

    public Caballo getCaballoPropio() {
        return caballoPropio;
    }

    public void setCaballoPropio(Caballo caballoPropio) {
        this.caballoPropio = caballoPropio;
    }

    public TipoPension getTipoPension() {
        return tipoPension;
    }

    public void setTipoPension(TipoPension tipoPension) {
        this.tipoPension = tipoPension;
    }

    public CuotaPension getCuotaPension() {
        return cuotaPension;
    }

    public void setCuotaPension(CuotaPension cuotaPension) {
        this.cuotaPension = cuotaPension;
    }

    // Métodos para la lista de clases
    public void agregarClase(Clase clase) {
        clases.add(clase);
        clase.setAlumno(this);
    }

    public void removerClase(Clase clase) {
        clases.remove(clase);
        clase.setAlumno(null);
    }

    // Campos de invitación - Getters & Setters
    public String getCodigoInvitacion() { return codigoInvitacion; }
    public void setCodigoInvitacion(String codigoInvitacion) { this.codigoInvitacion = codigoInvitacion; }

    public Boolean getCodigoUsado() { return codigoUsado; }
    public void setCodigoUsado(Boolean codigoUsado) { this.codigoUsado = codigoUsado; }

    public LocalDateTime getFechaExpiracionCodigo() { return fechaExpiracionCodigo; }
    public void setFechaExpiracionCodigo(LocalDateTime fechaExpiracionCodigo) { this.fechaExpiracionCodigo = fechaExpiracionCodigo; }

    // Métodos sobrescritos de Persona
    @Override
    public Rol getRol() {
        return Rol.ALUMNO;
    }

}