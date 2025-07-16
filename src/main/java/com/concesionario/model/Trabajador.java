package com.concesionario.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "trabajadores")
public class Trabajador {
    @Id
    private String id;

    private String nombre;
    private String apellido;
    private String identificacion;
    private String correo;
    private String password;
    private Rol rol = Rol.TRABAJADOR;

    private LocalTime horaInicioTrabajo;
    private LocalTime horaFinTrabajo;
    private List<String> diasTrabajo; // Ej: ["LUNES", "MARTES", "MIERCOLES"]

    // Constructores, getters y setters
    public Trabajador() {
    }

    // Getters y setters para todos los campos
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

    public List<String> getDiasTrabajo() {
        return diasTrabajo;
    }

    public void setDiasTrabajo(List<String> diasTrabajo) {
        this.diasTrabajo = diasTrabajo;
    }

    public LocalTime getHoraFinTrabajo() {
        return horaFinTrabajo;
    }

    public void setHoraFinTrabajo(LocalTime horaFinTrabajo) {
        this.horaFinTrabajo = horaFinTrabajo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalTime getHoraInicioTrabajo() {
        return horaInicioTrabajo;
    }

    public void setHoraInicioTrabajo(LocalTime horaInicioTrabajo) {
        this.horaInicioTrabajo = horaInicioTrabajo;
    }
}