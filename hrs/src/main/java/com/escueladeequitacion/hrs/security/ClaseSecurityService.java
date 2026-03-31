package com.escueladeequitacion.hrs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    /**
     * Verifica si el usuario autenticado puede modificar una clase.
     * ADMIN: siempre puede.
     * INSTRUCTOR: solo si la clase le pertenece.
     * Si el usuario es INSTRUCTOR pero no tiene perfil vinculado → 403 explícito.
     */
    public boolean puedeModificar(Long claseId, Authentication auth) {
        if (claseId == null || auth == null || !auth.isAuthenticated()) {
            return false;
        }

        if (esAdmin(auth))
            return true;

        if (esInstructor(auth)) {
            Long instructorId = getInstructorId(auth);
            if (instructorId == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Tu cuenta de instructor no está vinculada a un perfil. Contactá al administrador.");
            }
            return claseRepository.findById(claseId)
                    .map(clase -> {
                        Long classInstructorId = (clase.getInstructor() != null)
                                ? clase.getInstructor().getId()
                                : null;
                        return instructorId.equals(classInstructorId);
                    })
                    .orElse(false);
        }

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ALUMNO"))) {
            Long alumnoId = getAlumnoId(auth);
            if (alumnoId == null) {
                return false;
            }
            return claseRepository.findById(claseId)
                    .map(clase -> {
                        Long classAlumnoId = (clase.getAlumno() != null)
                                ? clase.getAlumno().getId()
                                : null;
                        return alumnoId.equals(classAlumnoId);
                    })
                    .orElse(false);
        }

        return false;
    }

    /**
     * Resuelve el ID del Instructor a partir de la autenticación.
     * Estrategia: email del username → email del User → DNI del User.
     */
    public Long getInstructorId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return null;

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        // NUEVO: campo directo — estrategia 0
        if (user != null && user.getInstructorId() != null) {
            return user.getInstructorId();
        }

        // Fallback 1: username == email del instructor
        Instructor byUsername = instructorRepository.findByEmailIgnoreCase(username).orElse(null);
        if (byUsername != null)
            return byUsername.getId();

        if (user != null) {
            // Fallback 2: email del User
            if (user.getEmail() != null) {
                Instructor byEmail = instructorRepository
                        .findByEmailIgnoreCase(user.getEmail()).orElse(null);
                if (byEmail != null)
                    return byEmail.getId();
            }
            // Fallback 3: DNI
            if (user.getPersonaDni() != null) {
                Instructor byDni = instructorRepository
                        .findByDni(user.getPersonaDni().toString()).orElse(null);
                if (byDni != null)
                    return byDni.getId();
            }
        }

        return null;
    }

    /**
     * Verifica si el usuario autenticado ES ese instructor.
     * ADMIN: siempre true.
     */
    public boolean esElMismoInstructor(Long instructorId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return false;
        if (esAdmin(auth))
            return true;

        Long id = getInstructorId(auth);
        return id != null && id.equals(instructorId);
    }

    /**
     * Verifica si el usuario autenticado puede acceder a los datos de un alumno.
     * ADMIN: siempre true.
     * INSTRUCTOR: siempre true (ve todos los alumnos de sus clases).
     * ALUMNO: solo si es él mismo (match por DNI).
     */
    public boolean esElMismoAlumno(Long alumnoId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return false;

        // ADMIN e INSTRUCTOR tienen acceso
        if (esAdmin(auth) || esInstructor(auth))
            return true;

        // ALUMNO: verificar que el alumnoId corresponde a su perfil
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null || user.getPersonaDni() == null)
            return false;

        Alumno alumno = alumnoRepository.findByDni(user.getPersonaDni().toString()).orElse(null);
        if (alumno == null)
            return false;

        return alumno.getId().equals(alumnoId);
    }

    /**
     * Igual que esElMismoAlumno pero recibe el DNI directamente.
     */
    public boolean esElMismoAlumnoPorDni(String dni, Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return false;
        if (esAdmin(auth) || esInstructor(auth))
            return true;

        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null || user.getPersonaDni() == null)
            return false;

        return user.getPersonaDni().toString().equals(dni);
    }

    /**
     * Resuelve el ID del Alumno a partir de la autenticación.
     * Usado principalmente para que el ALUMNO acceda a sus propios datos.
     * Retorna null si el usuario no es ALUMNO o no tiene perfil vinculado.
     */
    public Long getAlumnoId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return null;

        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null)
            return null;

        // NUEVO: campo directo — estrategia 0
        if (user.getAlumnoId() != null)
            return user.getAlumnoId();

        // Fallback: por DNI
        if (user.getPersonaDni() != null) {
            Alumno alumno = alumnoRepository
                    .findByDni(user.getPersonaDni().toString()).orElse(null);
            if (alumno != null)
                return alumno.getId();
        }

        return null;
    }

    // ── Helpers privados ─────────────────────────────────────────

    public boolean esAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean esInstructor(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"));
    }

    public boolean esAdminOrInstructor(Authentication auth) {
        return esAdmin(auth) || esInstructor(auth);
    }
}