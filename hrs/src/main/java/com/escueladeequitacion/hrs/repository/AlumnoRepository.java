package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Alumno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long>,
        JpaSpecificationExecutor<Alumno> {

    Optional<Alumno> findByDni(@Param("dni") String dni);

    List<Alumno> findByNombreIgnoreCase(@Param("nombre") String nombre);

    List<Alumno> findByApellidoIgnoreCase(@Param("apellido") String apellido);

    List<Alumno> findByNombreAndApellidoIgnoreCase(@Param("nombre") String nombre,
            @Param("apellido") String apellido);

    List<Alumno> findByActivo(@Param("activo") Boolean activo);

    List<Alumno> findByPropietario(@Param("propietario") Boolean propietario);

    List<Alumno> findByFechaInscripcion(LocalDate fechaInscripcion);

    List<Alumno> findByFechaNacimiento(LocalDate fechaNacimiento);

    Boolean existsByDni(String dni);

    Boolean existsByNombreAndApellidoIgnoreCase(String nombre, String apellido);

    void deleteByDni(String dni);

    @Query("SELECT COUNT(c) FROM Clase c WHERE c.alumno.id = :alumnoId AND c.estado = :estado")
    long contarClasesPorEstado(@Param("alumnoId") Long alumnoId, @Param("estado") Estado estado);

    @Query("SELECT COUNT(c) FROM Clase c WHERE c.alumno.id = :alumnoId AND c.estado IN :estados")
    long contarClasesPorEstados(
            @Param("alumnoId") Long alumnoId,
            @Param("estados") List<Estado> estados);

    @Query("SELECT a FROM Alumno a LEFT JOIN FETCH a.caballoPropio WHERE a.id = :alumnoId")
    Optional<Alumno> findAlumnoConCaballo(@Param("alumnoId") Long alumnoId);

    // Mantenemos estos métodos por compatibilidad con código existente
    Page<Alumno> findByActivo(Boolean activo, Pageable pageable);
    
    @Query("SELECT DISTINCT a FROM Alumno a JOIN a.clases c WHERE c.instructor.id = :instructorId")
    Page<Alumno> findDistinctByClasesInstructorId(@Param("instructorId") Long instructorId, Pageable pageable);
    Page<Alumno> findByPropietario(Boolean propietario, Pageable pageable);
    Page<Alumno> findByCantidadClases(Integer cantidadClases, Pageable pageable);
    Page<Alumno> findByActivoAndPropietario(Boolean activo, Boolean propietario, Pageable pageable);
    Page<Alumno> findByActivoAndCantidadClases(Boolean activo, Integer cantidadClases, Pageable pageable);
    Page<Alumno> findByPropietarioAndCantidadClases(Boolean propietario, Integer cantidadClases, Pageable pageable);
    Page<Alumno> findByActivoAndPropietarioAndCantidadClases(Boolean activo, Boolean propietario,
            Integer cantidadClases, Pageable pageable);

    // Invitación por código UUID
    Optional<Alumno> findByCodigoInvitacion(String codigoInvitacion);
}
