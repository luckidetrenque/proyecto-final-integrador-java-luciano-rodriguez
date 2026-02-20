package com.escueladeequitacion.hrs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escueladeequitacion.hrs.dto.PersonaPruebaDto;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.PersonaPrueba;
import com.escueladeequitacion.hrs.repository.PersonaPruebaRepository;
import com.escueladeequitacion.hrs.utility.Constantes;

import jakarta.validation.Valid;

@RestController
@RequestMapping(Constantes.API_VERSION)
@CrossOrigin(origins = { Constantes.ORIGINS })
public class PersonaPruebaController {

    @Autowired
    private PersonaPruebaRepository personaPruebaRepository;

    @PostMapping("/personas-prueba")
    public ResponseEntity<?> crear(@Valid @RequestBody PersonaPruebaDto dto) {
        PersonaPrueba p = new PersonaPrueba(dto.getNombre(), dto.getApellido());
        PersonaPrueba guardada = personaPruebaRepository.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/personas-prueba/{id}")
    public ResponseEntity<PersonaPrueba> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonaPruebaDto dto) {

        // Buscar la persona por ID
        PersonaPrueba existente = personaPruebaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PersonaPrueba", "ID", id));

        // Actualizar campos
        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());

        // Guardar cambios
        PersonaPrueba actualizado = personaPruebaRepository.save(existente);

        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/personas-prueba")
    public ResponseEntity<List<PersonaPrueba>> listar() {
        return ResponseEntity.ok(personaPruebaRepository.findAll());
    }

    @GetMapping("/personas-prueba/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        PersonaPrueba p = personaPruebaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PersonaPrueba", "ID", id));
        return ResponseEntity.ok(p);
    }
}
