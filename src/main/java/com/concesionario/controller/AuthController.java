package com.concesionario.controller;

import com.concesionario.model.Usuario;
import com.concesionario.model.Rol;
import com.concesionario.repository.UsuarioRepository;
import com.concesionario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private final UsuarioService usuarioService;


    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) boolean error, Model model) {
        if (error) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String showRegister() {
        return "usuario/loginup";
    }

    @PostMapping("/registro")
    public String registerUser(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String identificacion,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        if (usuarioService.existeIdentificacionEnCualquierTabla(identificacion)) {
            model.addAttribute("error", "La identificación ya está registrada (como usuario o trabajador)");
            model.addAttribute("nombre", nombre);
            model.addAttribute("apellido", apellido);
            model.addAttribute("identificacion", identificacion);
            model.addAttribute("email", email);
            return "usuario/loginup";
        }

        if (usuarioService.existeCorreoEnCualquierTabla(email)) {
            model.addAttribute("error", "El correo ya está registrado (como usuario o trabajador)");
            model.addAttribute("nombre", nombre);
            model.addAttribute("apellido", apellido);
            model.addAttribute("identificacion", identificacion);
            model.addAttribute("email", email);
            return "usuario/loginup";
        }

        // Validar correo existente
        if (usuarioRepository.existsByCorreoUser(email)) {
            model.addAttribute("error", "El correo ya está registrado");
            return "usuario/loginup";
        }

        // Validar identificación existente
        if (usuarioRepository.existsByIdentificacionUser(identificacion)) {
            model.addAttribute("error", "La identificación ya está registrada");
            return "usuario/loginup";
        }

        try {
            usuarioService.registrarUsuario(
                    nombre,
                    apellido,
                    email,
                    identificacion,
                    password,
                    Rol.USUARIO
            );
            return "redirect:/login?success=Registro+exitoso";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("apellido", apellido);
            model.addAttribute("email", email);
            model.addAttribute("identificacion", identificacion);
            return "redirect:/login?success=true";
        }
    }

    @GetMapping("/registro-admin")
    public String mostrarRegistroAdmin() {
        return "usuario/registro-admin";
    }

    @PostMapping("/registro-admin")
    public String registrarAdmin(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String identificacion,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        if (usuarioRepository.existsByCorreoUser(email)) {
            model.addAttribute("error", "El correo ya está registrado");
            return "usuario/registro-admin";
        }

        Usuario admin = new Usuario();
        admin.setNombreUser(nombre);
        admin.setApellidoUser(apellido);
        admin.setIdentificacionUser(identificacion);
        admin.setCorreoUser(email);
        admin.setPasswordUser(passwordEncoder.encode(password));
        admin.setRol(Rol.ADMINISTRADOR); // Rol específico para admin


        usuarioRepository.save(admin);

        return "redirect:/login?adminRegistrado=true";
    }
    
}

