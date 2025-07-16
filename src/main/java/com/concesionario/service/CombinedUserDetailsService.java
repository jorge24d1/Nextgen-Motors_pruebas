package com.concesionario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CombinedUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthService authService; // Tu servicio actual de usuarios

    @Autowired
    private TrabajadorDetailsService trabajadorDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Primero intenta con usuarios normales
            return authService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // Si no encuentra usuario, intenta con trabajador
            return trabajadorDetailsService.loadUserByUsername(username);
        }
    }
}