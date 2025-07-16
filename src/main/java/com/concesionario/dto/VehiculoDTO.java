package com.concesionario.dto;

public class VehiculoDTO {
    private String id;
    private String marca;
    private String modelo;
    private double precio;
    private String categoria;

    // Constructor vacío
    public VehiculoDTO() {
    }

    // Constructor con todos los atributos
    public VehiculoDTO(String id, String marca, String modelo, double precio, String categoria) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Getters y Setters
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
}
