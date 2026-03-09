package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.enums.TipoPension;
import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.model.PlanAbono;
import com.escueladeequitacion.hrs.model.PlanPension;
import com.escueladeequitacion.hrs.enums.FormaPago;
import com.escueladeequitacion.hrs.enums.ModalidadClase;
import com.escueladeequitacion.hrs.enums.TipoClase;
import com.escueladeequitacion.hrs.dto.ConceptoPrecioDto;
import com.escueladeequitacion.hrs.dto.InscripcionRequestDto;
import com.escueladeequitacion.hrs.dto.PrecioCalculadoDto;
import com.escueladeequitacion.hrs.repository.PlanAbonoRepository;
import com.escueladeequitacion.hrs.repository.PlanPensionRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PrecioServiceImpl implements PrecioService {

    @Autowired
    private PlanAbonoRepository planAbonoRepository;

    @Autowired
    private PlanPensionRepository planPensionRepository;

    /**
     * Calcula el precio total de una inscripción según los parámetros.
     * Este es el método principal que usa todo el sistema.
     */
    public PrecioCalculadoDto calcularPrecioInscripcion(InscripcionRequestDto request) {

        List<ConceptoPrecioDto> conceptos = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // 1. DETERMINAR TIPO DE CLASE (si no viene especificado)
        TipoClase tipoClase = determinarTipoClase(request);

        // 2. BUSCAR PLAN DE CLASES
        PlanAbono planClases = buscarPlanClases(
                tipoClase,
                request.getModalidad(),
                request.getCantidadClases(),
                request.getFechaPago());

        if (planClases == null) {
            throw new BusinessException(
                    String.format("No existe plan de %d clases de tipo %s en modalidad %s",
                            request.getCantidadClases(), tipoClase, request.getModalidad()));
        }

        // 3. CALCULAR PRECIO DE CLASES
        BigDecimal precioClases = obtenerPrecioSegunFecha(
                planClases,
                request.getFormaPago(),
                request.getFechaPago());

        conceptos.add(new ConceptoPrecioDto(
                "Abono de " + request.getCantidadClases() + " clases - " + tipoClase.getDescripcion(),
                precioClases));
        total = total.add(precioClases);

        // 4. CUOTA SOCIO (si NO es socio)
        BigDecimal cuotaSocio = null;
        if (!request.getEsSocio() && planClases.getCuotaSocio() != null) {
            cuotaSocio = planClases.getCuotaSocio();
            conceptos.add(new ConceptoPrecioDto("Cuota de socio", cuotaSocio));
            total = total.add(cuotaSocio);
        }

        // 5. PENSIÓN DE CABALLO (solo si tiene caballo propio con pensión)
        PlanPension planPension = null;
        if (request.getTipoPension() == TipoPension.CABALLO_PROPIO) {
            // Validar que tenga al menos 8 clases
            if (request.getCantidadClases() < 8) {
                throw new BusinessException(
                        "Los alumnos con caballo en pensión deben tomar mínimo 8 clases mensuales");
            }

            planPension = buscarPlanPension(request.getCantidadClases(), request.getFechaPago());

            if (planPension == null) {
                throw new BusinessException(
                        "No existe plan de pensión para " + request.getCantidadClases() + " clases");
            }

            BigDecimal precioPension = obtenerPrecioSegunFecha(
                    planPension,
                    request.getFormaPago(),
                    request.getFechaPago());

            conceptos.add(new ConceptoPrecioDto("Pensión de caballo", precioPension));
            total = total.add(precioPension);
        }

        // 6. VALIDAR CABALLO SEGÚN TIPO DE PENSIÓN
        validarCaballo(request);

        // 7. CONSTRUIR RESPUESTA
        PrecioCalculadoDto resultado = new PrecioCalculadoDto(conceptos, total, planClases, planPension);

        // Mensaje adicional según fecha de pago
        if (request.getFechaPago().getDayOfMonth() <= 15) {
            resultado.setMensajeAdicional("Precio con descuento por pago antes del día 15");
        } else {
            resultado.setMensajeAdicional("Precio regular (después del día 15)");
        }

        return resultado;
    }

    /**
     * Determina el tipo de clase según edad y características del alumno.
     */
    private TipoClase determinarTipoClase(InscripcionRequestDto request) {
        // Si ya viene especificado, usarlo
        if (request.getTipoClase() != null) {
            return request.getTipoClase();
        }

        // Determinar automáticamente según edad
        if (request.getEdad() == null) {
            throw new BusinessException("Debe proporcionar la edad del alumno o especificar el tipo de clase");
        }

        if (request.getEdad() < 6) {
            return TipoClase.ESCUELA_MENOR_6;
        } else {
            return TipoClase.ESCUELA_MAYOR_6;
        }
    }

    /**
     * Busca el plan de clases vigente.
     */
    private PlanAbono buscarPlanClases(TipoClase tipo, ModalidadClase modalidad,
            Integer cantidad, LocalDate fecha) {
        return planAbonoRepository.findPlanVigente(tipo, modalidad, cantidad, fecha)
                .orElse(null);
    }

    /**
     * Busca el plan de pensión vigente.
     */
    private PlanPension buscarPlanPension(Integer cantidad, LocalDate fecha) {
        return planPensionRepository.findPlanVigente(cantidad, fecha)
                .orElse(null);
    }

    /**
     * Obtiene el precio según forma de pago y fecha (para PlanAbono).
     */
    private BigDecimal obtenerPrecioSegunFecha(PlanAbono plan, FormaPago formaPago, LocalDate fechaPago) {
        boolean esAntesDel15 = fechaPago.getDayOfMonth() <= 15;

        if (formaPago == FormaPago.EFECTIVO) {
            return esAntesDel15 ? plan.getPrecioEfectivo1al15() : plan.getPrecioEfectivoDespues15();
        } else {
            return esAntesDel15 ? plan.getPrecioTransferencia1al15() : plan.getPrecioTransferenciaDespues15();
        }
    }

    /**
     * Obtiene el precio según forma de pago y fecha (para PlanPension).
     */
    private BigDecimal obtenerPrecioSegunFecha(PlanPension plan, FormaPago formaPago, LocalDate fechaPago) {
        boolean esAntesDel15 = fechaPago.getDayOfMonth() <= 15;

        if (formaPago == FormaPago.EFECTIVO) {
            return esAntesDel15 ? plan.getPrecioEfectivo1al15() : plan.getPrecioEfectivoDespues15();
        } else {
            return esAntesDel15 ? plan.getPrecioTransferencia1al15() : plan.getPrecioTransferenciaDespues15();
        }
    }

    /**
     * Valida que el caballo sea coherente con el tipo de pensión.
     */
    private void validarCaballo(InscripcionRequestDto request) {
        if (request.getTipoPension() == TipoPension.SIN_CABALLO) {
            if (request.getCaballoId() != null) {
                throw new BusinessException("No debe especificar caballo si el tipo de pensión es SIN_CABALLO");
            }
        } else {
            // RESERVA_ESCUELA o CABALLO_PROPIO
            if (request.getCaballoId() == null) {
                throw new BusinessException("Debe especificar el caballo cuando el tipo de pensión no es SIN_CABALLO");
            }
        }
    }

    /**
     * Lista todos los planes de abono vigentes en una fecha.
     * Útil para mostrar catálogo de precios.
     */
    public List<PlanAbono> listarPlanesVigentes(LocalDate fecha) {
        return planAbonoRepository.findAllVigentes(fecha);
    }

    /**
     * Lista todos los planes de pensión vigentes.
     */
    public List<PlanPension> listarPlanesPensionVigentes(LocalDate fecha) {
        return planPensionRepository.findAllVigentes(fecha);
    }
}