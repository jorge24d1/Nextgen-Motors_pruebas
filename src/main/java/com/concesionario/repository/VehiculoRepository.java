package com.concesionario.repository;

import com.concesionario.model.Vehiculo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface VehiculoRepository extends MongoRepository<Vehiculo, String> {
    List<Vehiculo> findByCategoria(String categoria);
    List<Vehiculo> findByDestacadoTrue();
    List<Vehiculo> findByDestacadoFalse();
    long countByDestacadoFalse();
    long countByDestacadoTrue();
}