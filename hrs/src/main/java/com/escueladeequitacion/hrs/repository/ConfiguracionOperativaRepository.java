package com.escueladeequitacion.hrs.repository;

import com.escueladeequitacion.hrs.model.ConfiguracionOperativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionOperativaRepository extends JpaRepository<ConfiguracionOperativa, Long> {
}
