package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.exception.ConflictException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.repository.AlumnoRepository;
import com.escueladeequitacion.hrs.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Gestiona el ciclo de vida de los códigos de invitación:
 *  POST /api/v1/alumnos/{id}/invitar        — genera/regenera código (COORDINADOR/SUPERADMIN)
 */
@RestController
@RequestMapping("/api/v1/alumnos")
public class InvitacionController {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private EmailService emailService;

    // TODO: En producción, obtener esto de una variable de entorno o la configuración de la app
    private final String frontendUrl = "http://localhost:5173"; 

    /**
     * POST /api/v1/alumnos/{id}/invitar
     * Genera un UUID de invitación para el alumno (o regenera uno expirado/existente)
     * y retorna el link completo para enviar al alumno.
     * Validez: 7 días.
     */
    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PostMapping("/{id}/invitar")
    public ResponseEntity<?> generarInvitacion(@PathVariable("id") Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", id));

        if (alumno.getEmail() == null || alumno.getEmail().isBlank()) {
            throw new ConflictException(
                    "El alumno no tiene email registrado. Agregá un email antes de enviar la invitación.");
        }

        if (Boolean.TRUE.equals(alumno.getCodigoUsado())) {
            throw new ConflictException(
                    "Este alumno ya tiene un usuario registrado en el sistema.");
        }

        // Generar (o regenerar) código UUID
        String codigo = UUID.randomUUID().toString();
        alumno.setCodigoInvitacion(codigo);
        alumno.setCodigoUsado(false);
        alumno.setFechaExpiracionCodigo(LocalDateTime.now().plusDays(7));
        alumnoRepository.save(alumno);

        // Retornamos el código para que el frontend construya la URL de registro
        return ResponseEntity.ok(Map.of(
                "codigo", codigo,
                "email", alumno.getEmail(),
                "nombre", alumno.getNombre() + " " + alumno.getApellido(),
                "expiracion", alumno.getFechaExpiracionCodigo().toString(),
                "urlInvitacion", String.format("%s/register?code=%s", frontendUrl, codigo)
        ));
    }

    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PostMapping("/{id}/invitar/enviar-email")
    public ResponseEntity<?> enviarInvitacionEmail(@PathVariable("id") Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "ID", id));

        if (alumno.getCodigoInvitacion() == null) {
            throw new ConflictException("Primero tenés que generar un código de invitación.");
        }

        String link = String.format("%s/register?code=%s", frontendUrl, alumno.getCodigoInvitacion());
        emailService.enviarInvitacion(alumno.getEmail(), alumno.getNombre(), link);

        return ResponseEntity.ok(Map.of("mensaje", "Correo enviado correctamente a " + alumno.getEmail()));
    }
}
