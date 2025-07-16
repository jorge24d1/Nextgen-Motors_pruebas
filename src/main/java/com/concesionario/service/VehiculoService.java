package com.concesionario.service;

import com.concesionario.model.Vehiculo;
import com.concesionario.repository.VehiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VehiculoService {
    private static final Logger log = LoggerFactory.getLogger(VehiculoService.class);

    private final VehiculoRepository vehiculoRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    // Métodos para todos los vehículos
    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepository.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Métodos para vehículos normales (no destacados)
    public List<Vehiculo> obtenerVehiculosNormales() {
        return vehiculoRepository.findByDestacadoFalse();
    }

    public void crearVehiculoNormal(Vehiculo vehiculo, MultipartFile imagen) throws IOException {
        String rutaImagen = guardarImagen(imagen);
        vehiculo.setImagenUrl(rutaImagen);
        vehiculo.setDestacado(false); // Asegura que no sea destacado
        vehiculoRepository.save(vehiculo);
    }

    // Métodos para anuncios destacados
    public List<Vehiculo> obtenerDestacados() {
        try {
            List<Vehiculo> destacados = vehiculoRepository.findByDestacadoTrue();
            return destacados != null ? destacados : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error al obtener vehiculos destacados", e);
            return Collections.emptyList();
        }
    }

    public void crearAnuncio(Vehiculo vehiculo, MultipartFile imagen) throws IOException {
        String rutaImagen = guardarImagen(imagen);
        vehiculo.setImagenUrl(rutaImagen);
        vehiculo.setDestacado(true); // Asegura que sea destacado
        vehiculoRepository.save(vehiculo);
    }

    public void crearAnuncioCompleto(Vehiculo vehiculo, MultipartFile imagen) throws IOException {
        String rutaImagen = guardarImagen(imagen);
        vehiculo.setImagenUrl(rutaImagen);
        vehiculo.setDestacado(true); // Asegura que sea destacado
        vehiculoRepository.save(vehiculo);
    }

    // Métodos comunes
    public Vehiculo obtenerPorId(String id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    public void guardarVehiculo(Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo);
    }

    public void eliminarVehiculo(String id) {
        vehiculoRepository.deleteById(id);
    }

    public List<Vehiculo> obtenerPorCategoria(String categoria) {
        return vehiculoRepository.findByCategoria(categoria);
    }

    public long contarTodosVehiculos() {
        return vehiculoRepository.count();
    }

    public long contarVehiculosNormales() {
        return vehiculoRepository.countByDestacadoFalse();
    }

    public long contarAnuncios() {
        return vehiculoRepository.countByDestacadoTrue();
    }



    // Método privado para manejo de imágenes
    private String guardarImagen(MultipartFile imagen) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String nombreArchivo = System.currentTimeMillis() + "_" +
                Objects.requireNonNull(imagen.getOriginalFilename());
        Path rutaCompleta = uploadPath.resolve(nombreArchivo);
        Files.copy(imagen.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + nombreArchivo;
    }

    // Manejo de estado
    public void cambiarEstadoDestacado(String id, boolean destacado) {
        vehiculoRepository.findById(id).ifPresent(vehiculo -> {
            vehiculo.setDestacado(destacado);
            vehiculoRepository.save(vehiculo);
        });
    }
}