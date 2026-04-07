package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.model.ConfiguracionOperativa;
import com.escueladeequitacion.hrs.repository.ConfiguracionOperativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * GET  /api/v1/configuracion/operativa  — cualquier usuario autenticado puede leer
 * PUT  /api/v1/configuracion/operativa  — solo COORDINADOR o SUPERADMIN
 */
@RestController
@RequestMapping("/api/v1/configuracion")
public class ConfiguracionOperativaController {

    @Autowired
    private ConfiguracionOperativaRepository repo;

    private ConfiguracionOperativa getOrCreate() {
        return repo.findById(1L).orElseGet(() -> repo.save(new ConfiguracionOperativa()));
    }

    @GetMapping("/operativa")
    public ResponseEntity<ConfiguracionOperativa> obtener() {
        return ResponseEntity.ok(getOrCreate());
    }

    @PreAuthorize("hasAnyRole('COORDINADOR','SUPERADMIN')")
    @PutMapping("/operativa")
    public ResponseEntity<ConfiguracionOperativa> actualizar(@RequestBody ConfiguracionOperativa body) {
        ConfiguracionOperativa config = getOrCreate();
        if (body.getHorasAnticipacionCancelacion() != null) {
            config.setHorasAnticipacionCancelacion(body.getHorasAnticipacionCancelacion());
        }
        if (body.getHorasAnticipacionReserva() != null) {
            config.setHorasAnticipacionReserva(body.getHorasAnticipacionReserva());
        }
        if (body.getDiasValidezInvitacion() != null) {
            config.setDiasValidezInvitacion(body.getDiasValidezInvitacion());
        }
        return ResponseEntity.ok(repo.save(config));
    }
}
