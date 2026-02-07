package com.escueladeequitacion.hrs.dto;

public class AlumnoPruebaDto extends PersonaDto {

    private Boolean propietario = false;
    private Long caballoId;

    // Constructores, getters y setters
    public AlumnoPruebaDto() {
        super();
    }

    public Boolean isPropietario() {
        return propietario;
    }

    public void setPropietario(Boolean propietario) {
        this.propietario = propietario;
    }

    public Long getCaballoId() {
        return caballoId;
    }

    public void setCaballoId(Long caballoId) {
        this.caballoId = caballoId;
    }
}
