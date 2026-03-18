package com.escueladeequitacion.hrs.repository.specifications;

import com.escueladeequitacion.hrs.model.Alumno;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AlumnoSpecification {

    private AlumnoSpecification() {}

    public static Specification<Alumno> filtrar(
            Boolean activo,
            Boolean propietario,
            Integer cantidadClases,
            String nombre,
            String apellido) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }
            if (propietario != null) {
                predicates.add(cb.equal(root.get("propietario"), propietario));
            }
            if (cantidadClases != null) {
                predicates.add(cb.equal(root.get("cantidadClases"), cantidadClases));
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
