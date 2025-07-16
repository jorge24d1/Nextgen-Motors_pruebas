package com.concesionario.service;


// import com.concesionario.model.Trabajadores;
import com.concesionario.model.Usuario;
// import com.concesionario.repository.TrabajadoresRepository;
import com.concesionario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // @Autowired
    // private TrabajadoresRepository trabajadoresRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

    public void initiatePasswordReset(String email) {
        // Buscar primero en usuarios
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoUser(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = generateToken();
            usuario.setResetPasswordToken(token);
            usuario.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(EXPIRE_TOKEN_AFTER_MINUTES));
            usuarioRepository.save(usuario);
            sendResetEmail(usuario.getCorreoUser(), token, "usuario");
            return;
        }

        // Si no es usuario, buscar en trabajadores
        // Optional<Trabajadores> trabajadorOpt = trabajadoresRepository.findByCorreoTAM(email);

        // if (trabajadorOpt.isPresent()) {
        //     Trabajadores trabajador = trabajadorOpt.get();
        //     String token = generateToken();
        //     trabajador.setResetPasswordToken(token);
        //     trabajador.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(EXPIRE_TOKEN_AFTER_MINUTES));
        //     trabajadoresRepository.save(trabajador);
        //     sendResetEmail(trabajador.getCorreoTAM(), token, "trabajador");
        //     return;
        // }

        throw new RuntimeException("No se encontró ninguna cuenta con ese correo electrónico");
    }

    public void resetPassword(String token, String newPassword) {
        // Buscar en usuarios
        Optional<Usuario> usuarioOpt = usuarioRepository.findByResetPasswordToken(token);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (isTokenExpired(usuario.getResetPasswordTokenExpiry())) {
                throw new RuntimeException("El token ha expirado");
            }
            // Encriptar la nueva contraseña
            usuario.setPasswordUser(passwordEncoder.encode(newPassword));
            usuario.setResetPasswordToken(null);
            usuario.setResetPasswordTokenExpiry(null);
            usuarioRepository.save(usuario);
            return;
        }

        // Buscar en trabajadores
        // Optional<Trabajadores> trabajadorOpt = trabajadoresRepository.findByResetPasswordToken(token);

        // if (trabajadorOpt.isPresent()) {
        //     Trabajadores trabajador = trabajadorOpt.get();
        //     if (isTokenExpired(trabajador.getResetPasswordTokenExpiry())) {
        //         throw new RuntimeException("El token ha expirado");
        //     }
        //     // Encriptar la nueva contraseña
        //     trabajador.setPassword(passwordEncoder.encode(newPassword));
        //     trabajador.setResetPasswordToken(null);
        //     trabajador.setResetPasswordTokenExpiry(null);
        //     trabajadoresRepository.save(trabajador);
        //     return;
        // }

        throw new RuntimeException("Token inválido");
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private boolean isTokenExpired(LocalDateTime tokenExpiry) {
        return tokenExpiry == null || LocalDateTime.now().isAfter(tokenExpiry);
    }

    private void sendResetEmail(String email, String token, String tipoUsuario) {
        String resetLink = baseUrl + "/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Restablecimiento de contraseña");
        message.setText("Para restablecer tu contraseña, haz clic en el siguiente enlace: " + resetLink +
                "\n\nEste enlace expirará en 30 minutos.");

        mailSender.send(message);
    }
}
