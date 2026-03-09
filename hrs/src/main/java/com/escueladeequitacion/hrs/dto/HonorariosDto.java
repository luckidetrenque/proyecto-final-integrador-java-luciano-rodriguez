package com.escueladeequitacion.hrs.dto;

import java.util.List;

public class HonorariosDto {

    private double totalHonorarios;
    private List<FilaInstructorDto> filas;

    public HonorariosDto() {
    }

    public double getTotalHonorarios() {
        return totalHonorarios;
    }

    public void setTotalHonorarios(double v) {
        this.totalHonorarios = v;
    }

    public List<FilaInstructorDto> getFilas() {
        return filas;
    }

    public void setFilas(List<FilaInstructorDto> v) {
        this.filas = v;
    }

    public static class FilaInstructorDto {
        private long instructorId;
        private String nombre;
        private String apellido;
        private long clasesCompletadas;
        private double honorarioBase;
        private double honorarioPorClases;
        private double totalHonorario;

        public FilaInstructorDto() {
        }

        public long getInstructorId() {
            return instructorId;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public long getClasesCompletadas() {
            return clasesCompletadas;
        }

        public double getHonorarioBase() {
            return honorarioBase;
        }

        public double getHonorarioPorClases() {
            return honorarioPorClases;
        }

        public double getTotalHonorario() {
            return totalHonorario;
        }

        public void setInstructorId(long v) {
            this.instructorId = v;
        }

        public void setNombre(String v) {
            this.nombre = v;
        }

        public void setApellido(String v) {
            this.apellido = v;
        }

        public void setClasesCompletadas(long v) {
            this.clasesCompletadas = v;
        }

        public void setHonorarioBase(double v) {
            this.honorarioBase = v;
        }

        public void setHonorarioPorClases(double v) {
            this.honorarioPorClases = v;
        }

        public void setTotalHonorario(double v) {
            this.totalHonorario = v;
        }
    }
}