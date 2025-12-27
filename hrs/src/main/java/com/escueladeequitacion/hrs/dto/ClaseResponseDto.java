package com.escueladeequitacion.hrs.dto;

import com.escueladeequitacion.hrs.enums.Especialidades;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Clase;

import java.time.LocalDate;
import java.time.LocalTime;

// DTO para la respuesta de Clase con datos relacionados de Instructor, Alumno y Caballo.
public class ClaseResponseDto {
    private Long id;
    private Especialidades especialidades;
    private LocalDate dia;
    private LocalTime hora;
    private Estado estado;
    private String observaciones;

    // Datos del instructor
    private Long instructorId;
    private String instructorNombre;
    private String instructorApellido;
    private String instructorNombreCompleto;

    // Datos del alumno
    private Long alumnoId;
    private String alumnoNombre;
    private String alumnoApellido;
    private String alumnoNombreCompleto;

    // Datos del caballo
    private Long caballoId;
    private String caballoNombre;
    private String caballoTipo;

    public ClaseResponseDto() {
    }

    /**
     * Constructor que convierte una entidad Clase en ClaseResponseDto.
     * Este constructor extrae los datos necesarios de las relaciones.
     */
    public ClaseResponseDto(Clase clase) {
        this.id = clase.getId();
        this.especialidades = clase.getEspecialidades();
        this.dia = clase.getDia();
        this.hora = clase.getHora();
        this.estado = clase.getEstado();
        this.observaciones = clase.getObservaciones();

        // Extraer datos del instructor
        if (clase.getInstructor() != null) {
            this.instructorId = clase.getInstructor().getId();
            this.instructorNombre = clase.getInstructor().getNombre();
            this.instructorApellido = clase.getInstructor().getApellido();
            this.instructorNombreCompleto = clase.getInstructor().getNombreCompleto();
        }

        // Extraer datos del alumno
        if (clase.getAlumno() != null) {
            this.alumnoId = clase.getAlumno().getId();
            this.alumnoNombre = clase.getAlumno().getNombre();
            this.alumnoApellido = clase.getAlumno().getApellido();
            this.alumnoNombreCompleto = clase.getAlumno().getNombreCompleto();
        }

        // Extraer datos del caballo
        if (clase.getCaballo() != null) {
            this.caballoId = clase.getCaballo().getId();
            this.caballoNombre = clase.getCaballo().getNombre();
            this.caballoTipo = clase.getCaballo().getTipoCaballo().toString();
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Especialidades getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Especialidades especialidades) {
        this.especialidades = especialidades;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Getters y Setters del Instructor
    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorNombre() {
        return instructorNombre;
    }

    public void setInstructorNombre(String instructorNombre) {
        this.instructorNombre = instructorNombre;
    }

    public String getInstructorApellido() {
        return instructorApellido;
    }

    public void setInstructorApellido(String instructorApellido) {
        this.instructorApellido = instructorApellido;
    }

    public String getInstructorNombreCompleto() {
        return instructorNombreCompleto;
    }

    public void setInstructorNombreCompleto(String instructorNombreCompleto) {
        this.instructorNombreCompleto = instructorNombreCompleto;
    }

    // Getters y Setters del Alumno
    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getAlumnoNombre() {
        return alumnoNombre;
    }

    public void setAlumnoNombre(String alumnoNombre) {
        this.alumnoNombre = alumnoNombre;
    }

    public String getAlumnoApellido() {
        return alumnoApellido;
    }

    public void setAlumnoApellido(String alumnoApellido) {
        this.alumnoApellido = alumnoApellido;
    }

    public String getAlumnoNombreCompleto() {
        return alumnoNombreCompleto;
    }

    public void setAlumnoNombreCompleto(String alumnoNombreCompleto) {
        this.alumnoNombreCompleto = alumnoNombreCompleto;
    }

    // Getters y Setters del Caballo
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

    public String getCaballoTipo() {
        return caballoTipo;
    }

    public void setCaballoTipo(String caballoTipo) {
        this.caballoTipo = caballoTipo;
    }
}