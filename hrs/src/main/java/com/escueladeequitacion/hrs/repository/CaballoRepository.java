package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.Caballo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.escueladeequitacion.hrs.enums.Tipo;

@Repository
public interface CaballoRepository extends JpaRepository<Caballo, Long>,
        JpaSpecificationExecutor<Caballo> {

    List<Caballo> findByNombreIgnoreCase(@Param("nombre") String nombre);

    List<Caballo> findByDisponible(@Param("disponible") Boolean disponible);

    List<Caballo> findBytipo(Tipo tipo);

    Boolean existsByNombre(String nombre);

    void deleteByNombre(String nombre);

    @Query("SELECT c FROM Caballo c LEFT JOIN FETCH c.propietarios WHERE c.id = :caballoId")
    Optional<Caballo> findCaballoConAlumnos(@Param("caballoId") Long caballoId);

    // Mantenemos por compatibilidad
    Page<Caballo> findByDisponible(Boolean disponible, Pageable pageable);
    Page<Caballo> findByTipo(Tipo tipo, Pageable pageable);
    Page<Caballo> findByDisponibleAndTipo(Boolean disponible, Tipo tipo, Pageable pageable);
}
