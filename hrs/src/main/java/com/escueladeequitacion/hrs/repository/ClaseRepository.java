package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.enums.Especialidades;
import com.escueladeequitacion.hrs.model.Clase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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

        public boolean existsByEstado(Estado estado);

        public boolean existsByInstructorId(Long instructor_id);

        public boolean existsByAlumnoId(Long alumno_id);

        public boolean existsByCaballoId(Long caballo_id);

        // ============================================================
        // CONSULTAS CON JOIN FETCH (Nuevas - traen datos relacionados)
        // ============================================================

        /**
         * Obtiene una clase por ID con todos sus datos relacionados (Instructor,
         * Alumno, Caballo).
         * JOIN FETCH hace que Hibernate traiga todo en una sola consulta SQL.
         * 
         * @param id - ID de la clase
         * @return Optional con la clase y sus relaciones cargadas
         */
        @Query("SELECT c FROM Clase c " +
                        "JOIN FETCH c.instructor " +
                        "JOIN FETCH c.alumno " +
                        "JOIN FETCH c.caballo " +
                        "WHERE c.id = :id")
        public Optional<Clase> findByIdWithDetails(@Param("id") Long id);

        /**
         * Lista todas las clases con sus relaciones cargadas.
         * Útil para el endpoint GET /api/v1/clases/detalles
         */
        @Query("SELECT c FROM Clase c " +
                        "JOIN FETCH c.instructor " +
                        "JOIN FETCH c.alumno " +
                        "JOIN FETCH c.caballo")
        public List<Clase> findAllWithDetails();

        /**
         * Busca clases por día con relaciones cargadas.
         * 
         * @param dia - Fecha de la clase
         * @return Lista de clases con instructor, alumno y caballo cargados
         */
        @Query("SELECT c FROM Clase c " +
                        "JOIN FETCH c.instructor " +
                        "JOIN FETCH c.alumno " +
                        "JOIN FETCH c.caballo " +
                        "WHERE c.dia = :dia")
        public List<Clase> findByDiaWithDetails(@Param("dia") LocalDate dia);

        /**
         * Busca clases por instructor con relaciones cargadas.
         * Útil para que un instructor vea sus clases asignadas.
         */
        @Query("SELECT c FROM Clase c " +
                        "JOIN FETCH c.instructor i " +
                        "JOIN FETCH c.alumno " +
                        "JOIN FETCH c.caballo " +
                        "WHERE i.id = :instructorId")
        public List<Clase> findByInstructorIdWithDetails(@Param("instructorId") Long instructorId);

        /**
         * Busca clases por alumno con relaciones cargadas.
         * Útil para que un alumno vea sus clases programadas.
         */
        @Query("SELECT c FROM Clase c " +
                        "JOIN FETCH c.instructor " +
                        "JOIN FETCH c.alumno a " +
                        "JOIN FETCH c.caballo " +
                        "WHERE a.id = :alumnoId")
        public List<Clase> findByAlumnoIdWithDetails(@Param("alumnoId") Long alumnoId);

        /**
         * Busca clases por caballo con relaciones cargadas.
         */
        @Query("SELECT c FROM Clase c " +
                        "JOIN FETCH c.instructor " +
                        "JOIN FETCH c.alumno " +
                        "JOIN FETCH c.caballo cab " +
                        "WHERE cab.id = :caballoId")
        public List<Clase> findByCaballoIdWithDetails(@Param("caballoId") Long caballoId);

        /**
         * Busca clases por estado con relaciones cargadas.
         */
        @Query("SELECT c FROM Clase c " +
                        "JOIN FETCH c.instructor " +
                        "JOIN FETCH c.alumno " +
                        "JOIN FETCH c.caballo " +
                        "WHERE c.estado = :estado")
        public List<Clase> findByEstadoWithDetails(@Param("estado") Estado estado);

}