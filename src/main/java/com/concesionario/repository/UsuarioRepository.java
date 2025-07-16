package com.concesionario.repository;

import com.concesionario.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByCorreoUser(String correo);
    boolean existsByCorreoUser(String correo);
    boolean existsByIdentificacionUser(String identificacion);
    Optional<Usuario> findByResetPasswordToken(String token);
    
}
