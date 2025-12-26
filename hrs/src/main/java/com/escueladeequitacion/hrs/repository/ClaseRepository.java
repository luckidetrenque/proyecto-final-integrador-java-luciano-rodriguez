package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.enums.Especialidades;
import com.escueladeequitacion.hrs.model.Clase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import com.escueladeequitacion.hrs.enums.Estado;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    // Métodos para buscar alumnos por diferentes criterios
    public List<Clase> findByEspecialidades(Especialidades especialidades);

    public List<Clase> findByDia(LocalDate dia);

    public List<Clase> findByHora(LocalTime hora);

    public List<Clase> findByEstado(Estado estado);

    public List<Clase> findByInstructorId(Long instructor_id);

    public List<Clase> findByAlumnoId(Long alumno_id);

    public List<Clase> findByCaballoId(Long caballo_id);

    // Métodos para verificar la existencia de una clase por diferentes criterios
    public boolean existsByEspecialidades(Especialidades especialidades);

    public boolean existsByDia(LocalDate dia);

    public boolean existsByHora(LocalTime hora);

    public boolean existsByInstructorId(Long instructor_id);

    public boolean existsByAlumnoId(Long alumno_id);

    public boolean existsByCaballoId(Long caballo_id);

}
