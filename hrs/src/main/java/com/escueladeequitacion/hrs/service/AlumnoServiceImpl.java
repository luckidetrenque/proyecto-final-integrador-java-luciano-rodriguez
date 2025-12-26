package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.repository.AlumnoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Implementación de la interfaz AlumnoService
@Service
@Transactional
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public List<Alumno> listarAlumnos() {
        return alumnoRepository.findAll();
    };

    @Override
    public Optional<Alumno> buscarAlumnoPorId(Long id) {
        return alumnoRepository.findById(id);
    };

    @Override
    public Optional<Alumno> buscarAlumnoPorDni(Integer dni) {
        return alumnoRepository.findByDni(dni);
    };

    @Override
    public List<Alumno> buscarAlumnoPorNombre(String nombre) {
        return alumnoRepository.findByNombreIgnoreCase(nombre);
    }

    @Override
    public List<Alumno> buscarAlumnoPorApellido(String apellido) {
        return alumnoRepository.findByApellidoIgnoreCase(apellido);
    }

    @Override
    public List<Alumno> buscarAlumnoPorNombreYApellido(String nombre, String apellido) {
        return alumnoRepository.findByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public List<Alumno> buscarAlumnoPorEstado(Boolean activo) {
        return alumnoRepository.findByActivo(activo);
    }

        @Override
    public List<Alumno> buscarAlumnoConCaballo(Boolean propietario) {
        return alumnoRepository.findByPropietario(propietario);
    }


    @Override
        public List<Alumno> buscarPorFechaInscripcion(LocalDate fechaInscripcion) {
            return alumnoRepository.findByFechaInscripcion(fechaInscripcion);
        };

            @Override
    public List<Alumno> buscarPorFechaNacimiento(LocalDate fechaNacimiento) {
        return alumnoRepository.findByFechaNacimiento(fechaNacimiento);
    };

    @Override
    public Boolean existeAlumnoPorId(Long id) {
        return alumnoRepository.existsById(id);
    };

    @Override
    public Boolean existeAlumnoPorDni(Integer dni) {
        return alumnoRepository.existsByDni(dni);
    };

    @Override
    public Boolean existeAlumnoPorNombreYApellido(String nombre, String apellido) {
        return alumnoRepository.existsByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public Boolean estadoAlumno(Long id) {
        Optional<Alumno> alumno = alumnoRepository.findById(id);
        return alumno.map(Alumno::isActivo).orElse(false);
    }

    @Override
    public void guardarAlumno(Alumno alumno) {
        alumnoRepository.save(alumno);
    };

    @Override
    public void actualizarAlumno(Long id, Alumno alumno) {
        alumno.setId(id);
        alumnoRepository.save(alumno);
    };

    @Override
    public void eliminarAlumno(Long id) {
        alumnoRepository.deleteById(id);
    };

    @Override
    public void eliminarAlumnoTemporalmente(Long id) {
        Optional<Alumno> alumnoOpt = alumnoRepository.findById(id);
        if (alumnoOpt.isPresent()) {
            Alumno alumno = alumnoOpt.get();
            alumno.setActivo(false);
            alumnoRepository.save(alumno);
        }
    }
}
