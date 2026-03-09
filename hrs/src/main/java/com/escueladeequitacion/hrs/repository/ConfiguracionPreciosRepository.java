package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.ConfiguracionPrecios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionPreciosRepository
        extends JpaRepository<ConfiguracionPrecios, Long> {
    // El registro singleton siempre tiene id = 1.
    // Usamos findById(1L) directamente desde el service.
}