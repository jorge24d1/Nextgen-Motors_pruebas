package com.concesionario.repository;

import com.concesionario.model.Cita;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// import org.springframework.data.domain.Page; findAllByOrderByIdDesc()
// import org.springframework.data.domain.Pageable;

public interface CitaRepository extends MongoRepository<Cita, String> {
    List<Cita> findByTipo(String tipo);
    List<Cita> findAllByOrderByIdDesc();
    List<Cita> findByLeidaFalseOrderByFechaCreacionDesc();
    long countByLeidaFalse();
    List<Cita> findByAtendidaFalseOrderByIdDesc();
    List<Cita> findByLeidaFalse();
    List<Cita> findByUsuarioIdOrderByFechaCreacionDesc(String usuarioId);
    // Page<Cita> findAllByOrderByFechaCreacionDesc(Pageable pageable);
    List<Cita> findByTrabajadorIdAndFechaCita(String trabajadorId, String fechaCita);
    List<Cita> findByTrabajadorIdAndFechaCitaAndHoraCita(String trabajadorId, String fechaCita, String horaCita);
}

