package com.concesionario.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concesionario.dto.UsuarioDTO;
import com.concesionario.dto.VehiculoDTO;
import com.concesionario.model.Cita;
import com.concesionario.model.Usuario;
import com.concesionario.model.Vehiculo;
import com.concesionario.repository.CitaRepository;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public Cita guardarCita(Cita cita) {
        return citaRepository.save(cita);
    }

    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAllByOrderByIdDesc();
    }

    public List<Cita> obtenerCitasPorTipo(String tipo) {
        return citaRepository.findByTipo(tipo);
    }


    public List<Cita> obtenerCitasPendientes() {
        return citaRepository.findByAtendidaFalseOrderByIdDesc();
    }


    public List<Cita> obtenerCitasPorUsuarioId(String usuarioId) {
        return citaRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    public Cita obtenerCitaPorId(String id) {
        return citaRepository.findById(id).orElse(null);
    }

    // public Page<Cita> obtenerCitasPaginadas(int page, int size){
    //     Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
    //     return citaRepository.findAllByOrderByFechaCreacionDesc(pageable);
    // }

    public void guardarCitaSimple(Cita cita, Usuario usuario, Vehiculo vehiculo) {
        cita.setCedula(usuario.getIdentificacionUser());
        cita.setCorreoElectronico(usuario.getCorreoUser());


        // Crear DTOs para embedding
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombreUser());
        usuarioDTO.setApellido(usuario.getApellidoUser());
        usuarioDTO.setCorreo(usuario.getCorreoUser());
        usuarioDTO.setIdentificacion(usuario.getIdentificacionUser());

        VehiculoDTO vehiculoDTO = new VehiculoDTO();
        vehiculoDTO.setId(vehiculo.getId());
        vehiculoDTO.setMarca(vehiculo.getMarca());
        vehiculoDTO.setModelo(vehiculo.getModelo());
        vehiculoDTO.setPrecio(vehiculo.getPrecio());
        vehiculoDTO.setCategoria(vehiculo.getCategoria());
        // vehiculoDTO.setColores(vehiculo.getColores());

        // Establece relaciones embebidas
        cita.setUsuario(usuarioDTO);
        cita.setVehiculo(vehiculoDTO);
        cita.setFechaCreacion(LocalDateTime.now());
        cita.setAtendida(false);
        cita.setLeida(false);
        cita.setEstado("Pendiente");
        
        
        cita.setNombres(null);
        cita.setApellidos(null);
        cita.setCedula(null);
        cita.setCorreoElectronico(null);
        // cita.setComentario(null);
        // cita.setTelefono(null);
        cita.setModelo(null);
        cita.setVehiculoId(null);
        cita.setNombreVehiculo(null);

        citaRepository.save(cita);
    }

    public void crearCitaConEmbedding(Cita cita, Usuario usuario, Vehiculo vehiculo) {

        cita.setCedula(usuario.getIdentificacionUser());
        cita.setCorreoElectronico(usuario.getCorreoUser());


        // Crear DTOs para embedding
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombreUser());
        usuarioDTO.setApellido(usuario.getApellidoUser());
        usuarioDTO.setCorreo(usuario.getCorreoUser());
        usuarioDTO.setIdentificacion(usuario.getIdentificacionUser());

        VehiculoDTO vehiculoDTO = new VehiculoDTO();
        vehiculoDTO.setId(vehiculo.getId());
        vehiculoDTO.setMarca(vehiculo.getMarca());
        vehiculoDTO.setModelo(vehiculo.getModelo());
        vehiculoDTO.setPrecio(vehiculo.getPrecio());
        vehiculoDTO.setCategoria(vehiculo.getCategoria());
        // vehiculoDTO.setColores(vehiculo.getColores());

        // Establece relaciones embebidas
        cita.setUsuario(usuarioDTO);
        cita.setVehiculo(vehiculoDTO);
        cita.setFechaCreacion(LocalDateTime.now());
        cita.setAtendida(false);
        cita.setLeida(false);
        cita.setEstado("Pendiente");
        
        
        cita.setNombres(null);
        cita.setApellidos(null);
        cita.setCedula(null);
        cita.setCorreoElectronico(null);
        cita.setComentario(null);
        // cita.setTelefono(null);
        cita.setModelo(null);
        cita.setVehiculoId(null);
        cita.setNombreVehiculo(null);

        citaRepository.save(cita);
    }


    public long contarTodasLasCitas() {
        return citaRepository.count();
    }
    public boolean isHoraDisponible(String trabajadorId, String fechaCita, String horaCita) {
        return citaRepository.findByTrabajadorIdAndFechaCitaAndHoraCita(trabajadorId, fechaCita, horaCita).isEmpty();
    }

    public List<String> getHorasOcupadas(String trabajadorId, String fechaCita) {
        List<Cita> citas = citaRepository.findByTrabajadorIdAndFechaCita(trabajadorId, fechaCita);
        return citas.stream().map(Cita::getHoraCita).collect(Collectors.toList());
    }
    

}
