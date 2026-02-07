package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.Caballo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.escueladeequitacion.hrs.enums.Tipo;

// Repositorio para la entidad Caballo
@Repository
public interface CaballoRepository extends JpaRepository<Caballo, Long> {

    // Métodos para buscar caballos por diferentes criterios
    public List<Caballo> findByNombreIgnoreCase(@Param("nombre") String nombre);

    public List<Caballo> findByDisponible(@Param("disponible") Boolean disponible);

    public List<Caballo> findBytipo(Tipo tipo);

    // Métodos para verificar la existencia de un caballo por diferentes criterios
    public Boolean existsByNombre(String nombre);

    // Método para eliminar un caballo por su nombre
    public void deleteByNombre(String nombre);

    // Método para buscar un caballo junto con sus alumnos propietarios
    @Query("SELECT c FROM Caballo c LEFT JOIN FETCH c.propietarios WHERE c.id = :caballoId")
    public Optional<Caballo> findCaballoConAlumnos(@Param("caballoId") Long caballoId);
}
