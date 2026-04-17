package com.escueladeequitacion.hrs.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio para la entidad User — el login ahora usa email como identificador.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUsername(String username);

    public Boolean existsByEmail(String email);

    public Boolean existsByUsername(String username);

    // Buscar usuario por DNI de persona (compatibilidad)
    Optional<User> findByPersonaDni(Integer personaDni);

    // Buscar por rol
    java.util.List<User> findByRol(RolSeguridad rol);
}