package com.concesionario.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaCreacion;

    private String nombreUser;
    private String apellidoUser;
    private String identificacionUser; // Cambiado a minúscula
    private String correoUser; // Cambiado a minúscula
    private String passwordUser; // Cambiado a minúscula


    private Rol rol = Rol.USUARIO;


    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;


    // Getters y Setters
    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public LocalDateTime getResetPasswordTokenExpiry() {
        return resetPasswordTokenExpiry;
    }

    public void setResetPasswordTokenExpiry(LocalDateTime resetPasswordTokenExpiry) {
        this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
    }

    // Constructor vacío
    public Usuario() {
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getApellidoUser() {
        return apellidoUser;
    }

    public void setApellidoUser(String apellidoUser) {
        this.apellidoUser = apellidoUser;
    }

    public String getIdentificacionUser() {
        return identificacionUser;
    }

    public void setIdentificacionUser(String identificacionUser) {
        this.identificacionUser = identificacionUser;
    }

    public String getCorreoUser() {
        return correoUser;
    }

    public void setCorreoUser(String correoUser) {
        this.correoUser = correoUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    
}
