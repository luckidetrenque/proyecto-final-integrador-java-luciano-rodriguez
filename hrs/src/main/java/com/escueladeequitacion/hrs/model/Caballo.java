package com.escueladeequitacion.hrs.model;

import java.util.ArrayList;
import java.util.List;

import com.escueladeequitacion.hrs.enums.Tipo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

/**
 * Clase que representa a un Caballo en la escuela de equitación.
 */
@Entity
@Table(name = "caballos" , indexes = {
    @Index(name = "idx_caballo_nombre", columnList = "nombre"),
    @Index(name = "idx_caballo_disponible", columnList = "disponible"),
    @Index(name = "idx_caballo_tipo", columnList = "tipoCaballo")
})
public class Caballo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "disponible", nullable = false)
    private Boolean disponible; // Para indicar si el caballo está disponible o no
    @Column(name = "tipo", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    @OneToMany(mappedBy = "caballoPropio")
    @JsonIgnore
    private List<Alumno> propietarios = new ArrayList<>();

    @OneToMany(mappedBy = "caballo", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Clase> clases = new ArrayList<>();

    // Constructor vacío
    public Caballo() {
    }

    // Constructor con parámetros
    public Caballo(String nombre, Boolean disponible, Tipo tipo) {
        this.nombre = nombre;
        this.disponible = disponible;
        this.tipo = tipo;

    }

    // Getters y Setters
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

    public Boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Tipo gettipo() {
        return tipo;
    }

    public void settipo(Tipo tipo) {
        this.tipo = tipo;
    }

    // Métodos para la lista de clases
    public void agregarClase(Clase clase) {
        clases.add(clase);
        clase.setCaballo(this);
    }

    public void removerClase(Clase clase) {
        clases.remove(clase);
        clase.setCaballo(null);
    }

}