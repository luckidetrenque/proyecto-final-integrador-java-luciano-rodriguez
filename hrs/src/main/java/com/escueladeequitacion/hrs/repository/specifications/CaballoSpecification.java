package com.escueladeequitacion.hrs.repository.specifications;

import com.escueladeequitacion.hrs.enums.Tipo;
import com.escueladeequitacion.hrs.model.Caballo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CaballoSpecification {

    private CaballoSpecification() {}

    public static Specification<Caballo> filtrar(
            Boolean disponible,
            Tipo tipo,
            String nombre) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (disponible != null) {
                predicates.add(cb.equal(root.get("disponible"), disponible));
            }
            if (tipo != null) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (nombre != null && !nombre.isBlank()) {
                predicates.add(cb.like(
                    cb.lower(root.get("nombre")),
                    "%" + nombre.toLowerCase().trim() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
