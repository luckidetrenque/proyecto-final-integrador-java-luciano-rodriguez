package com.escueladeequitacion.hrs.dto;

import java.util.List;

public class CuotasAlumnosDto {

    private double totalProyectado;
    private int alumnosActivos;
    private List<FilaAlumnoDto> filas;

    public CuotasAlumnosDto() {
    }

    public double getTotalProyectado() {
        return totalProyectado;
    }

    public void setTotalProyectado(double v) {
        this.totalProyectado = v;
    }

    public int getAlumnosActivos() {
        return alumnosActivos;
    }

    public void setAlumnosActivos(int v) {
        this.alumnosActivos = v;
    }

    public List<FilaAlumnoDto> getFilas() {
        return filas;
    }

    public void setFilas(List<FilaAlumnoDto> v) {
        this.filas = v;
    }

    public static class FilaAlumnoDto {
        private long alumnoId;
        private String nombre;
        private String apellido;
        private int plan; // 4, 8, 12 ó 16
        private long clasesCompletadas;
        private double montoCuota;
        private String tipoPension;
        private double montoPension;
        private double totalAlumno; // cuota + pension

        public FilaAlumnoDto() {
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

        public int getPlan() {
            return plan;
        }

        public long getClasesCompletadas() {
            return clasesCompletadas;
        }

        public double getMontoCuota() {
            return montoCuota;
        }

        public String getTipoPension() {
            return tipoPension;
        }

        public double getMontoPension() {
            return montoPension;
        }

        public double getTotalAlumno() {
            return totalAlumno;
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

        public void setPlan(int v) {
            this.plan = v;
        }

        public void setClasesCompletadas(long v) {
            this.clasesCompletadas = v;
        }

        public void setMontoCuota(double v) {
            this.montoCuota = v;
        }

        public void setTipoPension(String v) {
            this.tipoPension = v;
        }

        public void setMontoPension(double v) {
            this.montoPension = v;
        }

        public void setTotalAlumno(double v) {
            this.totalAlumno = v;
        }
    }
}