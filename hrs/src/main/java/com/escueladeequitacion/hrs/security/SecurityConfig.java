package com.escueladeequitacion.hrs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad simple con usuarios en memoria.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura las reglas de seguridad.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF (para facilitar pruebas con Postman)
                .csrf(csrf -> csrf.disable())

                // Configurar autorización de endpoints
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (sin autenticación)
                        .requestMatchers("/api/v1/alumnos/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/caballos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/caballos/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/clases/detalles").permitAll()

                        // Solo ADMIN puede crear, actualizar y eliminar
                        .requestMatchers(HttpMethod.POST, "/api/v1/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")

                        // INSTRUCTOR puede ver clases y actualizarlas
                        // .requestMatchers(HttpMethod.GET, "/api/v1/clases/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/clases/*/estado").hasAnyRole("ADMIN", "INSTRUCTOR")

                        // ALUMNO puede ver sus propios datos
                        .requestMatchers(HttpMethod.GET, "/api/v1/alumnos/**").hasAnyRole("ADMIN", "ALUMNO")

                        // Cualquier otro endpoint requiere autenticación
                        .anyRequest().authenticated())

                // Usar HTTP Basic Authentication (usuario y contraseña en cada request)
                .httpBasic(basic -> {
                });

        return http.build();
    }

    /**
     * Define usuarios en memoria (para desarrollo).
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Usuario ADMIN
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        // Usuario INSTRUCTOR
        UserDetails instructor = User.builder()
                .username("claudia")
                .password(passwordEncoder().encode("claudia123"))
                .roles("INSTRUCTOR")
                .build();

        // Usuario ALUMNO
        UserDetails alumno = User.builder()
                .username("luciano")
                .password(passwordEncoder().encode("luciano123"))
                .roles("ALUMNO")
                .build();

        return new InMemoryUserDetailsManager(admin, instructor, alumno);
    }

    /**
     * Codificador de contraseñas BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}