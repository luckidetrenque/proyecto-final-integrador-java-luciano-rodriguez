package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    // Métodos para buscar alumnos por diferentes criterios
    public Optional<Instructor> findByDni(@Param("dni") Integer dni);

    public List<Instructor> findByNombreIgnoreCase(@Param("nombre") String nombre);

    public List<Instructor> findByApellidoIgnoreCase(@Param("apellido") String apellido);

    public List<Instructor> findByNombreAndApellidoIgnoreCase(@Param("nombre") String nombre,
            @Param("apellido") String apellido);

    public List<Instructor> findByActivo(@Param("activo") Boolean activo);

    public List<Instructor> findByFechaNacimiento(LocalDate fechaNacimiento);

    // Métodos para verificar la existencia de un instructor por diferentes
    // criterios
    public Boolean existsByDni(Integer dni);

    public Boolean existsByNombreAndApellidoIgnoreCase(String nombre, String apellido);

    // Método para eliminar un instructor por su DNI
    public void deleteByDni(Integer dni);

}
