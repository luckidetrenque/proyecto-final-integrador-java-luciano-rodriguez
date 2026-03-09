package com.escueladeequitacion.hrs.dto;

import java.util.List;

public class ResumenFinancieroDto {

    private double ingresosCuotasProyectado; // Suma cuotas alumnos activos
    private double ingresosPensionesProyectado; // Suma pensiones activas
    private double ingresosTotalProyectado;
    private double egresoHonorarios; // Suma honorarios instructores en período
    private double balanceProyectado;

    // Desglose por plan
    private List<DesglosePlanDto> desglosePlanes;

    // Evolución mensual (para gráfico de línea)
    private List<PuntoEvolucionDto> evolucion;

    // Constructores vacío
    public ResumenFinancieroDto() {
    }

    // ── Getters & Setters ─────────────────────────────────────
    public double getIngresosCuotasProyectado() {
        return ingresosCuotasProyectado;
    }

    public void setIngresosCuotasProyectado(double v) {
        this.ingresosCuotasProyectado = v;
    }

    public double getIngresosPensionesProyectado() {
        return ingresosPensionesProyectado;
    }

    public void setIngresosPensionesProyectado(double v) {
        this.ingresosPensionesProyectado = v;
    }

    public double getIngresosTotalProyectado() {
        return ingresosTotalProyectado;
    }

    public void setIngresosTotalProyectado(double v) {
        this.ingresosTotalProyectado = v;
    }

    public double getEgresoHonorarios() {
        return egresoHonorarios;
    }

    public void setEgresoHonorarios(double v) {
        this.egresoHonorarios = v;
    }

    public double getBalanceProyectado() {
        return balanceProyectado;
    }

    public void setBalanceProyectado(double v) {
        this.balanceProyectado = v;
    }

    public List<DesglosePlanDto> getDesglosePlanes() {
        return desglosePlanes;
    }

    public void setDesglosePlanes(List<DesglosePlanDto> v) {
        this.desglosePlanes = v;
    }

    public List<PuntoEvolucionDto> getEvolucion() {
        return evolucion;
    }

    public void setEvolucion(List<PuntoEvolucionDto> v) {
        this.evolucion = v;
    }

    // ── Clases internas ───────────────────────────────────────
    public static class DesglosePlanDto {
        private int cantidadClases;
        private int alumnosEnPlan;
        private double cuotaUnitaria;
        private double subtotal;

        public DesglosePlanDto() {
        }

        public DesglosePlanDto(int cantidadClases, int alumnosEnPlan, double cuotaUnitaria) {
            this.cantidadClases = cantidadClases;
            this.alumnosEnPlan = alumnosEnPlan;
            this.cuotaUnitaria = cuotaUnitaria;
            this.subtotal = alumnosEnPlan * cuotaUnitaria;
        }

        public int getCantidadClases() {
            return cantidadClases;
        }

        public int getAlumnosEnPlan() {
            return alumnosEnPlan;
        }

        public double getCuotaUnitaria() {
            return cuotaUnitaria;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setCantidadClases(int v) {
            this.cantidadClases = v;
        }

        public void setAlumnosEnPlan(int v) {
            this.alumnosEnPlan = v;
        }

        public void setCuotaUnitaria(double v) {
            this.cuotaUnitaria = v;
        }

        public void setSubtotal(double v) {
            this.subtotal = v;
        }
    }

    public static class PuntoEvolucionDto {
        private String mes; // "Ene 2025"
        private double ingresos;
        private double egresos;

        public PuntoEvolucionDto() {
        }

        public PuntoEvolucionDto(String mes, double ingresos, double egresos) {
            this.mes = mes;
            this.ingresos = ingresos;
            this.egresos = egresos;
        }

        public String getMes() {
            return mes;
        }

        public double getIngresos() {
            return ingresos;
        }

        public double getEgresos() {
            return egresos;
        }

        public void setMes(String v) {
            this.mes = v;
        }

        public void setIngresos(double v) {
            this.ingresos = v;
        }

        public void setEgresos(double v) {
            this.egresos = v;
        }
    }
}