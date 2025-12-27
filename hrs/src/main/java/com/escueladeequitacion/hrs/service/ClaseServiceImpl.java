package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.ClaseResponseDto;
import com.escueladeequitacion.hrs.enums.Especialidades;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Clase;
import com.escueladeequitacion.hrs.repository.ClaseRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementación de la interfaz ClaseService
@Service
@Transactional
public class ClaseServiceImpl implements ClaseService {

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    AlumnoService alumnoService;

    @Autowired
    InstructorService instructorService;

    @Autowired
    CaballoService caballoService;

    @Override
    public List<Clase> listarClases() {
        return claseRepository.findAll();
    }

    @Override
    public Optional<Clase> buscarClasePorId(Long id) {
        return claseRepository.findById(id);
    }

    @Override
    public List<Clase> buscarClasePorEspecialidad(Especialidades especialidad) {
        return claseRepository.findByEspecialidades(especialidad);
    }

    @Override
    public List<Clase> buscarClasePorDia(LocalDate dia) {
        return claseRepository.findByDia(dia);
    }

    @Override
    public List<Clase> buscarClasePorHora(LocalTime hora) {
        return claseRepository.findByHora(hora);
    }

    @Override
    public List<Clase> buscarClasePorEstado(Estado estado) {
        return claseRepository.findByEstado(estado);
    };

    @Override
    public List<Clase> buscarClasePorInstructor(Long instructor_id) {
        return claseRepository.findByInstructorId(instructor_id);
    };

    @Override
    public List<Clase> buscarClasePorAlumno(Long alumno_id) {
        return claseRepository.findByAlumnoId(alumno_id);
    };

    @Override
    public List<Clase> buscarClasePorCaballo(Long caballo_id) {
        return claseRepository.findByCaballoId(caballo_id);
    };

    @Override
    public Boolean existeClasePorId(Long id) {
        return claseRepository.existsById(id);
    }

    @Override
    public Boolean existeClasePorEspecialidades(Especialidades especialidades) {
        return claseRepository.existsByEspecialidades(especialidades);
    };

    @Override
    public Boolean existeClasePorDia(LocalDate dia) {
        return claseRepository.existsByDia(dia);
    };

    @Override
    public Boolean existeClasePorHora(LocalTime hora) {
        return claseRepository.existsByHora(hora);
    };

    @Override
    public Boolean existeClasePorInstructor(Long instructor_id) {
        return claseRepository.existsByInstructorId(instructor_id);
    };

    @Override
    public Boolean existeClasePorAlumno(Long alumno_id) {
        return claseRepository.existsByAlumnoId(alumno_id);
    };

    @Override
    public Boolean existeClasePorCaballo(Long caballo_id) {
        return claseRepository.existsByCaballoId(caballo_id);
    };

    @Override
    public void guardarClase(Clase clase) {
        // Validar que instructor existe y está activo
        var instructor = instructorService.buscarInstructorPorId(clase.getInstructor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "ID", clase.getInstructor().getId()));

        if (!instructor.isActivo()) {
            throw new BusinessException("El instructor " + instructor.getNombreCompleto() + " no está activo");
        }

        // Validar que alumno existe y está activo
        var alumno = alumnoService.buscarAlumnoPorId(clase.getAlumno().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", clase.getAlumno().getId()));

        if (!alumno.isActivo()) {
            throw new BusinessException("El alumno " + alumno.getNombreCompleto() + " no está activo");
        }

        // Validar que caballo existe y está disponible
        var caballo = caballoService.buscarCaballoPorId(clase.getCaballo().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Caballo", "ID", clase.getCaballo().getId()));

        if (!caballo.isDisponible()) {
            throw new BusinessException("El caballo " + caballo.getNombre() + " no está disponible");
        }

        // Validar fecha y hora
        LocalDate hoy = LocalDate.now();
        if (clase.getDia().isBefore(hoy)) {
            throw new BusinessException("La fecha de la clase no puede ser anterior a hoy");
        }

        if (clase.getDia().isEqual(hoy)) {
            LocalTime ahora = LocalTime.now();
            if (clase.getHora().isBefore(ahora.plusMinutes(60))) {
                throw new BusinessException("La clase debe programarse con al menos 60 minutos de anticipación");
            }
        }

        // Si todas las validaciones pasan, guardar
        claseRepository.save(clase);
    }

    @Override
    public void actualizarClase(Long id, Clase clase) {
        clase.setId(id);
        claseRepository.save(clase);
    };

    @Override
    public void eliminarClase(Long id) {
        claseRepository.deleteById(id);
    }

    @Override
    public void eliminarClaseTemporalmente(Long id) {
        Optional<Clase> claseOpt = claseRepository.findById(id);
        if (claseOpt.isPresent()) {
            Clase clase = claseOpt.get();
            clase.setEstado(Estado.CANCELADA);
            claseRepository.save(clase);
        }
    }

    // ============================================================
    // IMPLEMENTACIÓN DE MÉTODOS NUEVOS CON DETALLES
    // ============================================================

    /**
     * Lista todas las clases con sus relaciones cargadas y las convierte a DTO.
     * 
     * Flujo:
     * 1. Llama a findAllWithDetails() que hace JOIN FETCH
     * 2. Convierte cada Clase a ClaseResponseDto usando .stream()
     * 3. Retorna la lista de DTOs
     */
    @Override
    public List<ClaseResponseDto> listarClasesConDetalles() {
        return claseRepository.findAllWithDetails()
                .stream()
                .map(ClaseResponseDto::new) // Constructor reference
                .collect(Collectors.toList());
    }

    /**
     * Busca una clase por ID con detalles.
     * 
     * @param id - ID de la clase
     * @return Optional vacío si no existe, o Optional con ClaseResponseDto
     */
    @Override
    public Optional<ClaseResponseDto> buscarClasePorIdConDetalles(Long id) {
        return claseRepository.findByIdWithDetails(id)
                .map(ClaseResponseDto::new);
    }

    /**
     * Busca clases por día con detalles.
     */
    @Override
    public List<ClaseResponseDto> buscarClasePorDiaConDetalles(LocalDate dia) {
        return claseRepository.findByDiaWithDetails(dia)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca clases por instructor con detalles.
     * Útil para el endpoint: GET /api/v1/clases/instructor/{id}/detalles
     */
    @Override
    public List<ClaseResponseDto> buscarClasePorInstructorConDetalles(Long instructorId) {
        return claseRepository.findByInstructorIdWithDetails(instructorId)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca clases por alumno con detalles.
     * Útil para el endpoint: GET /api/v1/clases/alumno/{id}/detalles
     */
    @Override
    public List<ClaseResponseDto> buscarClasePorAlumnoConDetalles(Long alumnoId) {
        return claseRepository.findByAlumnoIdWithDetails(alumnoId)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca clases por caballo con detalles.
     */
    @Override
    public List<ClaseResponseDto> buscarClasePorCaballoConDetalles(Long caballoId) {
        return claseRepository.findByCaballoIdWithDetails(caballoId)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca clases por estado con detalles.
     */
    @Override
    public List<ClaseResponseDto> buscarClasePorEstadoConDetalles(Estado estado) {
        return claseRepository.findByEstadoWithDetails(estado)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    }

}