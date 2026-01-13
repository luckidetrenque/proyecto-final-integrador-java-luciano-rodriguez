package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.security.RolSeguridad;
import com.escueladeequitacion.hrs.security.User;
import com.escueladeequitacion.hrs.security.UserRepository;
import com.escueladeequitacion.hrs.utility.Mensaje;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestión de usuarios (registro, listar).
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = { "http://localhost:4200" })
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * DTO para registro de usuarios.
     */
    public static class RegisterRequest {
        @NotBlank(message = "El username no puede estar vacío")
        private String username;

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email debe ser válido")
        private String email;

        @NotBlank(message = "El password no puede estar vacío")
        private String password;

        @NotNull(message = "El rol no puede estar vacío")
        private RolSeguridad rol;

        private Integer personaDni; // Opcional: vincular con Alumno/Instructor

        // Getters y Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public RolSeguridad getRol() { return rol; }
        public void setRol(RolSeguridad rol) { this.rol = rol; }
        
        public Integer getPersonaDni() { return personaDni; }
        public void setPersonaDni(Integer personaDni) { this.personaDni = personaDni; }
    }

    /**
     * POST /api/v1/auth/register
     * Registra un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        // Validar username duplicado
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Mensaje("El username ya existe"));
        }

        // Validar email duplicado
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Mensaje("El email ya está registrado"));
        }

        // Crear usuario
        User user = new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getRol()
        );

        // Vincular con persona si se proporciona DNI
        if (request.getPersonaDni() != null) {
            user.setPersonaDni(request.getPersonaDni());
            user.setPersonaTipo(request.getRol().name()); // ALUMNO o INSTRUCTOR
        }

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Usuario registrado correctamente: " + user.getUsername()));
    }

    /**
     * GET /api/v1/auth/users
     * Lista todos los usuarios (solo ADMIN).
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> listarUsuarios() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/v1/auth/users/{id}
     * Obtiene un usuario por ID.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("User", "ID", id));
        return ResponseEntity.ok(user);
    }

    /**
     * DELETE /api/v1/auth/users/{id}
     * Elimina un usuario (solo ADMIN).
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("Usuario no encontrado"));
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok(new Mensaje("Usuario eliminado correctamente"));
    }
}