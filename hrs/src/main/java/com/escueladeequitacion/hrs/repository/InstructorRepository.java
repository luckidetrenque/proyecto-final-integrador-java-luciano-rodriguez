package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long>,
        JpaSpecificationExecutor<Instructor> {

    Optional<Instructor> findByDni(@Param("dni") String dni);

    Optional<Instructor> findByColor(@Param("color") String color);

    Optional<Instructor> findByEmailIgnoreCase(@Param("email") String email);

    List<Instructor> findByNombreIgnoreCase(@Param("nombre") String nombre);

    List<Instructor> findByApellidoIgnoreCase(@Param("apellido") String apellido);

    List<Instructor> findByNombreAndApellidoIgnoreCase(@Param("nombre") String nombre,
            @Param("apellido") String apellido);

    List<Instructor> findByActivo(@Param("activo") Boolean activo);

    List<Instructor> findByFechaNacimiento(LocalDate fechaNacimiento);

    Boolean existsByDni(String dni);

    Boolean existsByNombreAndApellidoIgnoreCase(String nombre, String apellido);

    void deleteByDni(String dni);

    Page<Instructor> findByActivo(Boolean activo, Pageable pageable);
}
