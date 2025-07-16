package com.concesionario.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehiculos")
public class Vehiculo {
    @Id
    private String id;

    private String marca;
    private String modelo;
    private int año;
    private double precio;
    private String categoria;
    private String imagenUrl;

    private String motor;
    private String transmision;
    private String combustible;
    private Integer pasajeros;
    private String descripcion;

    
    private boolean destacado = false;  
    private List<String> colores = new ArrayList<>(); 
    
    
    public Vehiculo() {
    }

    // Constructor con datos esenciales, el normal ja ja
    public Vehiculo(String marca, String modelo, int año, double precio, String categoria, String imagenUrl) {
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.precio = precio;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
    }

    // Constructor para vehiculos destacados
    public Vehiculo(String marca, String modelo, String descripcion, String imagenUrl) {
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.destacado = true; // Siempre destacado cuando se crea como un anuncio
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getTransmision() {
        return transmision;
    }

    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public Integer getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(Integer pasajeros) {
        this.pasajeros = pasajeros;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isDestacado() {
        return destacado;
    }

    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }

    public List<String> getColores() {
        return colores;
    }

    public void setColores(List<String> colores) {
        this.colores = colores;
    }
}
