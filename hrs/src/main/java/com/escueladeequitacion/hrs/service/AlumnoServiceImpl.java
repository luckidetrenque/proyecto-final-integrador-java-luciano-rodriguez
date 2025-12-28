package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ConflictException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.repository.AlumnoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Implementación de la interfaz AlumnoService
@Service
@Transactional
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public List<Alumno> listarAlumnos() {
        return alumnoRepository.findAll();
    };

    @Override
    public Optional<Alumno> buscarAlumnoPorId(Long id) {
        return alumnoRepository.findById(id);
    };

    @Override
    public Optional<Alumno> buscarAlumnoPorDni(Integer dni) {
        return alumnoRepository.findByDni(dni);
    };

    @Override
    public List<Alumno> buscarAlumnoPorNombre(String nombre) {
        return alumnoRepository.findByNombreIgnoreCase(nombre);
    };

    @Override
    public List<Alumno> buscarAlumnoPorApellido(String apellido) {
        return alumnoRepository.findByApellidoIgnoreCase(apellido);
    };

    @Override
    public List<Alumno> buscarAlumnoPorNombreYApellido(String nombre, String apellido) {
        return alumnoRepository.findByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public List<Alumno> buscarAlumnoPorEstado(Boolean activo) {
        return alumnoRepository.findByActivo(activo);
    };

    @Override
    public List<Alumno> buscarAlumnoConCaballo(Boolean propietario) {
        return alumnoRepository.findByPropietario(propietario);
    };

    @Override
    public List<Alumno> buscarPorFechaInscripcion(LocalDate fechaInscripcion) {
        return alumnoRepository.findByFechaInscripcion(fechaInscripcion);
    };

    @Override
    public List<Alumno> buscarPorFechaNacimiento(LocalDate fechaNacimiento) {
        return alumnoRepository.findByFechaNacimiento(fechaNacimiento);
    };

    @Override
    public Boolean existeAlumnoPorId(Long id) {
        return alumnoRepository.existsById(id);
    };

    @Override
    public Boolean existeAlumnoPorDni(Integer dni) {
        return alumnoRepository.existsByDni(dni);
    };

    @Override
    public Boolean existeAlumnoPorNombreYApellido(String nombre, String apellido) {
        return alumnoRepository.existsByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public Boolean estadoAlumno(Long id) {
        Optional<Alumno> alumno = alumnoRepository.findById(id);
        return alumno.map(Alumno::isActivo).orElse(false);
    };

    @Override
    public void guardarAlumno(Alumno alumno) {
        // Validar DNI duplicado solo al CREAR (cuando id es null)
        if (alumno.getId() == null) {
            if (alumnoRepository.existsByDni(alumno.getDni())) {
                throw new ConflictException("Alumno", "DNI", alumno.getDni());
            }
        }

        alumnoRepository.save(alumno);
    };

    @Override
    public void actualizarAlumno(Long id, Alumno alumno) {
        // 1. Verificar que el alumno que se quiere actualizar existe
        // Usar el método auxiliar en lugar de validar manualmente
        Alumno alumnoExistente = obtenerAlumnoOLanzarExcepcion(id);

        // 2. Si el DNI cambió, verificar que el nuevo DNI no esté en uso por OTRO
        // alumno
        if (!alumnoExistente.getDni().equals(alumno.getDni())) {
            Optional<Alumno> alumnoConNuevoDni = alumnoRepository.findByDni(alumno.getDni());
            if (alumnoConNuevoDni.isPresent()) {
                throw new ConflictException("Ya existe otro alumno con el DNI " + alumno.getDni());
            }
        }

        // 3. Actualizar campos usando método auxiliar
        actualizarCamposDesdeDto(alumnoExistente, alumno);

        // 4. Guardar los cambios
        alumnoRepository.save(alumnoExistente);
    };

    @Override
    public void eliminarAlumno(Long id) {
        obtenerAlumnoOLanzarExcepcion(id);
        alumnoRepository.deleteById(id);
    };


@Override
public void eliminarAlumnoTemporalmente(Long id) {
    // 1. Verificar que existe (lanza ResourceNotFoundException si no existe)
    Alumno alumno = obtenerAlumnoOLanzarExcepcion(id);
    
    // 2. Verificar que está activo (lanza BusinessException si ya está inactivo)
    if (!alumno.isActivo()) {
        throw new BusinessException("El alumno con ID " + id + " ya está inactivo");
    }
    
    // 3. Inactivar
    alumno.setActivo(false);
    alumnoRepository.save(alumno);
};

    /**
     * Método auxiliar para validar que un alumno existe
     */
    private Alumno obtenerAlumnoOLanzarExcepcion(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", id));
    };

    /**
     * Método auxiliar para mapear DTO a entidad existente.
     * Evita duplicar código de actualización de campos.
     */
    private void actualizarCamposDesdeDto(Alumno alumnoExistente, Alumno alumnoNuevo) {
        alumnoExistente.setDni(alumnoNuevo.getDni());
        alumnoExistente.setNombre(alumnoNuevo.getNombre());
        alumnoExistente.setApellido(alumnoNuevo.getApellido());
        alumnoExistente.setFechaNacimiento(alumnoNuevo.getFechaNacimiento());
        alumnoExistente.setTelefono(alumnoNuevo.getTelefono());
        alumnoExistente.setEmail(alumnoNuevo.getEmail());
        alumnoExistente.setFechaInscripcion(alumnoNuevo.getFechaInscripcion());
        alumnoExistente.setCantidadClases(alumnoNuevo.getCantidadClases());
        alumnoExistente.setActivo(alumnoNuevo.isActivo());
        alumnoExistente.setPropietario(alumnoNuevo.isPropietario());
    }
}
