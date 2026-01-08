package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Alumno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

// Repositorio para la entidad Alumno
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

        // Métodos para buscar alumnos por diferentes criterios
        public Optional<Alumno> findByDni(@Param("dni") Integer dni);

        public List<Alumno> findByNombreIgnoreCase(@Param("nombre") String nombre);

        public List<Alumno> findByApellidoIgnoreCase(@Param("apellido") String apellido);

        public List<Alumno> findByNombreAndApellidoIgnoreCase(@Param("nombre") String nombre,
                        @Param("apellido") String apellido);

        public List<Alumno> findByActivo(@Param("activo") Boolean activo);

        public List<Alumno> findByPropietario(@Param("propietario") Boolean propietario);

        public List<Alumno> findByFechaInscripcion(LocalDate fechaInscripcion);

        public List<Alumno> findByFechaNacimiento(LocalDate fechaNacimiento);

        // Métodos para verificar la existencia de un alumno por diferentes criterios
        public Boolean existsByDni(Integer dni);

        public Boolean existsByNombreAndApellidoIgnoreCase(String nombre, String apellido);

        // Método para eliminar un alumno por su DNI
        public void deleteByDni(Integer dni);

        // Método para contar las clases completadas de un alumno
        @Query("SELECT COUNT(c) FROM Clase c WHERE c.alumno.id = :alumnoId AND c.estado = :estado")
        public long contarClasesPorEstado(@Param("alumnoId") Long alumnoId, @Param("estado") Estado estado);

        // Método para contar las clases a recuperar (canceladas o ausente con aviso) de
        // un alumno
        @Query("SELECT COUNT(c) FROM Clase c WHERE c.alumno.id = :alumnoId AND c.estado IN :estados")
        public long contarClasesPorEstados(
                        @Param("alumnoId") Long alumnoId,
                        @Param("estados") List<Estado> estados);

}
