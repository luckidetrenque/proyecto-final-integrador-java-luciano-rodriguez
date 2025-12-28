package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.enums.TipoCaballo;
import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ConflictException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Caballo;
import com.escueladeequitacion.hrs.repository.CaballoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Implementación de la interfaz CaballoService
@Service
@Transactional
public class CaballoServiceImpl implements CaballoService {

    @Autowired
    CaballoRepository caballoRepository;

    @Override
    public List<Caballo> listarCaballos() {
        return caballoRepository.findAll();
    };

    @Override
    public Optional<Caballo> buscarCaballoPorId(Long id) {
        return caballoRepository.findById(id);
    };

    @Override
    public List<Caballo> buscarCaballoPorNombre(String nombre) {
        return caballoRepository.findByNombreIgnoreCase(nombre);
    };

    @Override
    public List<Caballo> buscarCaballoPorEstado(Boolean disponible) {
        return caballoRepository.findByDisponible(true);
    };

    public List<Caballo> buscarCaballoPorTipo(TipoCaballo tipoCaballo) {
        return caballoRepository.findByTipoCaballo(tipoCaballo);
    };

    @Override
    public Boolean existeCaballoPorId(Long id) {
        return caballoRepository.existsById(id);
    };

    @Override
    public Boolean existeCaballoPorNombre(String nombre) {
        return caballoRepository.existsByNombre(nombre);
    };

    @Override
    public Boolean estadoCaballo(Long id) {
        Optional<Caballo> caballo = caballoRepository.findById(id);
        return caballo.map(Caballo::isDisponible).orElse(false);
    };

    @Override
    public void guardarCaballo(Caballo caballo) {
        // Validar nombre duplicado solo al CREAR (cuando id es null)
        if (caballo.getId() == null) {
            if (caballoRepository.existsByNombre(caballo.getNombre())) {
                throw new ConflictException("Caballo", "nombre", caballo.getNombre());
            }
        }

        caballoRepository.save(caballo);
    }

    @Override
    public void actualizarCaballo(Long id, Caballo caballo) {
        // 1. Verificar que el caballo existe
        Caballo caballoExistente = obtenerCaballoOLanzarExcepcion(id);

        // 2. Si el nombre cambió, verificar que el nuevo nombre no esté en uso
        if (!caballoExistente.getNombre().equalsIgnoreCase(caballo.getNombre())) {
            if (caballoRepository.existsByNombre(caballo.getNombre())) {
                throw new ConflictException("Ya existe otro caballo con el nombre " + caballo.getNombre());
            }
        }

        // 3. Actualizar los campos
        actualizarCamposDesdeDto(caballoExistente, caballo);

        // 4. Guardar
        caballoRepository.save(caballoExistente);
    };

    @Override
    public void eliminarCaballo(Long id) {
        obtenerCaballoOLanzarExcepcion(id);
        caballoRepository.deleteById(id);
    };

@Override
public void eliminarCaballoTemporalmente(Long id) {
    // 1. Verificar que existe
Caballo caballo = obtenerCaballoOLanzarExcepcion(id);
    
    // 2. Verificar que está disponible
    if (!caballo.isDisponible()) {
        throw new BusinessException("El caballo con ID " + id + " ya no está disponible");
    }
    
    // 3. Marcar como no disponible
    caballo.setDisponible(false);
    caballoRepository.save(caballo);
}
    /**
     * Método auxiliar para validar que un caballo existe
     */
    private Caballo obtenerCaballoOLanzarExcepcion(Long id) {
        return caballoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caballo", "ID", id));
    }

    /**
     * Método auxiliar para mapear DTO a entidad existente.
     * Evita duplicar código de actualización de campos.
     */
    private void actualizarCamposDesdeDto(Caballo caballoExistente, Caballo caballoNuevo) {
        caballoExistente.setNombre(caballoNuevo.getNombre());
        caballoExistente.setDisponible(caballoNuevo.isDisponible());
        caballoExistente.setTipoCaballo(caballoNuevo.getTipoCaballo());
    }
}
