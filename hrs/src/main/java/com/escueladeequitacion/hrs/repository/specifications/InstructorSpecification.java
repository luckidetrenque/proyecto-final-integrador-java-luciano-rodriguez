package com.escueladeequitacion.hrs.repository.specifications;

import com.escueladeequitacion.hrs.model.Instructor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InstructorSpecification {

    private InstructorSpecification() {}

    public static Specification<Instructor> filtrar(
            Boolean activo,
            String nombre,
            String apellido) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }
            if (nombre != null && !nombre.isBlank()) {
                predicates.add(cb.like(
                    cb.lower(root.get("nombre")),
                    "%" + nombre.toLowerCase().trim() + "%"
                ));
            }
            if (apellido != null && !apellido.isBlank()) {
                predicates.add(cb.like(
                    cb.lower(root.get("apellido")),
                    "%" + apellido.toLowerCase().trim() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
