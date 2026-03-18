package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.CaballoDto;
import com.escueladeequitacion.hrs.enums.Tipo;
import com.escueladeequitacion.hrs.model.Caballo;
import com.escueladeequitacion.hrs.service.CaballoService;
import com.escueladeequitacion.hrs.utility.Mensaje;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/caballos")

// Controlador REST para gestionar los caballos
public class CaballoController {

    @Autowired
    CaballoService caballoService;

    // Endpoint GET para listar todos los caballos
    /**
     * GET /api/v1/caballos
     */
    @GetMapping()
    public ResponseEntity<Page<Caballo>> listarCaballos(
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable,
            @RequestParam(name = "disponible", required = false) Boolean disponible,
            @RequestParam(name = "tipo", required = false) Tipo tipo,
            @RequestParam(name = "nombre", required = false) String nombre) {

        Page<Caballo> caballos = caballoService.listarCaballos(pageable, disponible, tipo, nombre);
        return ResponseEntity.ok(caballos);
    }

    // Endpoint GET para buscar un caballo por ID
    /**
     * GET /api/v1/caballos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCaballoPorId(@PathVariable("id") Long id) {

        Caballo caballo = caballoService.buscarCaballoConAlumnosPorId(id)
                .orElseThrow(
                        () -> new com.escueladeequitacion.hrs.exception.ResourceNotFoundException("Caballo", "ID", id));
        return ResponseEntity.status(HttpStatus.OK).body(caballo);
    }

    // Endpoint POST para crear un nuevo caballo
    /**
     * POST /api/v1/caballos
     */
    @PostMapping()
    public ResponseEntity<?> crearCaballo(@Valid @RequestBody CaballoDto caballoDto) {

        Caballo caballo = caballoService.crearCaballoDesdeDto(caballoDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Caballo " + caballo.getNombre() + " creado correctamente"));
    }

    // Endpoint PUT para actualizar un caballo por ID
    /**
     * PUT /api/v1/caballos/{id}
     */
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCaballo(@PathVariable("id") Long id) {

        caballoService.eliminarCaballo(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Caballo con ID " + id + " eliminado correctamente"));
    }

    // Endpoint DELETE para eliminar un caballo por nombre (Eliminación Lógica)
    /**
     * DELETE /api/v1/caballos/{id}/inactivar
     */
    @DeleteMapping("/{id}/inactivar")
    public ResponseEntity<?> eliminarCaballoTemporalmente(@PathVariable("id") Long id) {
        // El Service maneja todas las validaciones
        caballoService.eliminarcaballoTemporalmente(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Caballo con ID " + id + " marcado como no disponible"));
    }

}