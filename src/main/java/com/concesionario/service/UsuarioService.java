package com.concesionario.service;



import com.concesionario.model.Rol;
import com.concesionario.model.Usuario;

// import com.concesionario.repository.TrabajadoresRepository;

import com.concesionario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // @Autowired
    // private TrabajadoresRepository trabajadoresRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // @Transactional
    public void registrarUsuario(String nombre, String apellido,
                                 String email, String identificacion,
                                 String password, Rol rol) {

        // Encriptar la contraseña una sola vez
        String passwordEncriptado = passwordEncoder.encode(password);

        Usuario usuario = new Usuario();
        usuario.setNombreUser(nombre);
        usuario.setApellidoUser(apellido);
        usuario.setCorreoUser(email);
        usuario.setIdentificacionUser(identificacion);
        usuario.setPasswordUser(passwordEncriptado);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setRol(rol);

        usuarioRepository.save(usuario);
    }
    public long contarUsuarios() {
        return usuarioRepository.count();
    }
// ||trabajadoresRepository.existsByCorreoTAM(correo);
    public boolean existeCorreoEnCualquierTabla(String correo) {
        return usuarioRepository.existsByCorreoUser(correo);
                
    }
//  ||trabajadoresRepository.existsByIdentificacion(identificacion);
    public boolean existeIdentificacionEnCualquierTabla(String identificacion) {
        return usuarioRepository.existsByIdentificacionUser(identificacion);
                
    }
    
    public Usuario findByCorreoUser(String correo) {
        return usuarioRepository.findByCorreoUser(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + correo));
    }

}
