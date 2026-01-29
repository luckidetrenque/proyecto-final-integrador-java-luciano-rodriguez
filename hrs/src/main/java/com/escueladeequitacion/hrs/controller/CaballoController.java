package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.CaballoDto;
import com.escueladeequitacion.hrs.enums.Tipo;
import com.escueladeequitacion.hrs.model.Caballo;
import com.escueladeequitacion.hrs.service.CaballoService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import com.escueladeequitacion.hrs.utility.Constantes;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constantes.API_VERSION)
@CrossOrigin(origins = { Constantes.ORIGINS })

// Controlador REST para gestionar los caballos
public class CaballoController {

    @Autowired
    CaballoService caballoService;

    // Endpoint GET para listar todos los caballos
    /**
     * GET /api/v1/caballos
     */
    @GetMapping(Constantes.RESOURCE_CABALLOS)
    public ResponseEntity<List<Caballo>> listarCaballos() {
        List<Caballo> caballos = caballoService.listarCaballos();
        return ResponseEntity.status(HttpStatus.OK).body(caballos);
    }

    // Endpoint GET para buscar un caballo por ID
    /**
     * GET /api/v1/caballos/{id}
     */
    @GetMapping(Constantes.RESOURCE_CABALLOS + "/{id}")
    public ResponseEntity<?> obtenerCaballoPorId(@PathVariable("id") Long id) {

        Caballo caballo = caballoService.buscarCaballoPorId(id)
                .orElseThrow(
                        () -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Caballo", "ID", id));
        return ResponseEntity.status(HttpStatus.OK).body(caballo);
    }

    // Endpoint POST para crear un nuevo caballo
    /**
     * POST /api/v1/caballos
     */
    @PostMapping(Constantes.RESOURCE_CABALLOS)
    public ResponseEntity<?> crearCaballo(@Valid @RequestBody CaballoDto caballoDto) {

        Caballo caballo = caballoService.crearCaballoDesdeDto(caballoDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Caballo " + caballo.getNombre() + " creado correctamente"));
    }

    // Endpoint PUT para actualizar un caballo por ID
    /**
     * PUT /api/v1/caballos/{id}
     */
    @PutMapping(Constantes.RESOURCE_CABALLOS + "/{id}")
    public ResponseEntity<?> actualizarCaballo(@PathVariable("id") Long id,
            @Valid @RequestBody CaballoDto caballoDto) {

        caballoService.actualizarCaballoDesdeDto(id, caballoDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Caballo con ID " + id + " actualizado correctamente"));
    }

    // Endpoint DELETE para eliminar un caballo por ID (Eliminación Física)
    /**
     * DELETE /api/v1/caballos/{id}
     */
    @DeleteMapping(Constantes.RESOURCE_CABALLOS + "/{id}")
    public ResponseEntity<?> eliminarCaballo(@PathVariable Long id) {

        caballoService.eliminarCaballo(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Caballo con ID " + id + " eliminado correctamente"));
    }

    // Endpoint DELETE para eliminar un caballo por nombre (Eliminación Lógica)
    /**
     * DELETE /api/v1/caballos/{id}/inactivar
     */
    @DeleteMapping(Constantes.RESOURCE_CABALLOS + "/{id}/inactivar")
    public ResponseEntity<?> eliminarCaballoTemporalmente(@PathVariable Long id) {
        // El Service maneja todas las validaciones
        caballoService.eliminarcaballoTemporalmente(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Caballo con ID " + id + " marcado como no disponible"));
    }

    // Endpoint GET para buscar por diferentes filtros
    /**
     * GET /api/v1/caballos/buscar
     */
    @GetMapping(Constantes.RESOURCE_CABALLOS + "/buscar")
    public ResponseEntity<?> buscarCaballo(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) Tipo tipo) {

        List<Caballo> caballos = caballoService.buscarCaballosConFiltros(nombre, disponible, tipo);

        if (!caballos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(caballos);
        }

        caballos = caballoService.listarCaballos();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje",
                "No existen caballos con los filtros de búsqueda ingresados, se retorna el listado completo.");
        respuesta.put("caballos", caballos);

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }
}
