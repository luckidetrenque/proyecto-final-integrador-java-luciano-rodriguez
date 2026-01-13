package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.ClaseDto;
import com.escueladeequitacion.hrs.dto.ClaseResponseDto;
import com.escueladeequitacion.hrs.enums.Especialidad;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.model.Caballo;
import com.escueladeequitacion.hrs.model.Clase;
import com.escueladeequitacion.hrs.model.Instructor;
import com.escueladeequitacion.hrs.repository.ClaseRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
    };

    @Override
    public Optional<Clase> buscarClasePorId(Long id) {
        return claseRepository.findById(id);
    };

    @Override
    public List<Clase> buscarClasePorEspecialidad(Especialidad especialidad) {
        return claseRepository.findByEspecialidad(especialidad);
    };

    @Override
    public List<Clase> buscarClasePorDia(LocalDate dia) {
        return claseRepository.findByDia(dia);
    };

    @Override
    public List<Clase> buscarClasePorHora(LocalTime hora) {
        return claseRepository.findByHora(hora);
    };

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
    };

    @Override
    public Boolean existeClasePorEspecialidad(Especialidad especialidad) {
        return claseRepository.existsByEspecialidad(especialidad);
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
    public Boolean existeClasePorEstado(Estado estado) {
        return claseRepository.existsByEstado(estado);
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

    // BUSCA el método guardarClase y MODIFÍCALO para incluir la validación:

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
            // Definir la zona horaria
            ZoneId zona = ZoneId.of("America/Buenos_Aires");
            LocalTime ahora = LocalTime.now(zona);
            if (clase.getHora().isBefore(ahora.plusMinutes(60))) {
                throw new BusinessException("La clase debe programarse con al menos 60 minutos de anticipación");
            }
        }

        // ✅ AGREGAR ESTA VALIDACIÓN DE CONFLICTO DE HORARIO:
        validarConflictoDeHorario(
                clase.getId(),
                clase.getDia(),
                clase.getHora(),
                clase.getInstructor().getId(),
                clase.getAlumno().getId(),
                clase.getCaballo().getId());

        // Si todas las validaciones pasan, guardar
        claseRepository.save(clase);
    };

    // BUSCA el método actualizarClase y AGRÉGALE la validación:

    @Override
    public void actualizarClase(Long id, Clase clase) {
        // Obtener la clase a actualizar
        var claseOpt = claseRepository.findById(id);
        if (claseOpt.isEmpty()) {
            throw new ResourceNotFoundException("Clase", "ID", id);
        }

        var claseExistente = claseOpt.get();

        // Validar conflictos de horario si cambió día u hora
        if (clase.getDia() != null || clase.getHora() != null) {
            validarConflictoDeHorario(
                    claseExistente.getId(),
                    claseExistente.getDia(),
                    claseExistente.getHora(),
                    claseExistente.getInstructor().getId(),
                    claseExistente.getAlumno().getId(),
                    claseExistente.getCaballo().getId());
        }

        // ⚠️ Solo actualizar si vienen en el DTO (pueden ser null)
        if (clase.getInstructor() != null) {
            var instr = instructorService.buscarInstructorPorId(clase.getInstructor().getId());
            if (instr.isEmpty()) {
                throw new ResourceNotFoundException("Instructor", "ID", clase.getInstructor().getId());
            }
            claseExistente.setInstructor(instr.get());
        }

        if (clase.getAlumno() != null) {
            var alum = alumnoService.buscarAlumnoPorId(clase.getAlumno().getId());
            if (alum.isEmpty()) {
                throw new ResourceNotFoundException("Alumno", "ID", clase.getAlumno().getId());
            }
            claseExistente.setAlumno(alum.get());
        }

        if (clase.getCaballo() != null) {
            var cab = caballoService.buscarCaballoPorId(clase.getCaballo().getId());
            if (cab.isEmpty()) {
                throw new ResourceNotFoundException("Caballo", "ID", clase.getCaballo().getId());
            }
            claseExistente.setCaballo(cab.get());
        }

        // Actualizar campos simples solo si vienen
        if (clase.getEspecialidad() != null)
            claseExistente.setEspecialidad(clase.getEspecialidad());
        if (clase.getDia() != null)
            claseExistente.setDia(clase.getDia());
        if (clase.getHora() != null)
            claseExistente.setHora(clase.getHora());
        if (clase.getEstado() != null)
            claseExistente.setEstado(clase.getEstado());
        if (clase.getObservaciones() != null)
            claseExistente.setObservaciones(clase.getObservaciones());

        claseRepository.save(claseExistente);
    };

    @Override
    public void eliminarClase(Long id) {
        claseRepository.deleteById(id);
    };

    @Override
    public void eliminarClaseTemporalmente(Long id) {
        Optional<Clase> claseOpt = claseRepository.findById(id);
        if (claseOpt.isPresent()) {
            Clase clase = claseOpt.get();
            clase.setEstado(Estado.CANCELADA);
            claseRepository.save(clase);
        }
    };

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
    };

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
    };

    /**
     * Busca clases por día con detalles.
     */
    @Override
    public List<ClaseResponseDto> buscarClasePorDiaConDetalles(LocalDate dia) {
        return claseRepository.findByDiaWithDetails(dia)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    };

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
    };

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
    };

    /**
     * Busca clases por caballo con detalles.
     */
    @Override
    public List<ClaseResponseDto> buscarClasePorCaballoConDetalles(Long caballoId) {
        return claseRepository.findByCaballoIdWithDetails(caballoId)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    };

    /**
     * Busca clases por estado con detalles.
     */
    @Override
    public List<ClaseResponseDto> buscarClasePorEstadoConDetalles(Estado estado) {
        return claseRepository.findByEstadoWithDetails(estado)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    };

    /**
     * Valida que no exista conflicto de horario para instructor, alumno o caballo.
     * 
     * @throws BusinessException si hay conflicto de horario
     */
    private void validarConflictoDeHorario(Long id, LocalDate dia, LocalTime hora, Long instructorId, Long alumnoId,
            Long caballoId) {
        List<Clase> clasesEnMismoHorario = claseRepository.findByDiaAndHoraWithDetails(dia, hora);

        for (Clase clase : clasesEnMismoHorario) {
            // Excluir la clase que estamos editando
            if (clase.getId().equals(id)) {
                continue;
            }

            // Validar conflicto de alumno
            if (clase.getAlumno().getId().equals(alumnoId)) {
                throw new BusinessException("El alumno ya tiene una clase asignada a esa hora");
            }

            // Validar conflicto de caballo
            if (clase.getCaballo().getId().equals(caballoId)) {
                throw new BusinessException("El caballo ya está asignado a una clase en esa hora");
            }
        }

        // for (Clase clase : clasesEnMismoHorario) {
        // if (clase.getHora().equals(hora)) {
        // if (clase.getInstructor().getId().equals(instructorId)) {
        // throw new BusinessException("El instructor ya tiene una clase asignada a esa
        // hora");
        // }
        // if (clase.getAlumno().getId().equals(alumnoId)) {
        // throw new BusinessException("El alumno ya tiene una clase asignada a esa
        // hora");
        // }
        // if (clase.getCaballo().getId().equals(caballoId)) {
        // throw new BusinessException("El caballo ya está asignado a una clase en esa
        // hora");
        // }
        // }
        // }
    }

    /**
     * Crea una clase desde un DTO con todas las validaciones.
     */
    @Override
    public Clase crearClaseDesdeDto(ClaseDto claseDto) {
        // 1. Validar y obtener instructor
        Instructor instructor = instructorService.buscarInstructorPorId(claseDto.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "ID", claseDto.getInstructorId()));

        if (!instructor.isActivo()) {
            throw new BusinessException("El instructor " + instructor.getNombreCompleto() + " no está activo");
        }

        // 2. Validar y obtener alumno
        Alumno alumno = alumnoService.buscarAlumnoPorId(claseDto.getAlumnoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", claseDto.getAlumnoId()));

        if (!alumno.isActivo()) {
            throw new BusinessException("El alumno " + alumno.getNombreCompleto() + " no está activo");
        }

        // 3. Validar y obtener caballo
        Caballo caballo = caballoService.buscarCaballoPorId(claseDto.getCaballoId())
                .orElseThrow(() -> new ResourceNotFoundException("Caballo", "ID", claseDto.getCaballoId()));

        if (!caballo.isDisponible()) {
            throw new BusinessException("El caballo " + caballo.getNombre() + " no está disponible");
        }

        // 4. Validar fecha y hora
        validarFechaYHora(claseDto.getDia(), claseDto.getHora());

        // 5. Validar conflictos de horario
        validarConflictoDeHorario(
                null,
                claseDto.getDia(),
                claseDto.getHora(),
                instructor.getId(),
                alumno.getId(),
                caballo.getId());

        // 6. Crear la clase
        Clase clase = new Clase();
        clase.setEspecialidad(claseDto.getEspecialidad());
        clase.setDia(claseDto.getDia());
        clase.setHora(claseDto.getHora());
        clase.setEstado(claseDto.getEstado());
        clase.setObservaciones(claseDto.getObservaciones());
        clase.setInstructor(instructor);
        clase.setAlumno(alumno);
        clase.setCaballo(caballo);

        return claseRepository.save(clase);
    }

    /**
     * Actualiza una clase desde un DTO.
     */
    @Override
    public void actualizarClaseDesdeDto(Long id, ClaseDto claseDto) {
        // 1. Verificar que la clase existe
        Clase claseExistente = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase", "ID", id));

        // 2. Actualizar instructor si cambió
        if (claseDto.getInstructorId() != null) {
            Instructor instructor = instructorService.buscarInstructorPorId(claseDto.getInstructorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor", "ID", claseDto.getInstructorId()));

            if (!instructor.isActivo()) {
                throw new BusinessException("El instructor no está activo");
            }

            claseExistente.setInstructor(instructor);
        }

        // 3. Actualizar alumno si cambió
        if (claseDto.getAlumnoId() != null) {
            Alumno alumno = alumnoService.buscarAlumnoPorId(claseDto.getAlumnoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", claseDto.getAlumnoId()));

            if (!alumno.isActivo()) {
                throw new BusinessException("El alumno no está activo");
            }

            claseExistente.setAlumno(alumno);
        }

        // 4. Actualizar caballo si cambió
        if (claseDto.getCaballoId() != null) {
            Caballo caballo = caballoService.buscarCaballoPorId(claseDto.getCaballoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Caballo", "ID", claseDto.getCaballoId()));

            if (!caballo.isDisponible()) {
                throw new BusinessException("El caballo no está disponible");
            }

            claseExistente.setCaballo(caballo);
        }

        // 5. Actualizar campos simples
        if (claseDto.getEspecialidad() != null) {
            claseExistente.setEspecialidad(claseDto.getEspecialidad());
        }
        if (claseDto.getDia() != null) {
            claseExistente.setDia(claseDto.getDia());
        }
        if (claseDto.getHora() != null) {
            claseExistente.setHora(claseDto.getHora());
        }
        if (claseDto.getEstado() != null) {
            claseExistente.setEstado(claseDto.getEstado());
        }
        if (claseDto.getObservaciones() != null) {
            claseExistente.setObservaciones(claseDto.getObservaciones());
        }

        // 6. Validar conflictos si cambió día u hora
        if (claseDto.getDia() != null || claseDto.getHora() != null) {
            validarConflictoDeHorario(
                    claseExistente.getId(),
                    claseExistente.getDia(),
                    claseExistente.getHora(),
                    claseExistente.getInstructor().getId(),
                    claseExistente.getAlumno().getId(),
                    claseExistente.getCaballo().getId());
        }

        claseRepository.save(claseExistente);
    }

    /**
     * Método auxiliar para validar fecha y hora.
     */
    private void validarFechaYHora(LocalDate dia, LocalTime hora) {
        LocalDate hoy = LocalDate.now();

        if (dia.isBefore(hoy)) {
            throw new BusinessException("La fecha de la clase no puede ser anterior a hoy");
        }

        if (dia.isEqual(hoy)) {
            LocalTime ahora = LocalTime.now();
            if (hora.isBefore(ahora.plusMinutes(60))) {
                throw new BusinessException("La clase debe programarse con al menos 60 minutos de anticipación");
            }
        }
    }

    /**
     * Cuenta clases completadas por alumno.
     */
    @Override
    public long contarClasesCompletadasPorAlumno(Long alumnoId) {
        return claseRepository.contarPorAlumnoYEstado(alumnoId, Estado.COMPLETADA);
    }

    /**
     * Cuenta clases completadas por instructor.
     */
    @Override
    public long contarClasesCompletadasPorInstructor(Long instructorId) {
        return claseRepository.contarPorInstructorYEstado(instructorId, Estado.COMPLETADA);
    }

    /**
     * Cuenta clases completadas por caballo.
     */
    @Override
    public long contarClasesCompletadasPorCaballo(Long caballoId) {
        return claseRepository.contarPorCaballoYEstado(caballoId, Estado.COMPLETADA);
    }

    // Método validarConflictoDeHorario ya existe (lo agregaste antes)
}