package com.escueladeequitacion.hrs.config;

import com.escueladeequitacion.hrs.security.RolSeguridad;
import com.escueladeequitacion.hrs.security.User;
import com.escueladeequitacion.hrs.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar usuarios base si no hay ninguno
        if (userRepository.count() == 0) {
            System.out.println("No se encontraron usuarios. Inicializando usuario administrador por defecto...");
            
            // Usuario administrador principal
            User admin = new User();
            admin.setEmail("luckidetrenque@gmail.com");
            admin.setUsername("luckidetrenque@gmail.com");
            admin.setPassword(passwordEncoder.encode("260177Lr"));
            admin.setRol(RolSeguridad.SUPERADMIN);
            admin.setActivo(true);
            
            userRepository.save(admin);
            System.out.println("Usuario SUPERADMIN creado: luckidetrenque@gmail.com / 260177Lr");

            // Otros usuarios de la whitelist (opcional)
            List<String> whitelist = Arrays.asList("claumarnavarro@gmail.com", "santiagomatheu@gmail.com");
            for (String email : whitelist) {
                User user = new User();
                user.setEmail(email);
                user.setUsername(email);
                user.setPassword(passwordEncoder.encode("260177Lr"));
                user.setRol(RolSeguridad.COORDINADOR);
                user.setActivo(true);
                userRepository.save(user);
                System.out.println("Usuario COORDINADOR creado: " + email);
            }
        }
    }
}
