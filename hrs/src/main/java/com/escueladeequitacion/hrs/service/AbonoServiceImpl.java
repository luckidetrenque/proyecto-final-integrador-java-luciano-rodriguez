package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.enums.TipoPension;
import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Abono;
import com.escueladeequitacion.hrs.enums.EstadoAbono;
import com.escueladeequitacion.hrs.dto.ConceptoPrecioDto;
import com.escueladeequitacion.hrs.dto.InscripcionRequestDto;
import com.escueladeequitacion.hrs.dto.PrecioCalculadoDto;
import com.escueladeequitacion.hrs.repository.AbonoRepository;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.model.Caballo;
import com.escueladeequitacion.hrs.repository.AlumnoRepository;
import com.escueladeequitacion.hrs.repository.CaballoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AbonoServiceImpl implements AbonoService {

    @Autowired
    private AbonoRepository abonoRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CaballoRepository caballoRepository;

    @Autowired
    private PrecioService precioService;

    @Autowired
    private FacturacionService facturacionService;

    /**
     * Crea un nuevo abono desde una inscripción.
     * Este es el método principal que se llama cuando un alumno se inscribe.
     */
    @Transactional
    public Abono crearAbonoDesdeInscripcion(InscripcionRequestDto request) {

        // 1. Validar que el alumno existe
        Alumno alumno = alumnoRepository.findById(request.getAlumnoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", request.getAlumnoId()));

        // 2. Validar que no tenga un abono activo
        Optional<Abono> abonoExistente = abonoRepository.findAbonoActivoByAlumnoId(request.getAlumnoId());
        if (abonoExistente.isPresent()) {
            throw new BusinessException(
                    "El alumno ya tiene un abono activo. Debe finalizarlo antes de crear uno nuevo.");
        }

        // 3. Calcular precio
        PrecioCalculadoDto precioCalculado = precioService.calcularPrecioInscripcion(request);

        // 4. Obtener caballo si aplica
        Caballo caballo = null;
        if (request.getTipoPension() != TipoPension.SIN_CABALLO) {
            caballo = caballoRepository.findById(request.getCaballoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Caballo", "ID", request.getCaballoId()));
        }

        // 5. Calcular fecha de vencimiento (un mes desde la contratación)
        LocalDate fechaContratacion = LocalDate.now();
        LocalDate fechaVencimiento = fechaContratacion.plusMonths(1);

        // 6. Crear abono
        Abono abono = new Abono(
                alumno,
                precioCalculado.getPlanClases(),
                precioCalculado.getPlanPension(),
                caballo,
                fechaContratacion,
                fechaVencimiento,
                request.getCantidadClases(),
                obtenerPrecioClases(precioCalculado),
                obtenerPrecioPension(precioCalculado),
                obtenerCuotaSocio(precioCalculado));

        // 7. Guardar abono
        Abono abonoGuardado = abonoRepository.save(abono);

        // 8. NUEVO: Generar factura automáticamente
        facturacionService.generarFacturaMensual(abonoGuardado, fechaContratacion);

        return abonoGuardado;
    }

    /**
     * Obtiene el abono activo de un alumno.
     */
    public Optional<Abono> obtenerAbonoActivo(Long alumnoId) {
        return abonoRepository.findAbonoActivoByAlumnoId(alumnoId);
    }

    /**
     * Descuenta una clase del abono cuando el alumno toma una clase.
     * Este método se llama desde ClaseService al completar una clase.
     */
    @Transactional
    public void descontarClase(Long abonoId) {
        Abono abono = abonoRepository.findById(abonoId)
                .orElseThrow(() -> new ResourceNotFoundException("Abono", "ID", abonoId));

        abono.descontarClase();
        abonoRepository.save(abono);
    }

    /**
     * Cancela un abono.
     */
    @Transactional
    public void cancelarAbono(Long abonoId, String motivo) {
        Abono abono = abonoRepository.findById(abonoId)
                .orElseThrow(() -> new ResourceNotFoundException("Abono", "ID", abonoId));

        if (abono.getEstado() == EstadoAbono.CANCELADO) {
            throw new BusinessException("El abono ya está cancelado");
        }

        abono.setEstado(EstadoAbono.CANCELADO);
        abono.setObservaciones(motivo);
        abonoRepository.save(abono);
    }

    /**
     * Pausa un abono temporalmente.
     */
    @Transactional
    public void pausarAbono(Long abonoId, String motivo) {
        Abono abono = abonoRepository.findById(abonoId)
                .orElseThrow(() -> new ResourceNotFoundException("Abono", "ID", abonoId));

        if (abono.getEstado() != EstadoAbono.ACTIVO) {
            throw new BusinessException("Solo se pueden pausar abonos activos");
        }

        abono.setEstado(EstadoAbono.PAUSADO);
        abono.setObservaciones(motivo);
        abonoRepository.save(abono);
    }

    /**
     * Reactiva un abono pausado.
     */
    @Transactional
    public void reactivarAbono(Long abonoId) {
        Abono abono = abonoRepository.findById(abonoId)
                .orElseThrow(() -> new ResourceNotFoundException("Abono", "ID", abonoId));

        if (abono.getEstado() != EstadoAbono.PAUSADO) {
            throw new BusinessException("Solo se pueden reactivar abonos pausados");
        }

        if (abono.estaVencido()) {
            throw new BusinessException("El abono está vencido, no se puede reactivar");
        }

        abono.setEstado(EstadoAbono.ACTIVO);
        abonoRepository.save(abono);
    }

    /**
     * Lista todos los abonos de un alumno (historial).
     */
    public List<Abono> listarAbonosPorAlumno(Long alumnoId) {
        return abonoRepository.findAllByAlumnoId(alumnoId);
    }

    /**
     * Lista abonos por estado.
     */
    public List<Abono> listarAbonosPorEstado(EstadoAbono estado) {
        return abonoRepository.findByEstado(estado);
    }

    /**
     * Actualiza abonos vencidos (tarea programada).
     * Cambia el estado de ACTIVO a VENCIDO si la fecha de vencimiento ya pasó.
     */
    @Transactional
    public void actualizarAbonosVencidos() {
        List<Abono> abonosVencidos = abonoRepository.findAbonosVencidos(LocalDate.now());

        for (Abono abono : abonosVencidos) {
            abono.setEstado(EstadoAbono.VENCIDO);
            abonoRepository.save(abono);
        }
    }

    // Métodos auxiliares privados

    private java.math.BigDecimal obtenerPrecioClases(PrecioCalculadoDto precio) {
        return precio.getConceptos().stream()
                .filter(c -> c.getConcepto().toLowerCase().contains("abono"))
                .findFirst()
                .map(ConceptoPrecioDto::getMonto)
                .orElse(java.math.BigDecimal.ZERO);
    }

    private java.math.BigDecimal obtenerPrecioPension(PrecioCalculadoDto precio) {
        return precio.getConceptos().stream()
                .filter(c -> c.getConcepto().toLowerCase().contains("pensión"))
                .findFirst()
                .map(ConceptoPrecioDto::getMonto)
                .orElse(null);
    }

    private java.math.BigDecimal obtenerCuotaSocio(PrecioCalculadoDto precio) {
        return precio.getConceptos().stream()
                .filter(c -> c.getConcepto().toLowerCase().contains("socio"))
                .findFirst()
                .map(ConceptoPrecioDto::getMonto)
                .orElse(null);
    }
}
