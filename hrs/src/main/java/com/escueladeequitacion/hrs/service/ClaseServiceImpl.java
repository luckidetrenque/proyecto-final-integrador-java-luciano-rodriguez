package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.enums.Especialidades;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Clase;
import com.escueladeequitacion.hrs.repository.ClaseRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

// Implementación de la interfaz ClaseService
@Service
@Transactional
public class ClaseServiceImpl implements ClaseService {

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    AlumnoService alumnoService;

    @Autowired
    InstructorService instructorService;

    @Autowired
    CaballoService caballoService;

    @Override
    public List<Clase> listarClases() {
        return claseRepository.findAll();
    }

    @Override
    public Optional<Clase> buscarClasePorId(Long id) {
        return claseRepository.findById(id);
    }
    
    @Override
    public List<Clase> buscarClasePorEspecialidad(Especialidades especialidad) {
        return claseRepository.findByEspecialidades(especialidad);
    }

    @Override
    public List<Clase> buscarClasePorDia(LocalDate dia) {
        return claseRepository.findByDia(dia);
    }

        @Override
    public List<Clase> buscarClasePorHora(LocalTime hora) {
        return claseRepository.findByHora(hora);
    }

    @Override
    public List<Clase> buscarClasePorEstado(Estado estado) {
        return claseRepository.findByEstado(estado);
    };

    @Override
public List<Clase> buscarClasePorInstructor(Long instructor_id) {
    return claseRepository.findByInstructorId(instructor_id);
};

@Override
    public List<Clase> buscarClasePorAlumno(Long alumno_id) {
        return claseRepository.findByAlumnoId(alumno_id);
    };

    @Override
    public List<Clase> buscarClasePorCaballo(Long caballo_id) {
        return claseRepository.findByCaballoId(caballo_id);
    };


    @Override
    public Boolean existeClasePorId(Long id) {
        return claseRepository.existsById(id);
    }

        @Override
    public Boolean existeClasePorEspecialidades(Especialidades especialidades) {
        return claseRepository.existsByEspecialidades(especialidades);
    };

        @Override
    public Boolean existeClasePorDia(LocalDate dia) {
        return claseRepository.existsByDia(dia);
    };

            @Override
    public Boolean existeClasePorHora(LocalTime hora) {
        return claseRepository.existsByHora(hora);
    };

@Override
        public Boolean existeClasePorInstructor(Long instructor_id) {
            return claseRepository.existsByInstructorId(instructor_id);
        };

        @Override
    public Boolean existeClasePorAlumno(Long alumno_id) {
        return claseRepository.existsByAlumnoId(alumno_id);
    };

    @Override
    public Boolean existeClasePorCaballo(Long caballo_id) {
        return claseRepository.existsByCaballoId(caballo_id);
    };


    @Override
    public void guardarClase(Clase clase) {
        claseRepository.save(clase);
    }


        @Override
    public void actualizarClase(Long id, Clase clase) {
        clase.setId(id);
        claseRepository.save(clase);
    };
    @Override
    public void eliminarClase(Long id) {
        claseRepository.deleteById(id);
    }

        @Override
    public void eliminarClaseTemporalmente(Long id) {
        Optional<Clase> claseOpt = claseRepository.findById(id);
        if (claseOpt.isPresent()) {
            Clase clase = claseOpt.get();
            clase.setEstado(Estado.CANCELADA);
            claseRepository.save(clase);
        }
    }
}