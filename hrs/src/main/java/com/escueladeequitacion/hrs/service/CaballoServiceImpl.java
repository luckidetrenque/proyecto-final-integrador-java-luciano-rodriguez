package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.dto.CaballoDto;
import com.escueladeequitacion.hrs.enums.Tipo;
import com.escueladeequitacion.hrs.exception.BusinessException;
import com.escueladeequitacion.hrs.exception.ConflictException;
import com.escueladeequitacion.hrs.exception.ResourceNotFoundException;
import com.escueladeequitacion.hrs.model.Caballo;
import com.escueladeequitacion.hrs.repository.CaballoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Caballo> buscarCaballoPorTipo(Tipo tipo) {
        return caballoRepository.findBytipo(tipo);
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
    public void eliminarcaballoTemporalmente(Long id) {
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
     * Crea un caballo desde un DTO con validaciones.
     */
    @Override
    public Caballo crearCaballoDesdeDto(CaballoDto caballoDto) {
        // Validar nombre duplicado
        if (caballoRepository.existsByNombre(caballoDto.getNombre())) {
            throw new ConflictException("Caballo", "nombre", caballoDto.getNombre());
        }

        // Crear el caballo
        Caballo caballo = new Caballo(
                caballoDto.getNombre(),
                caballoDto.isDisponible(),
                caballoDto.gettipo());

        return caballoRepository.save(caballo);
    }

    /**
     * Actualiza un caballo desde un DTO con validaciones.
     */
    @Override
    public void actualizarCaballoDesdeDto(Long id, CaballoDto caballoDto) {
        // 1. Obtener caballo existente
        Caballo caballoExistente = caballoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caballo", "ID", id));

        // 2. Validar nombre duplicado solo si cambió
        if (!caballoExistente.getNombre().equalsIgnoreCase(caballoDto.getNombre())) {
            if (caballoRepository.existsByNombre(caballoDto.getNombre())) {
                throw new ConflictException("Ya existe otro caballo con el nombre " + caballoDto.getNombre());
            }
        }

        // 3. Crear objeto temporal
        Caballo caballoNuevo = new Caballo();
        caballoNuevo.setNombre(caballoDto.getNombre());
        caballoNuevo.setDisponible(caballoDto.isDisponible());
        caballoNuevo.settipo(caballoDto.gettipo());

        // 4. Actualizar usando método auxiliar
        actualizarCamposDesdeDto(caballoExistente, caballoNuevo);

        // 5. Guardar
        caballoRepository.save(caballoExistente);
    }

    /**
     * Busca caballos con múltiples filtros.
     */
    @Override
    public List<Caballo> buscarCaballosConFiltros(String nombre, Boolean disponible, Tipo tipo) {
        if (nombre != null) {
            return caballoRepository.findByNombreIgnoreCase(nombre);
        }

        if (disponible != null) {
            return caballoRepository.findByDisponible(disponible);
        }

        if (tipo != null) {
            return caballoRepository.findBytipo(tipo);
        }

        return new ArrayList<>();
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
        caballoExistente.settipo(caballoNuevo.gettipo());
    }
}
