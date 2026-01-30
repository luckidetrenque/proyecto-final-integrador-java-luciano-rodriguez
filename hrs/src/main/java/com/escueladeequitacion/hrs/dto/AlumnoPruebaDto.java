package com.escueladeequitacion.hrs.dto;

public class AlumnoPruebaDto extends PersonaDto {

    private Boolean propietario = false;
    private Long caballoId; // Nullable - solo si propietario=true

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
}
