package com.concesionario.service;

import com.concesionario.model.Vehiculo;
import com.concesionario.repository.VehiculoRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VehiculoService {
    private static final Logger log = LoggerFactory.getLogger(VehiculoService.class);

    private final VehiculoRepository vehiculoRepository;
    private final Cloudinary cloudinary;

    // ELIMINAR esta línea ya que no usaremos uploadDir local
    // @Value("${upload.dir}")
    // private String uploadDir;

    public VehiculoService(VehiculoRepository vehiculoRepository, Cloudinary cloudinary) {
        this.vehiculoRepository = vehiculoRepository;
        this.cloudinary = cloudinary;
    }

    
    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepository.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    
    public List<Vehiculo> obtenerVehiculosNormales() {
        return vehiculoRepository.findByDestacadoFalse();
    }

    public void crearVehiculoNormal(Vehiculo vehiculo, MultipartFile imagen) throws IOException {
        String rutaImagen = guardarImagenEnCloudinary(imagen);
        vehiculo.setImagenUrl(rutaImagen);
        vehiculo.setDestacado(false); 
        vehiculoRepository.save(vehiculo);
    }

    
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
        String rutaImagen = guardarImagenEnCloudinary(imagen);
        vehiculo.setImagenUrl(rutaImagen);
        vehiculo.setDestacado(true); 
        vehiculoRepository.save(vehiculo);
    }

    public void crearAnuncioCompleto(Vehiculo vehiculo, MultipartFile imagen) throws IOException {
        String rutaImagen = guardarImagenEnCloudinary(imagen);
        vehiculo.setImagenUrl(rutaImagen);
        vehiculo.setDestacado(true); 
        vehiculoRepository.save(vehiculo);
    }

    
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

    
    private String guardarImagenEnCloudinary(MultipartFile imagen) throws IOException {
        try {
            
            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put("folder", "auto_plus/vehiculos"); 

            
            Map<String, Object> uploadResult = cloudinary.uploader()
                    .upload(imagen.getBytes(), uploadOptions); 

            String imageUrl = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();

            System.out.println("✅ Imagen subida EXITOSAMENTE");
            System.out.println("📁 URL: " + imageUrl);
            System.out.println("📂 Public ID: " + publicId);
            System.out.println("📊 Respuesta completa: " + uploadResult);

            return imageUrl;

        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
            throw new IOException("Error al subir la imagen: " + e.getMessage(), e);
        }
    }
}
