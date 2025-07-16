package com.concesionario.service;

import com.concesionario.model.Trabajador;
import com.concesionario.repository.TrabajadorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class TrabajadorService {
    private final TrabajadorRepository trabajadorRepository;
    private final PasswordEncoder passwordEncoder;

    public TrabajadorService(TrabajadorRepository trabajadorRepository,
                             PasswordEncoder passwordEncoder) {
        this.trabajadorRepository = trabajadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existeCorreo(String correo) {
        return trabajadorRepository.findByCorreo(correo).isPresent();
    }

    public boolean existeIdentificacion(String identificacion) {
        return trabajadorRepository.findByIdentificacion(identificacion).isPresent();
    }

    public void registrarTrabajador(String nombre, String apellido, String correo,
                                    String identificacion, String password,
                                    LocalTime horaInicio, LocalTime horaFin,
                                    List<String> diasTrabajo) {
        Trabajador trabajador = new Trabajador();
        trabajador.setNombre(nombre);
        trabajador.setApellido(apellido);
        trabajador.setCorreo(correo);
        trabajador.setIdentificacion(identificacion);
        trabajador.setPassword(passwordEncoder.encode(password));
        trabajador.setHoraInicioTrabajo(horaInicio);
        trabajador.setHoraFinTrabajo(horaFin);
        trabajador.setDiasTrabajo(diasTrabajo);

        trabajadorRepository.save(trabajador);
    }
}