package com.escueladeequitacion.hrs.model;

import jakarta.persistence.*;

/**
 * Tabla singleton (id=1) con las reglas operativas configurables por el
 * Coordinador: anticipación mínima para cancelar o reservar clases.
 */
@Entity
@Table(name = "configuracion_operativa")
public class ConfiguracionOperativa {

    @Id
    @Column(name = "id")
    private Long id = 1L; // Siempre 1 — singleton

    /**
     * Horas mínimas de anticipación para que un alumno pueda cancelar una
     * clase sin penalidad.
     */
    @Column(name = "horas_anticipacion_cancelacion", nullable = false)
    private Integer horasAnticipacionCancelacion = 2;

    /**
     * Horas mínimas de anticipación para que un alumno pueda reservar una
     * clase.
     */
    @Column(name = "horas_anticipacion_reserva", nullable = false)
    private Integer horasAnticipacionReserva = 24;

    /**
     * Días de validez del código de invitación para registro de usuarios.
     */
    @Column(name = "dias_validez_invitacion", nullable = false)
    private Integer diasValidezInvitacion = 7;

    // ── Constructores ─────────────────────────────────────────
    public ConfiguracionOperativa() {
    }

    // ── Getters & Setters ─────────────────────────────────────
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHorasAnticipacionCancelacion() {
        return horasAnticipacionCancelacion;
    }

    public void setHorasAnticipacionCancelacion(Integer horasAnticipacionCancelacion) {
        this.horasAnticipacionCancelacion = horasAnticipacionCancelacion;
    }

    public Integer getHorasAnticipacionReserva() {
        return horasAnticipacionReserva;
    }

    public void setHorasAnticipacionReserva(Integer horasAnticipacionReserva) {
        this.horasAnticipacionReserva = horasAnticipacionReserva;
    }

    public Integer getDiasValidezInvitacion() {
        return diasValidezInvitacion;
    }

    public void setDiasValidezInvitacion(Integer diasValidezInvitacion) {
        this.diasValidezInvitacion = diasValidezInvitacion;
    }
}
