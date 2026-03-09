package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.*;
import com.escueladeequitacion.hrs.dto.ResumenFinancieroDto.DesglosePlanDto;
import com.escueladeequitacion.hrs.dto.ResumenFinancieroDto.PuntoEvolucionDto;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.enums.TipoPension;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.model.ConfiguracionPrecios;
import com.escueladeequitacion.hrs.model.Instructor;
import com.escueladeequitacion.hrs.repository.AlumnoRepository;
import com.escueladeequitacion.hrs.repository.ClaseRepository;
import com.escueladeequitacion.hrs.repository.ConfiguracionPreciosRepository;
import com.escueladeequitacion.hrs.repository.InstructorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class FinanzasServiceImpl implements FinanzasService {

    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private ClaseRepository claseRepository;
    @Autowired
    private ConfiguracionPreciosRepository configRepo;

    // ── Singleton de configuración ────────────────────────────────────────────

    private ConfiguracionPrecios getOrCreateConfig() {
        return configRepo.findById(1L).orElseGet(() -> {
            ConfiguracionPrecios defaults = new ConfiguracionPrecios();
            defaults.setId(1L);
            return configRepo.save(defaults);
        });
    }

    // ── getConfiguracion ─────────────────────────────────────────────────────

    @Override
    public ConfiguracionPreciosDto getConfiguracion() {
        ConfiguracionPrecios cfg = getOrCreateConfig();
        return toDto(cfg);
    }

    // ── updateConfiguracion ──────────────────────────────────────────────────

    @Override
    public ConfiguracionPreciosDto updateConfiguracion(ConfiguracionPreciosDto dto) {
        ConfiguracionPrecios cfg = getOrCreateConfig();
        applyDto(cfg, dto);
        configRepo.save(cfg);
        return toDto(cfg);
    }

    // ── calcularResumen ──────────────────────────────────────────────────────

    @Override
    public ResumenFinancieroDto calcularResumen(LocalDate inicio, LocalDate fin) {
        ConfiguracionPrecios cfg = getOrCreateConfig();
        List<Alumno> alumnos = alumnoRepository.findByActivo(true);
        List<Instructor> instructores = instructorRepository.findByActivo(true);

        // ── Ingresos cuotas ────────────────────────────────────────────────
        double cuotas = 0;
        int[] planes = { 4, 8, 12, 16 };
        List<DesglosePlanDto> desglose = new ArrayList<>();

        for (int plan : planes) {
            long count = alumnos.stream()
                    .filter(a -> a.getCantidadClases() == plan)
                    .count();
            double tarifa = tarifaPlan(cfg, plan);
            cuotas += count * tarifa;
            desglose.add(new DesglosePlanDto(plan, (int) count, tarifa));
        }

        // ── Ingresos pensiones ────────────────────────────────────────────
        double pensiones = alumnos.stream()
                .mapToDouble(a -> calcularPensionAlumno(cfg, a))
                .sum();

        // ── Egresos honorarios (clases completadas en el período) ─────────
        double honorarios = instructores.stream().mapToDouble(inst -> {
            long completadas = claseRepository.contarPorInstructorYEstado(
                    inst.getId(), Estado.COMPLETADA);
            // Si el período es de varios meses, prorrateamos el base
            long meses = Math.max(1,
                    YearMonth.from(inicio).until(YearMonth.from(fin),
                            java.time.temporal.ChronoUnit.MONTHS) + 1);
            return cfg.getHonorarioBaseMensual() * meses
                    + completadas * cfg.getHonorarioPorClase();
        }).sum();

        // ── Evolución mensual ─────────────────────────────────────────────
        List<PuntoEvolucionDto> evolucion = new ArrayList<>();
        YearMonth ymInicio = YearMonth.from(inicio);
        YearMonth ymFin = YearMonth.from(fin);
        YearMonth ym = ymInicio;
        while (!ym.isAfter(ymFin)) {
            LocalDate mesInicio = ym.atDay(1);
            LocalDate mesFin = ym.atEndOfMonth();

            // Ingresos proyectados del mes = cuotas + pensiones (estáticos)
            double ingMes = cuotas + pensiones;

            // Egresos del mes = base mensual instructores activos + clases del mes
            double egrMes = calcularEgresosMes(cfg, instructores, mesInicio, mesFin);

            String label = ym.getMonth()
                    .getDisplayName(TextStyle.SHORT, new Locale("es", "AR"))
                    + " " + ym.getYear();
            evolucion.add(new PuntoEvolucionDto(label, ingMes, egrMes));
            ym = ym.plusMonths(1);
        }

        ResumenFinancieroDto res = new ResumenFinancieroDto();
        res.setIngresosCuotasProyectado(cuotas);
        res.setIngresosPensionesProyectado(pensiones);
        res.setIngresosTotalProyectado(cuotas + pensiones);
        res.setEgresoHonorarios(honorarios);
        res.setBalanceProyectado(cuotas + pensiones - honorarios);
        res.setDesglosePlanes(desglose);
        res.setEvolucion(evolucion);
        return res;
    }

    // ── calcularCuotasAlumnos ────────────────────────────────────────────────

    @Override
    public CuotasAlumnosDto calcularCuotasAlumnos(LocalDate inicio, LocalDate fin) {
        ConfiguracionPrecios cfg = getOrCreateConfig();
        List<Alumno> alumnos = alumnoRepository.findByActivo(true);

        List<CuotasAlumnosDto.FilaAlumnoDto> filas = new ArrayList<>();
        double total = 0;

        for (Alumno a : alumnos) {
            long completadas = claseRepository.contarPorAlumnoYEstado(
                    a.getId(), Estado.COMPLETADA);

            double montoCuota = tarifaPlan(cfg, a.getCantidadClases());
            double montoPension = calcularPensionAlumno(cfg, a);
            double totalAlumno = montoCuota + montoPension;
            total += totalAlumno;

            CuotasAlumnosDto.FilaAlumnoDto fila = new CuotasAlumnosDto.FilaAlumnoDto();
            fila.setAlumnoId(a.getId());
            fila.setNombre(a.getNombre());
            fila.setApellido(a.getApellido());
            fila.setPlan(a.getCantidadClases());
            fila.setClasesCompletadas(completadas);
            fila.setMontoCuota(montoCuota);
            fila.setTipoPension(a.getTipoPension() != null ? a.getTipoPension().name() : "SIN_CABALLO");
            fila.setMontoPension(montoPension);
            fila.setTotalAlumno(totalAlumno);
            filas.add(fila);
        }

        // Ordenar por apellido
        filas.sort((a, b) -> a.getApellido().compareToIgnoreCase(b.getApellido()));

        CuotasAlumnosDto dto = new CuotasAlumnosDto();
        dto.setTotalProyectado(total);
        dto.setAlumnosActivos(alumnos.size());
        dto.setFilas(filas);
        return dto;
    }

    // ── calcularPensiones ────────────────────────────────────────────────────

    @Override
    public PensionesDto calcularPensiones() {
        ConfiguracionPrecios cfg = getOrCreateConfig();
        List<Alumno> alumnos = alumnoRepository.findAll();

        List<PensionesDto.FilaPensionDto> filas = new ArrayList<>();
        double total = 0;

        for (Alumno a : alumnos) {
            if (a.getTipoPension() == null
                    || a.getTipoPension() == TipoPension.SIN_CABALLO
                    || !a.isActivo()) {
                continue;
            }

            double monto = calcularPensionAlumno(cfg, a);
            if (monto == 0)
                continue;

            total += monto;

            PensionesDto.FilaPensionDto fila = new PensionesDto.FilaPensionDto();
            fila.setAlumnoId(a.getId());
            fila.setNombre(a.getNombre());
            fila.setApellido(a.getApellido());
            fila.setTipoPension(a.getTipoPension().name());
            fila.setCuotaPension(a.getCuotaPension() != null ? a.getCuotaPension().name() : null);
            fila.setCaballoNombre(a.getCaballoPropio() != null ? a.getCaballoPropio().getNombre() : null);
            fila.setMonto(monto);
            filas.add(fila);
        }

        filas.sort((x, y) -> x.getApellido().compareToIgnoreCase(y.getApellido()));

        PensionesDto dto = new PensionesDto();
        dto.setTotalMensual(total);
        dto.setFilas(filas);
        return dto;
    }

    // ── calcularHonorarios ───────────────────────────────────────────────────

    @Override
    public HonorariosDto calcularHonorarios(LocalDate inicio, LocalDate fin) {
        ConfiguracionPrecios cfg = getOrCreateConfig();
        List<Instructor> instructores = instructorRepository.findByActivo(true);

        List<HonorariosDto.FilaInstructorDto> filas = new ArrayList<>();
        double total = 0;

        long meses = Math.max(1,
                YearMonth.from(inicio).until(YearMonth.from(fin),
                        java.time.temporal.ChronoUnit.MONTHS) + 1);

        for (Instructor inst : instructores) {
            long completadas = claseRepository.contarPorInstructorYEstado(
                    inst.getId(), Estado.COMPLETADA);

            double base = cfg.getHonorarioBaseMensual() * meses;
            double porClases = completadas * cfg.getHonorarioPorClase();
            double totalInst = base + porClases;
            total += totalInst;

            HonorariosDto.FilaInstructorDto fila = new HonorariosDto.FilaInstructorDto();
            fila.setInstructorId(inst.getId());
            fila.setNombre(inst.getNombre());
            fila.setApellido(inst.getApellido());
            fila.setClasesCompletadas(completadas);
            fila.setHonorarioBase(base);
            fila.setHonorarioPorClases(porClases);
            fila.setTotalHonorario(totalInst);
            filas.add(fila);
        }

        filas.sort((a, b) -> Double.compare(b.getTotalHonorario(), a.getTotalHonorario()));

        HonorariosDto dto = new HonorariosDto();
        dto.setTotalHonorarios(total);
        dto.setFilas(filas);
        return dto;
    }

    // ── Helpers privados ─────────────────────────────────────────────────────

    private double tarifaPlan(ConfiguracionPrecios cfg, int plan) {
        return switch (plan) {
            case 4 -> cfg.getCuota4Clases();
            case 8 -> cfg.getCuota8Clases();
            case 12 -> cfg.getCuota12Clases();
            case 16 -> cfg.getCuota16Clases();
            default -> 0.0;
        };
    }

    private double calcularPensionAlumno(ConfiguracionPrecios cfg, Alumno a) {
        if (a.getTipoPension() == null)
            return 0.0;
        return switch (a.getTipoPension()) {
            case SIN_CABALLO -> 0.0;
            case RESERVA_ESCUELA -> cfg.getReservaEscuela();
            case CABALLO_PROPIO -> {
                if (a.getCuotaPension() == null)
                    yield 0.0;
                yield switch (a.getCuotaPension()) {
                    case ENTERA -> cfg.getPensionEntera();
                    case MEDIA -> cfg.getPensionMedia();
                    case TERCIO -> cfg.getPensionTercio();
                };
            }
        };
    }

    private double calcularEgresosMes(ConfiguracionPrecios cfg,
            List<Instructor> instructores, LocalDate inicio, LocalDate fin) {
        return instructores.stream().mapToDouble(inst -> {
            long completadas = claseRepository.contarPorInstructorYEstado(
                    inst.getId(), Estado.COMPLETADA);
            return cfg.getHonorarioBaseMensual()
                    + completadas * cfg.getHonorarioPorClase();
        }).sum();
    }

    private ConfiguracionPreciosDto toDto(ConfiguracionPrecios cfg) {
        ConfiguracionPreciosDto dto = new ConfiguracionPreciosDto();
        dto.setCuota4Clases(cfg.getCuota4Clases());
        dto.setCuota8Clases(cfg.getCuota8Clases());
        dto.setCuota12Clases(cfg.getCuota12Clases());
        dto.setCuota16Clases(cfg.getCuota16Clases());
        dto.setPensionEntera(cfg.getPensionEntera());
        dto.setPensionMedia(cfg.getPensionMedia());
        dto.setPensionTercio(cfg.getPensionTercio());
        dto.setReservaEscuela(cfg.getReservaEscuela());
        dto.setHonorarioPorClase(cfg.getHonorarioPorClase());
        dto.setHonorarioBaseMensual(cfg.getHonorarioBaseMensual());
        return dto;
    }

    private void applyDto(ConfiguracionPrecios cfg, ConfiguracionPreciosDto dto) {
        if (dto.getCuota4Clases() != null)
            cfg.setCuota4Clases(dto.getCuota4Clases());
        if (dto.getCuota8Clases() != null)
            cfg.setCuota8Clases(dto.getCuota8Clases());
        if (dto.getCuota12Clases() != null)
            cfg.setCuota12Clases(dto.getCuota12Clases());
        if (dto.getCuota16Clases() != null)
            cfg.setCuota16Clases(dto.getCuota16Clases());
        if (dto.getPensionEntera() != null)
            cfg.setPensionEntera(dto.getPensionEntera());
        if (dto.getPensionMedia() != null)
            cfg.setPensionMedia(dto.getPensionMedia());
        if (dto.getPensionTercio() != null)
            cfg.setPensionTercio(dto.getPensionTercio());
        if (dto.getReservaEscuela() != null)
            cfg.setReservaEscuela(dto.getReservaEscuela());
        if (dto.getHonorarioPorClase() != null)
            cfg.setHonorarioPorClase(dto.getHonorarioPorClase());
        if (dto.getHonorarioBaseMensual() != null)
            cfg.setHonorarioBaseMensual(dto.getHonorarioBaseMensual());
    }
};