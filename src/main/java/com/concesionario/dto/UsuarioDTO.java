package com.concesionario.dto;

public class UsuarioDTO {
    private String id;
    private String nombre;
    private String apellido;
    private String identificacion;
    private String correo;
    
    // Constructor vacío
    public UsuarioDTO() {
    }

    // Constructor con parámetros
    public UsuarioDTO(String id, String nombre, String apellido, String identificacion, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.identificacion = identificacion;
        this.correo = correo;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
