package com.escueladeequitacion.hrs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.escueladeequitacion.hrs.model.Clase;
import com.escueladeequitacion.hrs.model.Instructor;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.repository.ClaseRepository;
import com.escueladeequitacion.hrs.repository.InstructorRepository;
import com.escueladeequitacion.hrs.repository.AlumnoRepository;

@Service("claseSecurityService")
public class ClaseSecurityService {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean puedeModificar(Long claseId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        // ADMIN puede todo
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // Obtener el User logueado
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null || user.getPersonaDni() == null) {
            return false;
        }

        // Buscar el Instructor que tiene ese DNI
        Instructor instructor = instructorRepository
                .findByDni(user.getPersonaDni().toString()).orElse(null);
        if (instructor == null) {
            return false;
        }

        // Verificar que la clase pertenece a ese instructor
        Clase clase = claseRepository.findById(claseId).orElse(null);
        if (clase == null) {
            return false;
        }

        return clase.getInstructor() != null && clase.getInstructor().getId().equals(instructor.getId());
    }

    public Long getInstructorId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        
        String username = auth.getName();
        
        // 1. Intentar buscar primero por el Nombre de Usuario directo (si es el email)
        Instructor instructorByUsername = instructorRepository.findByEmailIgnoreCase(username).orElse(null);
        if (instructorByUsername != null) {
            return instructorByUsername.getId();
        }

        // 2. Obtener el objeto User para buscar por su email interno y DNI
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            // 2a. Buscar por email del User (caso de que username != email)
            if (user.getEmail() != null) {
                Instructor instructorByEmail = instructorRepository.findByEmailIgnoreCase(user.getEmail()).orElse(null);
                if (instructorByEmail != null) {
                    return instructorByEmail.getId();
                }
            }
            
            // 2b. Buscar por DNI (Fallback tradicional)
            if (user.getPersonaDni() != null) {
                Instructor instructorByDni = instructorRepository.findByDni(user.getPersonaDni().toString()).orElse(null);
                if (instructorByDni != null) {
                    return instructorByDni.getId();
                }
            }
        }

        return null;
    }

    public boolean esElMismoInstructor(Long instructorId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return false;
        
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        Long id = getInstructorId(auth);
        return id != null && id.equals(instructorId);
    }

    public boolean esElMismoAlumno(Long alumnoId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        // ADMIN e INSTRUCTOR pueden ver datos
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_INSTRUCTOR"))) {
            return true;
        }

        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null || user.getPersonaDni() == null) {
            return false;
        }

        Alumno alumno = alumnoRepository.findByDni(user.getPersonaDni().toString()).orElse(null);
        if (alumno == null) {
            return false;
        }

        return alumno.getId().equals(alumnoId);
    }

    public boolean esElMismoAlumnoPorDni(String dni, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return false;
        
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_INSTRUCTOR"))) {
            return true;
        }
        
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null || user.getPersonaDni() == null) return false;
        
        return user.getPersonaDni().toString().equals(dni);
    }
}
