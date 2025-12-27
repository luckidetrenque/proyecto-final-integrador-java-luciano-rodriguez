package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.enums.TipoCaballo;
import com.escueladeequitacion.hrs.exception.ConflictException;
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
    }

    @Override
    public void guardarCaballo(Caballo caballo) {
        // Validar nombre duplicado
        if (caballo.getId() == null && caballoRepository.existsByNombre(caballo.getNombre())) {
            throw new ConflictException("Caballo", "nombre", caballo.getNombre());
        }

        caballoRepository.save(caballo);
    }

    @Override
    public void actualizarCaballo(Long id, Caballo caballo) {
        // Si es actualización, verificar que el nombre no esté usado por otro caballo
        if (caballo.getId() != null) {
            List<Caballo> existentes = caballoRepository.findByNombreIgnoreCase(caballo.getNombre());
            for (Caballo existente : existentes) {
                if (!existente.getId().equals(caballo.getId())) {
                    throw new ConflictException("Caballo", "nombre", caballo.getNombre());
                }
            }
        }
        caballo.setId(id);
        caballoRepository.save(caballo);
    };

    @Override
    public void eliminarCaballo(Long id) {
        caballoRepository.deleteById(id);
    }

    @Override
    public void eliminarCaballoTemporalmente(Long id) {
        Optional<Caballo> caballoOpt = caballoRepository.findById(id);
        if (caballoOpt.isPresent()) {
            Caballo caballo = caballoOpt.get();
            caballo.setDisponible(false);
            caballoRepository.save(caballo);
        }
    }
}
