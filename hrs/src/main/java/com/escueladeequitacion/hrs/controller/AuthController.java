package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.exception.ConflictException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.exception.UnauthorizedException;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.repository.AlumnoRepository;
import com.escueladeequitacion.hrs.security.RolSeguridad;
import com.escueladeequitacion.hrs.security.StorageService;
import com.escueladeequitacion.hrs.security.User;
import com.escueladeequitacion.hrs.security.UserRepository;
import com.escueladeequitacion.hrs.utility.Mensaje;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gestión de usuarios (registro, listar, invitación por código).
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ── DTOs ──────────────────────────────────────────────────────────────────

    /**
     * DTO para registro de usuarios (con o sin código de invitación).
     */
    public static class RegisterRequest {
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email debe ser válido")
        private String email;

        @NotBlank(message = "El password no puede estar vacío")
        private String password;

        private String username;

        /** Código UUID de invitación (opcional — registro abierto si no se provee). */
        private String codigoInvitacion;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getCodigoInvitacion() { return codigoInvitacion; }
        public void setCodigoInvitacion(String codigoInvitacion) { this.codigoInvitacion = codigoInvitacion; }
    }

    /**
     * DTO para que el COORDINADOR/SUPERADMIN actualice rol y estado sin contraseña.
     */
    public static class UpdateUserAdminRequest {
        private RolSeguridad rol;
        private Boolean activo;
        private Long instructorId;
        private Long alumnoId;

        public RolSeguridad getRol() { return rol; }
        public void setRol(RolSeguridad rol) { this.rol = rol; }
        public Boolean getActivo() { return activo; }
        public void setActivo(Boolean activo) { this.activo = activo; }
        public Long getInstructorId() { return instructorId; }
        public void setInstructorId(Long instructorId) { this.instructorId = instructorId; }
        public Long getAlumnoId() { return alumnoId; }
        public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }
    }

    // ── Endpoints de autenticación ─────────────────────────────────────────────

    /**
     * POST /api/v1/auth/login
     * El login utiliza el email como identificador (Spring Security llama
     * loadUserByUsername con lo que venga en el header Basic Auth).
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(java.security.Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        // Buscamos al usuario por email (el "username" en Basic Auth ahora es el email)
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", principal.getName()));

        return ResponseEntity.ok(user);
    }

    /**
     * POST /api/v1/auth/register
     * Flujo 1: con código de invitación → vincula al alumno.
     * Flujo 2: sin código → registro abierto como ALUMNO.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        String email = request.getEmail().trim();

        // Validar email duplicado
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Usuario", "email", email);
        }

        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        if (username != null && !username.isEmpty() && userRepository.existsByUsername(username)) {
            throw new ConflictException("Usuario", "username", username);
        }

        RolSeguridad rolFinal = RolSeguridad.ALUMNO;
        Long alumnoIdVinculado = null;

        // ── Flujo con código de invitación ──────────────────────
        if (request.getCodigoInvitacion() != null && !request.getCodigoInvitacion().isBlank()) {
            String codigo = request.getCodigoInvitacion().trim();

            Alumno alumno = alumnoRepository.findByCodigoInvitacion(codigo)
                    .orElseThrow(() -> new ConflictException("El código de invitación no es válido o no existe"));

            if (Boolean.TRUE.equals(alumno.getCodigoUsado())) {
                throw new ConflictException("El código de invitación ya fue utilizado");
            }

            if (alumno.getFechaExpiracionCodigo() != null
                    && alumno.getFechaExpiracionCodigo().isBefore(LocalDateTime.now())) {
                throw new ConflictException("El código de invitación ha expirado");
            }

            // El email del registro debe coincidir con el email del alumno
            if (!alumno.getEmail().equalsIgnoreCase(email)) {
                throw new ConflictException("El email no coincide con el del alumno invitado");
            }

            // Marcar código como usado
            alumno.setCodigoUsado(true);
            alumnoRepository.save(alumno);

            rolFinal = RolSeguridad.ALUMNO;
            alumnoIdVinculado = alumno.getId();
        }

        // ── Crear usuario ──────────────────────────────────────
        User user = new User();
        user.setEmail(email);
        user.setUsername(username != null && !username.isEmpty() ? username : email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRol(rolFinal);

        if (alumnoIdVinculado != null) {
            user.setAlumnoId(alumnoIdVinculado);
            user.setPersonaTipo("ALUMNO");
        }

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Usuario registrado correctamente: " + email));
    }

    /**
     * POST /api/v1/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validate() {
        return ResponseEntity.ok().build();
    }

    // ── Verificación de código de invitación (público) ─────────────────────────

    /**
     * GET /api/v1/auth/verify-code/{codigo}
     * Retorna los datos del alumno asociado al código (nombre, email)
     * para pre-llenar el formulario de registro. Endpoint público.
     */
    @GetMapping("/verify-code/{codigo}")
    public ResponseEntity<?> verificarCodigo(@PathVariable("codigo") String codigo) {
        Alumno alumno = alumnoRepository.findByCodigoInvitacion(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Código", "codigo", codigo));

        if (Boolean.TRUE.equals(alumno.getCodigoUsado())) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(new Mensaje("El código ya fue utilizado"));
        }

        if (alumno.getFechaExpiracionCodigo() != null
                && alumno.getFechaExpiracionCodigo().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(new Mensaje("El código ha expirado"));
        }

        return ResponseEntity.ok(Map.of(
                "nombre", alumno.getNombre(),
                "apellido", alumno.getApellido(),
                "email", alumno.getEmail() != null ? alumno.getEmail() : ""
        ));
    }

    // ── Administración de usuarios ─────────────────────────────────────────────

    /**
     * GET /api/v1/auth/users
     * Lista todos los usuarios (COORDINADOR y SUPERADMIN).
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> listarUsuarios() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    /**
     * GET /api/v1/auth/users/{id}
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));
        return ResponseEntity.ok(user);
    }

    /**
     * DELETE /api/v1/auth/users/{id}
     * Solo SUPERADMIN puede eliminar usuarios.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable("id") Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "ID", id);
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(new Mensaje("Usuario eliminado correctamente"));
    }

    /**
     * PUT /api/v1/auth/users/{id}
     * Actualiza email y rol de un usuario (COORDINADOR/SUPERADMIN).
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable("id") Long id,
            @Valid @RequestBody RegisterRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));

        String email = request.getEmail().trim();

        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new ConflictException("Usuario", "email", email);
        }

        user.setEmail(email);
        userRepository.save(user);

        return ResponseEntity.ok(new Mensaje("Usuario actualizado correctamente: " + email));
    }

    /**
     * PATCH /api/v1/auth/users/{id}/admin
     * Actualiza rol y estado. El COORDINADOR no puede asignar COORDINADOR ni SUPERADMIN.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PatchMapping("/users/{id}/admin")
    public ResponseEntity<?> adminUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserAdminRequest request,
            Authentication authentication) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));

        // Verificar permisos de asignación de rol
        if (request.getRol() != null) {
            boolean esSuperAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"));

            if (!esSuperAdmin) {
                // COORDINADOR solo puede asignar INSTRUCTOR o ALUMNO
                if (request.getRol() == RolSeguridad.COORDINADOR
                        || request.getRol() == RolSeguridad.SUPERADMIN) {
                    throw new UnauthorizedException(
                            "No tenés permisos para asignar el rol " + request.getRol().name());
                }
            }

            user.setRol(request.getRol());
            if (user.getPersonaTipo() != null) {
                user.setPersonaTipo(request.getRol().name());
            }
        }

        if (request.getActivo() != null) {
            user.setActivo(request.getActivo());
        }

        if (request.getInstructorId() != null) {
            user.setInstructorId(request.getInstructorId());
            user.setPersonaTipo("INSTRUCTOR");
        }

        if (request.getAlumnoId() != null) {
            user.setAlumnoId(request.getAlumnoId());
            user.setPersonaTipo("ALUMNO");
        }

        userRepository.save(user);
        return ResponseEntity.ok(new Mensaje("Usuario actualizado correctamente"));
    }

    /**
     * POST /api/v1/auth/users/{id}/avatar
     */
    @PostMapping("/users/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(
            @PathVariable("id") Long id,
            @RequestParam("avatar") MultipartFile file) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));

        String fileName = "avatar_" + id + "_" + System.currentTimeMillis() + ".jpg";
        storageService.store(file, fileName);

        String avatarUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));
    }
}
