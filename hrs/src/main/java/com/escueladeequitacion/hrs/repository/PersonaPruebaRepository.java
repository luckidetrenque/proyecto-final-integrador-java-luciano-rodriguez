package com.escueladeequitacion.hrs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.escueladeequitacion.hrs.model.PersonaPrueba;

@Repository
public interface PersonaPruebaRepository extends JpaRepository<PersonaPrueba, Long> {

    List<PersonaPrueba> findByNombreIgnoreCaseAndApellidoIgnoreCase(String nombre, String apellido);

    boolean existsByNombreIgnoreCaseAndApellidoIgnoreCase(String nombre, String apellido);
}
