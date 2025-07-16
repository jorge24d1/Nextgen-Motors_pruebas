package com.concesionario.controller;
import com.concesionario.service.GeneradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class GeneradorController {
    @Autowired
    private GeneradorService generadorService;

    @PostMapping("/generar")
    public ResponseEntity<String> generar() {
        generadorService.generarDatos();
        return ResponseEntity.ok("Datos generados correctamente");
    }
}
