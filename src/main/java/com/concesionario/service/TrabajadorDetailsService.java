package com.concesionario.service;

import com.concesionario.model.Trabajador;
import com.concesionario.repository.TrabajadorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// TrabajadorDetailsService.java
@Service
public class TrabajadorDetailsService implements UserDetailsService {

    private final TrabajadorRepository trabajadorRepository;

    public TrabajadorDetailsService(TrabajadorRepository trabajadorRepository) {
        this.trabajadorRepository = trabajadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Trabajador trabajador = trabajadorRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Trabajador no encontrado"));

        return User.builder()
                .username(trabajador.getCorreo())
                .password(trabajador.getPassword())
                .roles("TRABAJADOR")
                .build();
    }
}