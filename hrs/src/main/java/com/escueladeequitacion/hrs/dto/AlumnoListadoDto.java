package com.escueladeequitacion.hrs.dto;

import java.time.LocalDate;

public class AlumnoListadoDto extends PersonaDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private LocalDate fechaInscripcion;
    private Boolean activo;
    private Boolean propietario;
    private Integer cantidadClases;
    private String tipoPension;
    private String cuotaPension;
    private Long caballoId;
    private String caballoNombre;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getPropietario() {
        return propietario;
    }

    public void setPropietario(Boolean propietario) {
        this.propietario = propietario;
    }

    public Integer getCantidadClases() {
        return cantidadClases;
    }

    public void setCantidadClases(Integer cantidadClases) {
        this.cantidadClases = cantidadClases;
    }

    public String getTipoPension() {
        return tipoPension;
    }

    public void setTipoPension(String tipoPension) {
        this.tipoPension = tipoPension;
    }

    public String getCuotaPension() {
        return cuotaPension;
    }

    public void setCuotaPension(String cuotaPension) {
        this.cuotaPension = cuotaPension;
    }

    public Long getCaballoId() {
        return caballoId;
    }

    public void setCaballoId(Long caballoId) {
        this.caballoId = caballoId;
    }

    public String getCaballoNombre() {
        return caballoNombre;
    }

    public void setCaballoNombre(String caballoNombre) {
        this.caballoNombre = caballoNombre;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

}
