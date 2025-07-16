package com.concesionario.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.concesionario.dto.UsuarioDTO;
import com.concesionario.dto.VehiculoDTO;

import java.time.LocalDateTime;

@Document(collection = "citas")
public class Cita {


    @Id
    private String id;

    private UsuarioDTO usuario;  // Embedded DTO
    private VehiculoDTO vehiculo;  // Embedded DTO

    private String nombres;
    private String apellidos;
    private String cedula;
    private String correoElectronico;
    


    private String modelo;
    private String vehiculoId;
    private String nombreVehiculo;
    private String colorVehiculo;
    private String telefono;
    private String tipo; // Pick-Ups, Camionetas, Automóviles, etc.
    

    private String sede;
    private String comentario;
    private boolean atendida = false;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private boolean leida = false;
    private LocalDateTime fechaAsignada;
    private String estado = "Pendiente";

    private String notasAdmin;
    private String trabajadorId;
    private String fechaCita;
    private String horaCita;

    public String getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(String trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    // Constructor vacío
    public Cita() {
    }

    // Constructor con parámetros (sin fechaCreacion ni leida)
    public Cita(String nombres, String apellidos, String cedula, String correoElectronico,
                String telefono, String sede, String tipo, String vehiculoId,
                String nombreVehiculo, String comentario) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.sede = sede;
        this.tipo = tipo;
        this.vehiculoId = vehiculoId;
        this.nombreVehiculo = nombreVehiculo;
        this.comentario = comentario;
    }


    // otros setters y getters para dtos
    public UsuarioDTO getUsuario() {
        return usuario;
    }
    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
    public VehiculoDTO getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(VehiculoDTO vehiculo) {
        this.vehiculo = vehiculo;
    }


    // Getters y Setters (incluyendo los nuevos campos)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(String vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public String getNombreVehiculo() {
        return nombreVehiculo;
    }

    public void setNombreVehiculo(String nombreVehiculo) {
        this.nombreVehiculo = nombreVehiculo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public LocalDateTime getFechaAsignada() {
        return fechaAsignada;
    }

    public void setFechaAsignada(LocalDateTime fechaAsignada) {
        this.fechaAsignada = fechaAsignada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotasAdmin() {
        return notasAdmin;
    }

    public void setNotasAdmin(String notasAdmin) {
        this.notasAdmin = notasAdmin;
    }
    public String getColorVehiculo() {
        return colorVehiculo;
    }

    public void setColorVehiculo(String colorVehiculo) {
        this.colorVehiculo = colorVehiculo;
    }
    
}
