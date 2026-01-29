package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.enums.Especialidad;
import com.escueladeequitacion.hrs.model.Clase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    public List<Clase> findByEspecialidad(Especialidad especialidad);

    public List<Clase> findByDia(LocalDate dia);

    public List<Clase> findByHora(LocalTime hora);

    public List<Clase> findByEstado(Estado estado);

    public List<Clase> findByInstructorId(Long instructor_id);

    public List<Clase> findByAlumnoId(Long alumno_id);

    public List<Clase> findByCaballoId(Long caballo_id);

    // Métodos para verificar la existencia de una clase por diferentes criterios
    public boolean existsByEspecialidad(Especialidad especialidad);

    public boolean existsByDia(LocalDate dia);

    public boolean existsByHora(LocalTime hora);

    public boolean existsByEstado(Estado estado);

    public boolean existsByInstructorId(Long instructor_id);

    public boolean existsByAlumnoId(Long alumno_id);

    public boolean existsByCaballoId(Long caballo_id);

    /**
     * Busca clases por día y estado específicos.
     * Necesario para las tareas programadas de cambio de estado.
     */
    public List<Clase> findByDiaAndEstado(LocalDate dia, Estado estado);

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
            "JOIN FETCH c.caballo " +
            "ORDER BY c.dia ASC, c.hora ASC")
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
            "WHERE c.dia = :dia " +
            "ORDER BY c.hora ASC")
    public List<Clase> findByDiaWithDetails(@Param("dia") LocalDate dia);

    /**
     * Busca clases por día y hora con relaciones cargadas.
     * 
     * @param dia  - Fecha de la clase
     * @param hora - Hora de la clase
     * @return Lista de clases con instructor, alumno y caballo cargados
     */
    @Query("SELECT c FROM Clase c " +
            "JOIN FETCH c.instructor " +
            "JOIN FETCH c.alumno " +
            "JOIN FETCH c.caballo " +
            "WHERE c.dia = :dia AND c.hora = :hora ")
    public List<Clase> findByDiaAndHoraWithDetails(@Param("dia") LocalDate dia, @Param("hora") LocalTime hora);

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

    /**
     * Cuenta clases por alumno y estado.
     */
    @Query("SELECT COUNT(c) FROM Clase c WHERE c.alumno.id = :alumnoId AND c.estado = :estado")
    public long contarPorAlumnoYEstado(@Param("alumnoId") Long alumnoId, @Param("estado") Estado estado);

    /**
     * Cuenta clases por instructor y estado.
     */
    @Query("SELECT COUNT(c) FROM Clase c WHERE c.instructor.id = :instructorId AND c.estado = :estado")
    long contarPorInstructorYEstado(@Param("instructorId") Long instructorId, @Param("estado") Estado estado);

    /**
     * Cuenta clases por caballo y estado.
     */
    @Query("SELECT COUNT(c) FROM Clase c WHERE c.caballo.id = :caballoId AND c.estado = :estado")
    public long contarPorCaballoYEstado(@Param("caballoId") Long caballoId, @Param("estado") Estado estado);

    // Método para copiar las clases de un período a otro
    public List<Clase> findByDiaBetween(LocalDate inicio, LocalDate fin);

    // Método para eliminar las clases en un período
    @Modifying
    @Query("DELETE FROM Clase c WHERE c.dia BETWEEN :inicio AND :fin AND c.estado = :estado")
    public void deleteByDiaBetweenAndEstado(LocalDate inicio, LocalDate fin, Estado estado);

    @Query("""
               SELECT COUNT(c) > 0
               FROM Clase c
               WHERE c.dia = :dia
                 AND c.hora = :hora
                 AND (:id IS NULL OR c.id <> :id)
                 AND c.alumno.id = :alumnoId
            """)
    public boolean existsConflictoAlumno(
            Long id,
            LocalDate dia,
            LocalTime hora,
            Long alumnoId);

    @Query("""
               SELECT COUNT(c) > 0
               FROM Clase c
               WHERE c.dia = :dia
                 AND c.hora = :hora
                 AND (:id IS NULL OR c.id <> :id)
                 AND c.caballo.id = :caballoId
            """)
    public boolean existsConflictoCaballo(
            Long id,
            LocalDate dia,
            LocalTime hora,
            Long caballoId);

    // Buscar clases de prueba de un alumno
    @Query("SELECT c FROM Clase c WHERE c.alumno.id = :alumnoId AND c.esPrueba = true")
    public List<Clase> findClasesDePruebaPorAlumno(@Param("alumnoId") Long alumnoId);

    // Contar clases de prueba de un alumno
    @Query("SELECT COUNT(c) FROM Clase c WHERE c.alumno.id = :alumnoId AND c.esPrueba = true")
    public long contarClasesDePruebaPorAlumno(@Param("alumnoId") Long alumnoId);

    // Verificar si un alumno ya tomó clase de prueba
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Clase c WHERE c.alumno.id = :alumnoId AND c.esPrueba = true")
    public boolean alumnoTieneClaseDePrueba(@Param("alumnoId") Long alumnoId);

    // Buscar todas las clases de prueba
    public List<Clase> findByEsPrueba(Boolean esPrueba);

}