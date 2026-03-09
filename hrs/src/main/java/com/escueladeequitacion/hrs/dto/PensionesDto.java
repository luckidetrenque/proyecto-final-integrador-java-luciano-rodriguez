package com.escueladeequitacion.hrs.dto;

import java.util.List;

public class PensionesDto {

    private double totalMensual;
    private List<FilaPensionDto> filas;

    public PensionesDto() {
    }

    public double getTotalMensual() {
        return totalMensual;
    }

    public void setTotalMensual(double v) {
        this.totalMensual = v;
    }

    public List<FilaPensionDto> getFilas() {
        return filas;
    }

    public void setFilas(List<FilaPensionDto> v) {
        this.filas = v;
    }

    public static class FilaPensionDto {
        private long alumnoId;
        private String nombre;
        private String apellido;
        private String tipoPension; // CABALLO_PROPIO | RESERVA_ESCUELA
        private String cuotaPension; // ENTERA | MEDIA | TERCIO
        private String caballoNombre;
        private double monto;

        public FilaPensionDto() {
        }

        public long getAlumnoId() {
            return alumnoId;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public String getTipoPension() {
            return tipoPension;
        }

        public String getCuotaPension() {
            return cuotaPension;
        }

        public String getCaballoNombre() {
            return caballoNombre;
        }

        public double getMonto() {
            return monto;
        }

        public void setAlumnoId(long v) {
            this.alumnoId = v;
        }

        public void setNombre(String v) {
            this.nombre = v;
        }

        public void setApellido(String v) {
            this.apellido = v;
        }

        public void setTipoPension(String v) {
            this.tipoPension = v;
        }

        public void setCuotaPension(String v) {
            this.cuotaPension = v;
        }

        public void setCaballoNombre(String v) {
            this.caballoNombre = v;
        }

        public void setMonto(double v) {
            this.monto = v;
        }
    }
}