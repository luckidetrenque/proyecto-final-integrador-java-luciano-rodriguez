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
import com.escueladeequitacion.hrs.model.PersonaPrueba;
import com.escueladeequitacion.hrs.repository.ClaseRepository;
import com.escueladeequitacion.hrs.repository.PersonaPruebaRepository;

import jakarta.transaction.Transactional;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementación de la interfaz ClaseService
@Service
@Transactional
public class ClaseServiceImpl implements ClaseService {

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    PersonaPruebaRepository personaPruebaRepository;

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
     * Crea una clase desde un DTO con todas las validaciones.
     */
    @Override
    public Clase crearClase(ClaseDto claseDto) {
        /**
         * TODO Validar para el caso de MONTA
         * TODO crear alumnno comodin para que sea por defecto cuando se selecciona
         * MONTA
         */

        Instructor instructor = obtenerInstructorValido(claseDto.getInstructorId());

        // ⚠️ CAMBIO AQUÍ: Usar método sin validar estado si es clase de prueba
        Alumno alumno = null;
        PersonaPrueba personaPrueba = null;

        if (claseDto.isEsPrueba() != null && claseDto.isEsPrueba()) {
            if (claseDto.getPersonaPruebaId() != null) {
                // Prueba de persona nueva (sin cuenta de alumno)
                personaPrueba = personaPruebaRepository.findById(claseDto.getPersonaPruebaId())
                        .orElseThrow(() -> new ResourceNotFoundException("PersonaPrueba", "ID",
                                claseDto.getPersonaPruebaId()));
            } else if (claseDto.getAlumnoId() != null) {
                // Prueba de alumno existente en otra especialidad
                alumno = obtenerAlumnoSinValidarEstado(claseDto.getAlumnoId());
            } else {
                throw new BusinessException("Una clase de prueba requiere alumnoId o personaPruebaId");
            }
        } else {
            if (claseDto.getAlumnoId() == null) {
                throw new BusinessException("Una clase regular requiere alumnoId");
            }
            alumno = obtenerAlumnoValido(claseDto.getAlumnoId());
        }

        Caballo caballo = obtenerCaballoValido(claseDto.getCaballoId());

        if ((claseDto.getEstado() == Estado.PROGRAMADA || claseDto.getEstado() == Estado.INICIADA)) {
            validarFechaYHora(claseDto.getDia(), claseDto.getHora());
        }

        validarDuracionSegunHorario(claseDto.getHora(), claseDto.getDuracion());

        Long alumnoIdParaConflicto = alumno != null ? alumno.getId() : null;
        validarConflictoDeHorario(
                null,
                claseDto.getDia(),
                claseDto.getHora(),
                alumnoIdParaConflicto,
                caballo.getId());

        Clase clase = new Clase();
        aplicarCamposSimples(clase, claseDto);
        clase.setInstructor(instructor);
        clase.setAlumno(alumno);
        clase.setPersonaPrueba(personaPrueba);
        clase.setCaballo(caballo);

        // Validar reglas de clase de prueba
        if (claseDto.isEsPrueba() != null && claseDto.isEsPrueba()) {
            // Verificar que el alumno no haya tomado clase de prueba DE ESTA ESPECIALIDAD
            // Por esto:
            if (claseDto.isEsPrueba() != null && claseDto.isEsPrueba() && alumno != null) {
                if (claseRepository.alumnoTieneClaseDePruebaEnEspecialidad(
                        alumno.getId(),
                        claseDto.getEspecialidad())) {
                    throw new BusinessException(
                            "El alumno ya ha tomado una clase de prueba de " + claseDto.getEspecialidad());
                }
            }
        }
        clase.setEsPrueba(claseDto.isEsPrueba() != null ? claseDto.isEsPrueba() : false);

        return claseRepository.save(clase);
    }

    /**
     * Actualiza una clase desde un DTO.
     */
    @Override
    public void actualizarClase(Long id, ClaseDto dto) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase", "ID", id));

        // ✅ OPCIONAL: Prevenir cambio de tipo de clase
        if (dto.isEsPrueba() != null && !dto.isEsPrueba().equals(clase.isEsPrueba())) {
            throw new BusinessException("No se puede cambiar el tipo de clase (regular ↔ prueba)");
        }

        if (dto.getInstructorId() != null) {
            clase.setInstructor(obtenerInstructorValido(dto.getInstructorId()));
        }

        if (clase.isEsPrueba()) {
            if (dto.getPersonaPruebaId() != null) {
                // Cambiar a personaPrueba → limpiar alumno
                PersonaPrueba pp = personaPruebaRepository.findById(dto.getPersonaPruebaId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("PersonaPrueba", "ID", dto.getPersonaPruebaId()));
                clase.setPersonaPrueba(pp);
                clase.setAlumno(null);
            } else if (dto.getAlumnoId() != null) {
                // Cambiar a alumno existente → limpiar personaPrueba
                clase.setAlumno(obtenerAlumnoSinValidarEstado(dto.getAlumnoId()));
                clase.setPersonaPrueba(null);
            }
        } else {
            if (dto.getAlumnoId() != null) {
                clase.setAlumno(obtenerAlumnoValido(dto.getAlumnoId()));
            }
        }

        if (dto.getCaballoId() != null) {
            clase.setCaballo(obtenerCaballoValido(dto.getCaballoId()));
        }

        if ((dto.getDia() != null || dto.getHora() != null) &&
                clase.getDia().isAfter(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")))) {
            validarFechaYHora(dto.getDia(), dto.getHora());
            validarDuracionSegunHorario(
                    dto.getHora() != null ? dto.getHora() : clase.getHora(),
                    dto.getDuracion() != null ? dto.getDuracion() : clase.getDuracion());
        }

        aplicarCamposSimples(clase, dto);

        Long alumnoIdConflicto = clase.getAlumno() != null ? clase.getAlumno().getId() : null;
        validarConflictoDeHorario(
                clase.getId(),
                clase.getDia(),
                clase.getHora(),
                alumnoIdConflicto,
                clase.getCaballo().getId());

        claseRepository.save(clase);
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

    /**
     * Tarea programada que se ejecuta cada minuto para actualizar estados de
     * clases.
     * - Cambia de PROGRAMADA a INICIADA cuando llega la hora de inicio
     * - Cambia de INICIADA a COMPLETADA cuando transcurren 60 minutos
     */
    @Scheduled(cron = "0 0/30 9-18 * * TUE-SAT", zone = "America/Argentina/Buenos_Aires")
    public void actualizarEstadosDeClases() {
        ZoneId zona = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDate hoy = LocalDate.now(zona);
        LocalTime ahora = LocalTime.now(zona);

        // 1. Cambiar clases PROGRAMADA → INICIADA (cuando llega la hora de inicio)
        List<Clase> clasesPorIniciar = claseRepository.findByDiaAndEstado(hoy, Estado.PROGRAMADA);

        for (Clase clase : clasesPorIniciar) {
            if (!clase.getHora().isAfter(ahora)) {
                clase.setEstado(Estado.INICIADA);
                claseRepository.save(clase);
            }
        }

        // 2. Cambiar clases INICIADA → COMPLETADA (cuando transcurren 60 minutos)
        List<Clase> clasesEnCurso = claseRepository.findByDiaAndEstado(hoy, Estado.INICIADA);

        for (Clase clase : clasesEnCurso) {
            LocalTime horaFinalizacion = clase.getHora().plusMinutes(60);
            if (!horaFinalizacion.isAfter(ahora)) {
                clase.setEstado(Estado.COMPLETADA);
                claseRepository.save(clase);
            }
        }
    }

    /**
     * Busca clases con múltiples filtros.
     */
    @Override
    public List<Clase> buscarClasesConFiltros(LocalDate dia, LocalTime hora, Long alumnoId, Long instructorId,
            Long caballoId, Especialidad especialidad, Estado estado) {
        return claseRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dia != null)
                predicates.add(cb.equal(root.get("dia"), dia));
            if (hora != null)
                predicates.add(cb.equal(root.get("hora"), hora));
            if (alumnoId != null)
                predicates.add(cb.equal(root.get("alumno").get("id"), alumnoId));
            if (instructorId != null)
                predicates.add(cb.equal(root.get("instructor").get("id"), instructorId));
            if (caballoId != null)
                predicates.add(cb.equal(root.get("caballo").get("id"), caballoId));
            if (especialidad != null)
                predicates.add(cb.equal(root.get("especialidad"), especialidad));
            if (estado != null)
                predicates.add(cb.equal(root.get("estado"), estado));

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    /**
     * Método auxiliar para validar fecha y hora.
     */
    private void validarFechaYHora(LocalDate dia, LocalTime hora) {
        ZoneId zonaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDate hoy = LocalDate.now(zonaArgentina);

        if (dia.isBefore(hoy)) {
            throw new BusinessException("La fecha de la clase no puede ser anterior a hoy");
        }

        if (dia.isEqual(hoy)) {
            LocalTime ahora = LocalTime.now(zonaArgentina);
            if (hora.isBefore(ahora.plusMinutes(60))) {
                throw new BusinessException("La clase debe programarse con al menos 60 minutos de anticipación");
            }
        }
    }

    /**
     * Método auxiliar para validar duración según horario.
     * Solo se permite duración de 30 minutos a las 18:00 o después.
     * 
     * @throws BusinessException si hay conflicto de horario
     */
    private void validarDuracionSegunHorario(LocalTime hora, Integer duracion) {
        LocalTime horaFin = hora.plusMinutes(duracion != null ? duracion : 30);
        LocalTime horaLimite = LocalTime.of(18, 30);
        if (horaFin.isAfter(horaLimite)) {
            throw new BusinessException(
                    "La clase no puede terminar después de las 18:30. " +
                            "A las " + hora + " solo se permite duración de 30 minutos.");
        }
    }

    /**
     * Valida que no exista conflicto de horario para instructor, alumno o caballo.
     * 
     * @throws BusinessException si hay conflicto de horario
     */
    private void validarConflictoDeHorario(
            Long id,
            LocalDate dia,
            LocalTime hora,
            Long alumnoId,
            Long caballoId) {

        if (claseRepository.existsConflictoAlumno(id, dia, hora, alumnoId)) {
            throw new BusinessException(
                    "El alumno ya tiene una clase asignada a esa hora");
        }

        if (claseRepository.existsConflictoCaballo(id, dia, hora, caballoId)) {
            throw new BusinessException(
                    "El caballo ya está asignado a una clase en esa hora");
        }
    }

    private Alumno obtenerAlumnoSinValidarEstado(Long id) {
        return alumnoService.buscarAlumnoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", id));
    }

    private Alumno obtenerAlumnoValido(Long id) {
        Alumno alumno = alumnoService.buscarAlumnoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", id));

        if (!alumno.isActivo()) {
            throw new BusinessException("El alumno no está activo");
        }

        return alumno;
    }

    private Caballo obtenerCaballoValido(Long id) {
        Caballo caballo = caballoService.buscarCaballoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caballo", "ID", id));

        if (!caballo.isDisponible()) {
            throw new BusinessException("El caballo no está disponible");
        }

        return caballo;
    }

    private Instructor obtenerInstructorValido(Long id) {
        Instructor instructor = instructorService.buscarInstructorPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "ID", id));

        if (!instructor.isActivo()) {
            throw new BusinessException("El instructor no está activo");
        }

        return instructor;
    }

    private void aplicarCamposSimples(Clase clase, ClaseDto dto) {

        if (dto.getEspecialidad() != null)
            clase.setEspecialidad(dto.getEspecialidad());

        if (dto.getDia() != null)
            clase.setDia(dto.getDia());

        if (dto.getHora() != null)
            clase.setHora(dto.getHora());

        if (dto.getDuracion() != null)
            clase.setDuracion(dto.getDuracion());

        if (dto.getEstado() != null)
            clase.setEstado(dto.getEstado());

        if (dto.getObservaciones() != null)
            clase.setObservaciones(dto.getObservaciones());
    }

    // ============================================================
    // MÉTODOS PARA CLASES DE PRUEBA
    // ============================================================

    /**
     * Verifica si un alumno ya tomó al menos una clase de prueba.
     */
    @Override
    public boolean alumnoTieneClaseDePrueba(Long alumnoId) {
        return claseRepository.alumnoTieneClaseDePrueba(alumnoId);
    }

    /**
     * Cuenta cuántas clases de prueba ha tomado un alumno.
     */
    @Override
    public long contarClasesDePruebaPorAlumno(Long alumnoId) {
        return claseRepository.contarClasesDePruebaPorAlumno(alumnoId);
    }

    /**
     * Obtiene información completa sobre clases de prueba de un alumno.
     * Método auxiliar que encapsula la lógica de verificación.
     */
    @Override
    public Map<String, Object> obtenerInfoClasesDePrueba(Long alumnoId) {
        // Validar que el alumno existe
        if (!alumnoService.existeAlumnoPorId(alumnoId)) {
            throw new ResourceNotFoundException("Alumno", "ID", alumnoId);
        }

        boolean tienePrueba = alumnoTieneClaseDePrueba(alumnoId);
        long cantidad = contarClasesDePruebaPorAlumno(alumnoId);

        Map<String, Object> info = new HashMap<>();
        info.put("alumnoId", alumnoId);
        info.put("tienePrueba", tienePrueba);
        info.put("cantidadClasesPrueba", cantidad);

        return info;
    }

    /**
     * Lista las clases de prueba de un alumno con detalles.
     */
    @Override
    public List<ClaseResponseDto> listarClasesDePruebaPorAlumno(Long alumnoId) {
        // Validar que el alumno existe
        if (!alumnoService.existeAlumnoPorId(alumnoId)) {
            throw new ResourceNotFoundException("Alumno", "ID", alumnoId);
        }

        return claseRepository.findClasesDePruebaPorAlumno(alumnoId)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas las clases de prueba del sistema con detalles.
     */
    @Override
    public List<ClaseResponseDto> listarTodasLasClasesDePrueba() {
        return claseRepository.findByEsPrueba(true)
                .stream()
                .map(ClaseResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un alumno ya tomó clase de prueba de una especialidad específica.
     */
    @Override
    public boolean alumnoTieneClaseDePruebaEnEspecialidad(Long alumnoId, String especialidad) {
        Especialidad esp = Especialidad.valueOf(especialidad);
        return claseRepository.alumnoTieneClaseDePruebaEnEspecialidad(alumnoId, esp);
    }

    /**
     * Cambia solo el estado de una clase, sin validaciones de negocio.
     * Usado por el endpoint PATCH /clases/{id}/estado
     */
    @Override
    public ClaseResponseDto cambiarEstado(Long id, Estado nuevoEstado) {
        Clase clase = claseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase", "ID", id));
        clase.setEstado(nuevoEstado);
        claseRepository.save(clase);
        return new ClaseResponseDto(clase);
    }

}