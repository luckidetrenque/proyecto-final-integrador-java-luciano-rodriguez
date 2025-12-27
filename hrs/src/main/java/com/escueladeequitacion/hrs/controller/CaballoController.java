package com.escueladeequitacion.hrs.controller;

import com.escueladeequitacion.hrs.dto.CaballoDto;
import com.escueladeequitacion.hrs.enums.TipoCaballo;
import com.escueladeequitacion.hrs.model.Caballo;
import com.escueladeequitacion.hrs.service.CaballoService;
import com.escueladeequitacion.hrs.utility.Mensaje;
import com.escueladeequitacion.hrs.utility.Constantes;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @GetMapping(Constantes.RESOURCE_CABALLOS)
    public ResponseEntity<List<Caballo>> listarCaballos() {
        List<Caballo> caballos = caballoService.listarCaballos();
        return ResponseEntity.status(HttpStatus.OK).body(caballos);
    }

    // Endpoint GET para buscar un caballo por ID
    @GetMapping(Constantes.RESOURCE_CABALLOS + "/{id}")
    public ResponseEntity<?> obtenerCaballoPorId(@PathVariable("id") Long id) {
        if (!caballoService.existeCaballoPorId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("El caballo con ID " + id + " no existe en la base de datos"));
        }

        Caballo caballo = caballoService.buscarCaballoPorId(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(caballo);
    }

    // Endpoint POST para crear un nuevo caballo
    @PostMapping(Constantes.RESOURCE_CABALLOS)
    public ResponseEntity<?> crearCaballo(@Valid @RequestBody CaballoDto caballoDto) {
        if (caballoService.existeCaballoPorNombre(caballoDto.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Mensaje("Ya existe un caballo con el nombre "
                    + caballoDto.getNombre() + " en la base de datos"));
        }
        Caballo caballo = new Caballo(caballoDto.getNombre(), caballoDto.isDisponible(),
                caballoDto.getTipoCaballo());
        caballoService.guardarCaballo(caballo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Mensaje("Caballo " + caballoDto.getNombre() + " creado correctamente"));
    }

    // Endpoint PUT para actualizar un caballo por ID
    @PutMapping(Constantes.RESOURCE_CABALLOS + "/{id}")
    public ResponseEntity<?> actualizarCaballo(@PathVariable("id") Long id,
            @Valid @RequestBody CaballoDto caballoDto) {
        if (!caballoService.existeCaballoPorId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("El caballo con ID " + id + " no existe en la base de datos"));
        }
        if (!caballoService
                .existeCaballoPorNombre(caballoDto.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Mensaje(
                            "El caballo con nombre " + caballoDto.getNombre() + " no existe en la base de datos"));
        }

        Caballo caballo = caballoService.buscarCaballoPorId(id).get();
        caballo.setNombre(caballoDto.getNombre());
        caballo.setDisponible(caballoDto.isDisponible());
        caballo.setTipoCaballo(caballoDto.getTipoCaballo());
        caballoService.guardarCaballo(caballo);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Caballo con ID " + id + " actualizado correctamente"));

    }

    // Endpoint DELETE para eliminar un caballo por ID (Eliminación Física)
    @DeleteMapping(Constantes.RESOURCE_CABALLOS + "/{id}")
    public ResponseEntity<?> eliminarCaballo(@PathVariable Long id) {
        if (!caballoService.existeCaballoPorId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("El caballo con ID " + id + " no existe en la base de datos"));
        }

        caballoService.eliminarCaballo(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Caballo con ID " + id + " eliminado correctamente (forma física)"));

    }

    // Endpoint DELETE para eliminar un caballo por nombre (Eliminación Lógica)
    @DeleteMapping(Constantes.RESOURCE_CABALLOS + "/{id}/inactivar")
    public ResponseEntity<?> eliminarCaballoTemporalmente(@PathVariable Long id) {
        if (!caballoService.existeCaballoPorId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el caballo con ese id"));
        }

        if (!caballoService.estadoCaballo(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("\"El caballo con ID " + id + " no está disponible"));
        }
        caballoService.eliminarCaballo(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Mensaje("Caballo con ID " + id + " eliminado correctamente (forma lógica)"));

    }

    @GetMapping(Constantes.RESOURCE_CABALLOS + "/buscar")
    public ResponseEntity<?> buscarCaballo(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) TipoCaballo tipo) {

        List<Caballo> caballos = new ArrayList<>();

        if (nombre != null) {
            caballos = caballoService.buscarCaballoPorNombre(nombre);

        }

        if (disponible != null && disponible) {
            caballos = caballoService.buscarCaballoPorEstado(true);

        }

        if (disponible != null && !disponible) {
            caballos = caballoService.buscarCaballoPorEstado(false);

        }

        if (tipo != null) {
            caballos = caballoService.buscarCaballoPorTipo(tipo);

        }

        if (!caballos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(caballos);

        }

        caballos = caballoService.listarCaballos();

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje",
                "No existen caballos con los filtros de búsqueda ingresados, se retorna el listado completo.");
        respuesta.put("caballos", caballos);

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body(new Mensaje("No existen alumnos con los filtros de búsqueda
        // ingresados"));
    }
}
