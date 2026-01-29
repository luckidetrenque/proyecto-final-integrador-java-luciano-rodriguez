package com.escueladeequitacion.hrs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.escueladeequitacion.hrs.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Servicio para cargar usuarios desde la base de datos.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!user.getActivo()) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRol().name())))
                .build();
    }
}