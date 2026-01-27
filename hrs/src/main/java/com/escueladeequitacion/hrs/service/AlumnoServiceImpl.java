package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.AlumnoDto;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ConflictException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.exception.ValidationException;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.repository.AlumnoRepository;
import com.escueladeequitacion.hrs.utility.Constantes;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Optional<Alumno> buscarAlumnoPorDni(String dni) {
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
    public Boolean existeAlumnoPorDni(String dni) {
        return alumnoRepository.existsByDni(dni);
    };

    @Override
    public Boolean existeAlumnoPorNombreYApellido(String nombre, String apellido) {
        return alumnoRepository.existsByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public long contarClasesCompletadas(Long alumnoId) {
        return alumnoRepository.contarClasesPorEstado(alumnoId, Estado.COMPLETADA);
    }

    @Override
    public long contarClasesARecuperar(Long alumnoId, List<Estado> estados) {
        return alumnoRepository.contarClasesPorEstados(alumnoId, List.of(Estado.CANCELADA, Estado.ACA));
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
     * Crea un alumno desde un DTO con todas las validaciones.
     */
    @Override
    public Alumno crearAlumnoDesdeDto(AlumnoDto alumnoDto) {
        // 1. Validar DNI duplicado
        if (alumnoRepository.existsByDni(alumnoDto.getDni())) {
            throw new ConflictException("Alumno", "DNI", alumnoDto.getDni());
        }

        // 2. Validar cantidad de clases
        int cantidadClases = alumnoDto.getCantidadClases();
        if (!Arrays.asList(Constantes.CANTIDAD_CLASES).contains(cantidadClases)) {
            throw new ValidationException("cantidadClases", "La cantidad de clases debe ser 4, 8, 12 o 16");
        }

        // 3. Crear el alumno
        Alumno alumno = new Alumno(
                alumnoDto.getDni(),
                alumnoDto.getNombre(),
                alumnoDto.getApellido(),
                alumnoDto.getFechaNacimiento(),
                alumnoDto.getTelefono(),
                alumnoDto.getEmail(),
                alumnoDto.getFechaInscripcion(),
                alumnoDto.getCantidadClases(),
                alumnoDto.isActivo(),
                alumnoDto.isPropietario());

        return alumnoRepository.save(alumno);
    }

    /**
     * Actualiza un alumno desde un DTO con validaciones.
     */
    @Override
    public void actualizarAlumnoDesdeDto(Long id, AlumnoDto alumnoDto) {
        // 1. Obtener alumno existente
        Alumno alumnoExistente = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", id));

        // 2. Validar DNI duplicado solo si cambió
        if (!alumnoExistente.getDni().equals(alumnoDto.getDni())) {
            if (alumnoRepository.existsByDni(alumnoDto.getDni())) {
                throw new ConflictException("Ya existe otro alumno con el DNI " + alumnoDto.getDni());
            }
        }

        // 3. Validar cantidad de clases
        if (!Arrays.asList(Constantes.CANTIDAD_CLASES).contains(alumnoDto.getCantidadClases())) {
            throw new ValidationException("cantidadClases", "La cantidad de clases debe ser 4, 8, 12 o 16");
        }

        // 4. Crear objeto temporal con los nuevos datos
        Alumno alumnoNuevo = new Alumno();
        alumnoNuevo.setDni(alumnoDto.getDni());
        alumnoNuevo.setNombre(alumnoDto.getNombre());
        alumnoNuevo.setApellido(alumnoDto.getApellido());
        alumnoNuevo.setFechaNacimiento(alumnoDto.getFechaNacimiento());
        alumnoNuevo.setTelefono(alumnoDto.getTelefono());
        alumnoNuevo.setEmail(alumnoDto.getEmail());
        alumnoNuevo.setFechaInscripcion(alumnoDto.getFechaInscripcion());
        alumnoNuevo.setCantidadClases(alumnoDto.getCantidadClases());
        alumnoNuevo.setActivo(alumnoDto.isActivo());
        alumnoNuevo.setPropietario(alumnoDto.isPropietario());

        // 5. Actualizar campos usando método auxiliar
        actualizarCamposDesdeDto(alumnoExistente, alumnoNuevo);

        // 6. Guardar
        alumnoRepository.save(alumnoExistente);
    }

    /**
     * Busca alumnos con múltiples filtros.
     */
    @Override
    public List<Alumno> buscarAlumnosConFiltros(String nombre, String apellido, Boolean activo,
            Boolean propietario, LocalDate fechaInscripcion,
            LocalDate fechaNacimiento) {
        // Aplicar filtros en orden de especificidad
        if (nombre != null && apellido != null) {
            return alumnoRepository.findByNombreAndApellidoIgnoreCase(nombre, apellido);
        }

        if (nombre != null) {
            return alumnoRepository.findByNombreIgnoreCase(nombre);
        }

        if (apellido != null) {
            return alumnoRepository.findByApellidoIgnoreCase(apellido);
        }

        if (activo != null) {
            return alumnoRepository.findByActivo(activo);
        }

        if (propietario != null) {
            return alumnoRepository.findByPropietario(propietario);
        }

        if (fechaInscripcion != null) {
            return alumnoRepository.findByFechaInscripcion(fechaInscripcion);
        }

        if (fechaNacimiento != null) {
            return alumnoRepository.findByFechaNacimiento(fechaNacimiento);
        }

        // Si no hay filtros, retornar lista vacía (el controller devuelve todos)
        return new ArrayList<>();
    }

    /**
     * Método auxiliar para validar existencia (ya lo tienes).
     */
    private Alumno obtenerAlumnoOLanzarExcepcion(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", id));
    }

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
