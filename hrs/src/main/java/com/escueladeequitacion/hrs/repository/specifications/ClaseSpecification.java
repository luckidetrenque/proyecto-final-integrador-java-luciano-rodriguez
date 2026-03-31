package com.escueladeequitacion.hrs.repository.specifications;

import com.escueladeequitacion.hrs.enums.Especialidad;
import com.escueladeequitacion.hrs.enums.Estado;
import com.escueladeequitacion.hrs.model.Alumno;
import com.escueladeequitacion.hrs.model.Clase;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClaseSpecification {

    private ClaseSpecification() {}

    public static Specification<Clase> filtrar(
            Estado estado,
            Especialidad especialidad,
            String nombreAlumno,
            String apellidoAlumno,
            Long instructorId,
            Long alumnoId) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (estado != null) {
                predicates.add(cb.equal(root.get("estado"), estado));
            }
            if (especialidad != null) {
                predicates.add(cb.equal(root.get("especialidad"), especialidad));
            }
            if (instructorId != null) {
                predicates.add(cb.equal(root.get("instructor").get("id"), instructorId));
            }
            if (alumnoId != null) {
                predicates.add(cb.equal(root.get("alumno").get("id"), alumnoId));
            }

            // Búsqueda por nombre/apellido del alumno requiere JOIN
            if ((nombreAlumno != null && !nombreAlumno.isBlank()) ||
                (apellidoAlumno != null && !apellidoAlumno.isBlank())) {

                Join<Clase, Alumno> alumnoJoin = root.join("alumno", JoinType.LEFT);

                if (nombreAlumno != null && !nombreAlumno.isBlank()) {
                    predicates.add(cb.like(
                        cb.lower(alumnoJoin.get("nombre")),
                        "%" + nombreAlumno.toLowerCase().trim() + "%"
                    ));
                }
                if (apellidoAlumno != null && !apellidoAlumno.isBlank()) {
                    predicates.add(cb.like(
                        cb.lower(alumnoJoin.get("apellido")),
                        "%" + apellidoAlumno.toLowerCase().trim() + "%"
                    ));
                }

                // Evitar duplicados por el JOIN
                if (query != null) {
                    query.distinct(true);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
