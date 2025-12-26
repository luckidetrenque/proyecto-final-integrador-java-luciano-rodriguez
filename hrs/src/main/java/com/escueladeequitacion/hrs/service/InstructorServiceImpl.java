package com.escueladeequitacion.hrs.service;

import com.escueladeequitacion.hrs.model.Instructor;
import com.escueladeequitacion.hrs.repository.InstructorRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Implementación de la interfaz InstructorService
@Service
@Transactional
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    InstructorRepository instructorRepository;

    @Override
    public List<Instructor> listarInstructores() {
        return instructorRepository.findAll();
    };

    @Override
    public Optional<Instructor> buscarInstructorPorId(Long id) {
        return instructorRepository.findById(id);
    };

    @Override
    public Optional<Instructor> buscarInstructorPorDni(Integer dni) {
        return instructorRepository.findByDni(dni);
    };

    @Override
    public List<Instructor> buscarInstructorPorNombre(String nombre) {
        return instructorRepository.findByNombreIgnoreCase(nombre);
    }

    @Override
    public List<Instructor> buscarInstructorPorApellido(String apellido) {
        return instructorRepository.findByApellidoIgnoreCase(apellido);
    }

    @Override
    public List<Instructor> buscarInstructorPorNombreYApellido(String nombre, String apellido) {
        return instructorRepository.findByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public List<Instructor> buscarInstructorsPorEstado(Boolean activo) {
        return instructorRepository.findByActivo(activo);
    }

                @Override
    public List<Instructor> buscarPorFechaNacimiento(LocalDate fechaNacimiento) {
        return instructorRepository.findByFechaNacimiento(fechaNacimiento);
    };

    @Override
    public Boolean existeInstructorPorId(Long id) {
        return instructorRepository.existsById(id);
    };

    @Override
    public Boolean existeInstructorPorDni(Integer dni) {
        return instructorRepository.existsByDni(dni);
    };

    @Override
    public Boolean existeInstructorPorNombreYApellido(String nombre, String apellido) {
        return instructorRepository.existsByNombreAndApellidoIgnoreCase(nombre, apellido);
    };

    @Override
    public Boolean estadoInstructor(Long id) {
        Optional<Instructor> alumno = instructorRepository.findById(id);
        return alumno.map(Instructor::isActivo).orElse(false);
    }

    @Override
    public void guardarInstructor(Instructor alumno) {
        instructorRepository.save(alumno);
    };

    @Override
    public void actualizarInstructor(Long id, Instructor alumno) {
        alumno.setId(id);
        instructorRepository.save(alumno);
    };

    @Override
    public void eliminarInstructor(Long id) {
        instructorRepository.deleteById(id);
    };

    @Override
    public void eliminarInstructorTemporalmente(Long id) {
        Optional<Instructor> alumnoOpt = instructorRepository.findById(id);
        if (alumnoOpt.isPresent()) {
            Instructor alumno = alumnoOpt.get();
            alumno.setActivo(false);
            instructorRepository.save(alumno);
        }
    }
}
