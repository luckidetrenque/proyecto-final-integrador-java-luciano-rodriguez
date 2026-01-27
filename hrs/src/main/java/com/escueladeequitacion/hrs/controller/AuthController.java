package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.exception.ConflictException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
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
@CrossOrigin(origins = { "http://localhost:5173" })
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
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public RolSeguridad getRol() {
            return rol;
        }

        public void setRol(RolSeguridad rol) {
            this.rol = rol;
        }

        public Integer getPersonaDni() {
            return personaDni;
        }

        public void setPersonaDni(Integer personaDni) {
            this.personaDni = personaDni;
        }
    }

    /**
     * POST /api/v1/auth/login
     * Endpoint de login que retorna los datos del usuario autenticado.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Buscamos al usuario por el nombre que viene en el Header de Basic Auth
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", principal.getName()));

        return ResponseEntity.ok(user);
    }

    /**
     * POST /api/v1/auth/register
     * Registra un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        // Validar username duplicado
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Usuario", "username", request.getUsername());
        }

        // Validar email duplicado
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Usuario", "email", request.getEmail());
        }

        // Crear usuario
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRol());

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
     * POST /api/v1/auth/logout
     * Endpoint de logout (en Basic Auth no hay sesión real).
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // No hay sesión que cerrar en el servidor (Basic Auth),
        // pero respondemos 200 OK para que el frontend confirme la acción.
        return ResponseEntity.ok().build();
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
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));
        return ResponseEntity.ok(user);
    }

    /**
     * DELETE /api/v1/auth/users/{id}
     * Elimina un usuario (solo ADMIN).
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        // Validar que existe antes de eliminar
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "ID", id);
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok(new Mensaje("Usuario eliminado correctamente"));
    }

    /**
     * PUT /api/v1/auth/users/{id}
     * Actualiza un usuario existente.
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable("id") Long id,
            @Valid @RequestBody RegisterRequest request) {
        // 1. Validar que el usuario existe
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));

        // 2. Validar username duplicado solo si cambió
        if (!user.getUsername().equals(request.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new ConflictException("Usuario", "username", request.getUsername());
            }
        }

        // 3. Validar email duplicado solo si cambió
        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Usuario", "email", request.getEmail());
            }
        }

        // 4. Actualizar campos
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRol(request.getRol());
        user.setPersonaDni(request.getPersonaDni());

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Usuario actualizado correctamente: " + user.getUsername()));
    }
}