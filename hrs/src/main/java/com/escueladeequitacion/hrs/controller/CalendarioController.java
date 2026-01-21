package com.escueladeequitacion.hrs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escueladeequitacion.hrs.dto.CalendarioDto;
import com.escueladeequitacion.hrs.service.CalendarioService;
import com.escueladeequitacion.hrs.utility.Constantes;
import com.escueladeequitacion.hrs.utility.Mensaje;

@RestController
@RequestMapping(Constantes.API_VERSION)
@CrossOrigin(origins = { Constantes.ORIGINS })

// Controlador REST para gestionar el calendario
public class CalendarioController {

    @Autowired
    private CalendarioService calendarioService;

    @PostMapping(Constantes.RESOURCE_CALENDARIO + "/copiar-semana")
    public ResponseEntity<Mensaje> copiar(@RequestBody CalendarioDto request) {
        calendarioService.copiarSemanaCompleta(request.getDiaInicioOrigen(), request.getDiaInicioDestino());
        return ResponseEntity
                .ok(new Mensaje("Clases copiadas exitosamente a la semana del " + request.getDiaInicioDestino()));
    }

    @DeleteMapping(Constantes.RESOURCE_CALENDARIO + "/eliminar-periodo")
    public ResponseEntity<Mensaje> eliminarPeriodo(@RequestBody CalendarioDto request) {
        calendarioService.eliminarClasesEnPeriodo(request.getDiaInicioOrigen(), request.getDiaInicioDestino());
        return ResponseEntity
                .ok(new Mensaje("Clases eliminadas exitosamente desde " + request.getDiaInicioOrigen() + " hasta "
                        + request.getDiaInicioDestino()));
    }

}
