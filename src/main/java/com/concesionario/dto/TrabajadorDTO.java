package com.concesionario.dto;

import java.time.LocalTime;
import java.util.List;

public class TrabajadorDTO {
    private String nombre;
    private String apellido;
    private String identificacion;
    private String correo;
    private String password;
    private LocalTime horaInicioTrabajo;
    private LocalTime horaFinTrabajo;
    private List<String> diasTrabajo;

    public TrabajadorDTO() {
    }

    public TrabajadorDTO(String nombre, String apellido, String identificacion, String correo, String password, LocalTime horaInicioTrabajo, LocalTime horaFinTrabajo, List<String> diasTrabajo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.identificacion = identificacion;
        this.correo = correo;
        this.password = password;
        this.horaInicioTrabajo = horaInicioTrabajo;
        this.horaFinTrabajo = horaFinTrabajo;
        this.diasTrabajo = diasTrabajo;
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

    public LocalTime getHoraInicioTrabajo() {
        return horaInicioTrabajo;
    }

    public void setHoraInicioTrabajo(LocalTime horaInicioTrabajo) {
        this.horaInicioTrabajo = horaInicioTrabajo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalTime getHoraFinTrabajo() {
        return horaFinTrabajo;
    }

    public void setHoraFinTrabajo(LocalTime horaFinTrabajo) {
        this.horaFinTrabajo = horaFinTrabajo;
    }

    public List<String> getDiasTrabajo() {
        return diasTrabajo;
    }

    public void setDiasTrabajo(List<String> diasTrabajo) {
        this.diasTrabajo = diasTrabajo;
    }
}