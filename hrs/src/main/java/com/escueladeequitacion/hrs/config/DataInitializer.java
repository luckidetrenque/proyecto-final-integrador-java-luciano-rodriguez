package com.escueladeequitacion.hrs.config;

import com.escueladeequitacion.hrs.enums.ModalidadClase;
import com.escueladeequitacion.hrs.enums.TipoClase;
import com.escueladeequitacion.hrs.model.PeriodoPrecio;
import com.escueladeequitacion.hrs.model.PlanAbono;
import com.escueladeequitacion.hrs.model.PlanPension;
import com.escueladeequitacion.hrs.repository.PeriodoPrecioRepository;
import com.escueladeequitacion.hrs.repository.PlanAbonoRepository;
import com.escueladeequitacion.hrs.repository.PlanPensionRepository;
import com.escueladeequitacion.hrs.security.RolSeguridad;
import com.escueladeequitacion.hrs.security.User;
import com.escueladeequitacion.hrs.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PeriodoPrecioRepository periodoPrecioRepository;

    @Autowired
    private PlanAbonoRepository planAbonoRepository;

    @Autowired
    private PlanPensionRepository planPensionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Value("${app.coordinator.emails:}")
    private String coordinatorEmails;

    @Value("${app.coordinator.password:}")
    private String coordinatorPassword;

    @Value("${app.reset-alumno-passwords:false}")
    private boolean resetAlumnoPasswords;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar usuarios base si no hay ninguno
        if (userRepository.count() == 0) {
            // Usuario SUPERADMIN principal
            if (adminEmail != null && !adminEmail.isBlank() && adminPassword != null && !adminPassword.isBlank()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setUsername(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRol(RolSeguridad.SUPERADMIN);
                admin.setActivo(true);

                userRepository.save(admin);
                System.out.println("Usuario SUPERADMIN creado: " + adminEmail);
            } else {
                System.out.println("ADVERTENCIA: No se configuraron credenciales de SUPERADMIN (app.admin.email / app.admin.password). No se creó el usuario inicial.");
            }

            // Otros usuarios COORDINADOR (opcional)
            if (coordinatorEmails != null && !coordinatorEmails.isBlank() && coordinatorPassword != null && !coordinatorPassword.isBlank()) {
                List<String> emails = Arrays.stream(coordinatorEmails.split(","))
                        .map(String::trim)
                        .filter(e -> !e.isEmpty())
                        .toList();
                for (String email : emails) {
                    User user = new User();
                    user.setEmail(email);
                    user.setUsername(email);
                    user.setPassword(passwordEncoder.encode(coordinatorPassword));
                    user.setRol(RolSeguridad.COORDINADOR);
                    user.setActivo(true);
                    userRepository.save(user);
                    System.out.println("Usuario COORDINADOR creado: " + email);
                }
            }
        }

        // Reseteo de contraseñas de alumnos (solo si se activa explícitamente)
        if (resetAlumnoPasswords) {
            List<User> alumnos = userRepository.findByRol(RolSeguridad.ALUMNO);
            if (!alumnos.isEmpty()) {
                String encodedPassword = passwordEncoder.encode("alumno123");
                for (User alumno : alumnos) {
                    alumno.setPassword(encodedPassword);
                }
                userRepository.saveAll(alumnos);
                System.out.println(">>> SEGURIDAD: Se han reseteado las contraseñas de " + alumnos.size() + " alumnos a 'alumno123'");
            } else {
                System.out.println(">>> SEGURIDAD: No se encontraron usuarios con rol ALUMNO para resetear.");
            }
        }

        // Inicializar Datos Financieros (Precios) si no hay ninguno
        if (periodoPrecioRepository.count() == 0) {
            System.out.println(">>> FINANZAS: Inicializando lista de precios base...");
            
            PeriodoPrecio periodo = new PeriodoPrecio(
                "LISTA BASE 2024-2030", 
                LocalDate.of(2024, 1, 1), 
                LocalDate.of(2030, 12, 31)
            );
            periodo = periodoPrecioRepository.save(periodo);

            // 1. Planes de Abono (Clases)
            crearPlanAbono(periodo, TipoClase.ESCUELA_MAYOR_6, ModalidadClase.SEMANA, 4, "40000", "42000", "2000");
            crearPlanAbono(periodo, TipoClase.ESCUELA_MAYOR_6, ModalidadClase.SEMANA, 8, "72000", "75000", "2000");
            crearPlanAbono(periodo, TipoClase.ESCUELA_MAYOR_6, ModalidadClase.SABADOS, 4, "45000", "47000", "2000");
            
            crearPlanAbono(periodo, TipoClase.ESCUELA_MENOR_6, ModalidadClase.SEMANA, 4, "35000", "37000", "1500");
            crearPlanAbono(periodo, TipoClase.EQUINOTERAPIA, ModalidadClase.SESION, 1, "12000", "13000", "0");
            crearPlanAbono(periodo, TipoClase.PENSIONADOS_PRIVADOS, ModalidadClase.SEMANA, 8, "55000", "58000", "0");

            // 2. Planes de Pensión
            crearPlanPension(periodo, 8, "Pensión 8 clases", "85000", "90000");
            crearPlanPension(periodo, 12, "Pensión 12 clases", "110000", "115000");
            crearPlanPension(periodo, 16, "Pensión 16 clases", "130000", "135000");

            System.out.println(">>> FINANZAS: Precios inicializados correctamente.");
        }
    }

    private void crearPlanAbono(PeriodoPrecio periodo, TipoClase tipo, ModalidadClase mod, int cant, String precio1, String precio2, String socio) {
        PlanAbono plan = new PlanAbono();
        plan.setPeriodoVigencia(periodo);
        plan.setTipoClase(tipo);
        plan.setModalidad(mod);
        plan.setCantidadClases(cant);
        plan.setDescripcion(cant + " clases - " + tipo.getDescripcion());
        plan.setPrecioEfectivo1al15(new BigDecimal(precio1));
        plan.setPrecioTransferencia1al15(new BigDecimal(precio1));
        plan.setPrecioEfectivoDespues15(new BigDecimal(precio2));
        plan.setPrecioTransferenciaDespues15(new BigDecimal(precio2));
        plan.setCuotaSocio(new BigDecimal(socio));
        plan.setFechaVigenciaDesde(periodo.getFechaInicio());
        plan.setFechaVigenciaHasta(periodo.getFechaFin());
        plan.setActivo(true);
        planAbonoRepository.save(plan);
    }

    private void crearPlanPension(PeriodoPrecio periodo, int cant, String desc, String precio1, String precio2) {
        PlanPension plan = new PlanPension();
        plan.setPeriodoVigencia(periodo);
        plan.setCantidadClases(cant);
        plan.setDescripcion(desc);
        plan.setPrecioEfectivo1al15(new BigDecimal(precio1));
        plan.setPrecioTransferencia1al15(new BigDecimal(precio1));
        plan.setPrecioEfectivoDespues15(new BigDecimal(precio2));
        plan.setPrecioTransferenciaDespues15(new BigDecimal(precio2));
        plan.setFechaVigenciaDesde(periodo.getFechaInicio());
        plan.setFechaVigenciaHasta(periodo.getFechaFin());
        plan.setActivo(true);
        planPensionRepository.save(plan);
    }
}
