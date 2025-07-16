package com.concesionario.controller;

import com.concesionario.model.Vehiculo;
import com.concesionario.model.Cita;
import com.concesionario.service.VehiculoService;
import com.concesionario.service.CitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class VehiculoController {
    private final VehiculoService vehiculoService;
    private final CitaService citaService;

    public VehiculoController(VehiculoService vehiculoService, CitaService citaService) {
        this.vehiculoService = vehiculoService;
        this.citaService = citaService;
    }

    @GetMapping("/vehiculos")
    public String mostrarVehiculos(Model model) {
        List<Vehiculo> vehiculos = vehiculoService.obtenerTodos();

        // Solución 1: Filtrar vehículos sin categoría
        Map<String, List<Vehiculo>> vehiculosPorCategoria = vehiculos.stream()
                .filter(v -> v.getCategoria() != null && !v.getCategoria().isEmpty())
                .collect(Collectors.groupingBy(Vehiculo::getCategoria));

        model.addAttribute("categorias", vehiculosPorCategoria.keySet());
        model.addAttribute("vehiculosPorCategoria", vehiculosPorCategoria);
        return "vehiculos";
    }

    @GetMapping("/fragments/chatbot")
    public String Inicio(Model model) {
//
        return "chatbot";
    }



    @GetMapping("/vehiculos/explorar/{id}")
    public String explorarVehiculo(@PathVariable String id, Model model) {
        Vehiculo vehiculo = vehiculoService.obtenerPorId(id);
        if (vehiculo == null) {
            return "redirect:/vehiculos";
        }
        model.addAttribute("vehiculo", vehiculo);
        return "explorar-vehiculo";
    }
    @GetMapping("/")
    public String redirectToInicio() {
        return "redirect:/usuario/Inicio";
    }

    @GetMapping("/nosotros")
    public String nosotros(){
        return "nosotros";
    }
    @GetMapping("/garantias")
    public String garantias(){
        return "garantias";
    }
    @GetMapping("/credito")
    public String credito(){
        return "credito";
    }

    @GetMapping("/cookies")
    public String cookies(){
        return "cookies";
    }
    @GetMapping("/terminos")
    public String terminos(){
        return "terminos";
    }
    @GetMapping("/ubicaciones")
    public String ubicaciones(){
        return "ubicaciones";
    }

}