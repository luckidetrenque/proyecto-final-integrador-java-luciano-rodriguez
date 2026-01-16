package com.escueladeequitacion.hrs.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/v1/auth/**").permitAll() // Login/registro
                        // .requestMatchers(HttpMethod.GET, "/api/v1/alumnos").permitAll()

                        // Solo ADMIN
                        // .requestMatchers(HttpMethod.POST, "/api/v1/alumnos/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT, "/api/v1/alumnos/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE, "/api/v1/alumnos/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST, "/api/v1/instructores/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT, "/api/v1/instructores/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,
                        // "/api/v1/instructores/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST, "/api/v1/caballos/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT, "/api/v1/caballos/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE, "/api/v1/caballos/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST, "/api/v1/clases/**").hasRole("ADMIN")

                        // ADMIN e INSTRUCTOR
                        // .requestMatchers(HttpMethod.GET, "/api/v1/clases/**").hasAnyRole("ADMIN",
                        // "INSTRUCTOR")
                        // .requestMatchers(HttpMethod.PUT, "/api/v1/clases/**").hasAnyRole("ADMIN",
                        // "INSTRUCTOR")
                        // .requestMatchers(HttpMethod.DELETE, "/api/v1/clases/**").hasRole("ADMIN")

                        // ALUMNO
                        // .requestMatchers(HttpMethod.GET, "/api/v1/alumnos/**").hasAnyRole("ADMIN", "ALUMNO")

                        .anyRequest().authenticated())

                .httpBasic(basic -> {
                });

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

    @Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Puerto de React
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
    configuration.setAllowCredentials(true); // Muy importante para Basic Auth
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}