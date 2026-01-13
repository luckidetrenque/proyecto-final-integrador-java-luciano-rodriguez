package com.escueladeequitacion.hrs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad usando usuarios de la BD.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/api/v1/auth/**").permitAll() // Login/registro
                .requestMatchers(HttpMethod.GET, "/api/v1/caballos").permitAll()
                
                // Solo ADMIN
                .requestMatchers(HttpMethod.POST, "/api/v1/alumnos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/alumnos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/alumnos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/instructores/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/instructores/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/instructores/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/caballos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/caballos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/caballos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/clases/**").hasRole("ADMIN")
                
                // ADMIN e INSTRUCTOR
                .requestMatchers(HttpMethod.GET, "/api/v1/clases/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                .requestMatchers(HttpMethod.PUT, "/api/v1/clases/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/clases/**").hasRole("ADMIN")
                
                // ALUMNO
                .requestMatchers(HttpMethod.GET, "/api/v1/alumnos/**").hasAnyRole("ADMIN", "ALUMNO")
                
                .anyRequest().authenticated()
            )
            
            .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}