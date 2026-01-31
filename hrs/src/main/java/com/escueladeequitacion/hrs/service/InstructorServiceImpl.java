package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.InstructorDto;
import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ConflictException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Instructor;
import com.escueladeequitacion.hrs.repository.InstructorRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Implementación de la interfaz InstructorService
@Service
@Transactional
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    InstructorRepository instructorRepository;

    @Override
    public List<Instructor> listarInstructores() {
        return instructorRepository.findAll();
    };

    @Override
    public Optional<Instructor> buscarInstructorPorId(Long id) {
        return instructorRepository.findById(id);
    };

    @Override
    public Optional<Instructor> buscarInstructorPorDni(String dni) {
        return instructorRepository.findByDni(dni);
    };

    @Override
    public Optional<Instructor> buscarInstructorPorColor(String color) {
        return instructorRepository.findByColor(color);
    };

    @Override
    public List<Instructor> buscarInstructorPorNombre(String nombre) {
        return instructorRepository.findByNombreIgnoreCase(nombre);
    };

    @Override
    public List<Instructor> buscarInstructorPorApellido(String apellido) {
        return instructorRepository.findByApellidoIgnoreCase(apellido);
    };

    @Override
    public List<Instructor> buscarInstructorPorNombreYApellido(String nombre, String apellido) {
        return instructorRepository.findByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public List<Instructor> buscarInstructorsPorEstado(Boolean activo) {
        return instructorRepository.findByActivo(activo);
    };

    @Override
    public List<Instructor> buscarPorFechaNacimiento(LocalDate fechaNacimiento) {
        return instructorRepository.findByFechaNacimiento(fechaNacimiento);
    };

    @Override
    public Boolean existeInstructorPorId(Long id) {
        return instructorRepository.existsById(id);
    };

    @Override
    public Boolean existeInstructorPorDni(String dni) {
        return instructorRepository.existsByDni(dni);
    };

    @Override
    public Boolean existeInstructorPorNombreYApellido(String nombre, String apellido) {
        return instructorRepository.existsByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public Boolean estadoInstructor(Long id) {
        Optional<Instructor> instructor = instructorRepository.findById(id);
        return instructor.map(Instructor::isActivo).orElse(false);
    };

    @Override
    public void guardarInstructor(Instructor instructor) {
        // Validar DNI duplicado solo al CREAR (cuando id es null)
        if (instructor.getId() == null) {
            if (instructorRepository.existsByDni(instructor.getDni())) {
                throw new ConflictException("Instructor", "DNI", instructor.getDni());
            }
        }

        instructorRepository.save(instructor);
    };

    @Override
    public void actualizarInstructor(Long id, Instructor instructor) {
        // 1. Verificar que el instructor existe
        Instructor instructorExistente = obtenerInstructorOLanzarExcepcion(id);

        // 2. Si el DNI cambió, verificar que el nuevo DNI no esté en uso
        if (!instructorExistente.getDni().equals(instructor.getDni())) {
            Optional<Instructor> instructorConNuevoDni = instructorRepository.findByDni(instructor.getDni());
            if (instructorConNuevoDni.isPresent()) {
                throw new ConflictException("Ya existe otro instructor con el DNI " + instructor.getDni());
            }
        }

        // 3. Actualizar los campos
        actualizarCamposDesdeDto(instructorExistente, instructor);

        // 4. Guardar
        instructorRepository.save(instructorExistente);
    }

    @Override
    public void eliminarInstructor(Long id) {
        obtenerInstructorOLanzarExcepcion(id);
        instructorRepository.deleteById(id);
    };

    @Override
    public void eliminarInstructorTemporalmente(Long id) {
        // 1. Verificar que existe
        Instructor instructor = obtenerInstructorOLanzarExcepcion(id);

        // 2. Verificar que está activo
        if (!instructor.isActivo()) {
            throw new BusinessException("El instructor con ID " + id + " ya está inactivo");
        }

        // 3. Inactivar
        instructor.setActivo(false);
        instructorRepository.save(instructor);
    };

    /**
     * Crea un instructor desde un DTO con validaciones.
     */
    @Override
    public Instructor crearInstructorDesdeDto(InstructorDto instructorDto) {
        // Validar DNI duplicado
        if (instructorRepository.existsByDni(instructorDto.getDni())) {
            throw new ConflictException("Instructor", "DNI", instructorDto.getDni());
        }

        // Crear el instructor
        Instructor instructor = new Instructor(
                instructorDto.getDni(),
                instructorDto.getNombre(),
                instructorDto.getApellido(),
                instructorDto.getFechaNacimiento(),
                instructorDto.getTelefono(),
                instructorDto.getEmail(),
                instructorDto.isActivo(),
                instructorDto.getColor());

        return instructorRepository.save(instructor);
    }

    /**
     * Actualiza un instructor desde un DTO con validaciones.
     */
    @Override
    public void actualizarInstructorDesdeDto(Long id, InstructorDto instructorDto) {
        // 1. Obtener instructor existente
        Instructor instructorExistente = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "ID", id));

        // 2. Validar DNI duplicado solo si cambió
        if (!instructorExistente.getDni().equals(instructorDto.getDni())) {
            if (instructorRepository.existsByDni(instructorDto.getDni())) {
                throw new ConflictException("Ya existe otro instructor con el DNI " + instructorDto.getDni());
            }
        }

        // 3. Crear objeto temporal
        Instructor instructorNuevo = new Instructor();
        instructorNuevo.setDni(instructorDto.getDni());
        instructorNuevo.setNombre(instructorDto.getNombre());
        instructorNuevo.setApellido(instructorDto.getApellido());
        instructorNuevo.setFechaNacimiento(instructorDto.getFechaNacimiento());
        instructorNuevo.setTelefono(instructorDto.getTelefono());
        instructorNuevo.setEmail(instructorDto.getEmail());
        instructorNuevo.setActivo(instructorDto.isActivo());
        instructorNuevo.setColor(instructorDto.getColor());

        // 4. Actualizar usando método auxiliar
        actualizarCamposDesdeDto(instructorExistente, instructorNuevo);

        // 5. Guardar
        instructorRepository.save(instructorExistente);
    }

    /**
     * Busca instructores con múltiples filtros.
     */
    @Override
    public List<Instructor> buscarInstructoresConFiltros(String nombre, String apellido,
            Boolean activo, LocalDate fechaNacimiento) {
        if (nombre != null && apellido != null) {
            return instructorRepository.findByNombreAndApellidoIgnoreCase(nombre, apellido);
        }

        if (nombre != null) {
            return instructorRepository.findByNombreIgnoreCase(nombre);
        }

        if (apellido != null) {
            return instructorRepository.findByApellidoIgnoreCase(apellido);
        }

        if (activo != null) {
            return instructorRepository.findByActivo(activo);
        }

        if (fechaNacimiento != null) {
            return instructorRepository.findByFechaNacimiento(fechaNacimiento);
        }

        return new ArrayList<>();
    }

    /**
     * Método auxiliar para validar que un instructor existe
     */
    private Instructor obtenerInstructorOLanzarExcepcion(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "ID", id));
    }

    /**
     * Método auxiliar para mapear DTO a entidad existente.
     * Evita duplicar código de actualización de campos.
     */
    private void actualizarCamposDesdeDto(Instructor instructorExistente, Instructor instructorNuevo) {
        instructorExistente.setDni(instructorNuevo.getDni());
        instructorExistente.setNombre(instructorNuevo.getNombre());
        instructorExistente.setApellido(instructorNuevo.getApellido());
        instructorExistente.setFechaNacimiento(instructorNuevo.getFechaNacimiento());
        instructorExistente.setTelefono(instructorNuevo.getTelefono());
        instructorExistente.setEmail(instructorNuevo.getEmail());
        instructorExistente.setActivo(instructorNuevo.isActivo());
        instructorExistente.setColor(instructorNuevo.getColor());
    }
}
