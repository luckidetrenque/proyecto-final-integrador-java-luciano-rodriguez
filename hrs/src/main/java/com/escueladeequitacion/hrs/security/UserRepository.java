package com.escueladeequitacion.hrs.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio para la entidad User
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);

    // Buscar usuario por DNI de persona
    Optional<User> findByPersonaDni(Integer personaDni);
}