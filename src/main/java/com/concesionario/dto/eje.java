// package com.concesionario.dto;

// import java.security.Principal;

// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.concesionario.model.Cita;
// import com.concesionario.model.Usuario;
// import com.concesionario.model.Vehiculo;

// public class eje {

// @PostMapping("/cita/guardar")
// public String guardarCita(@ModelAttribute Cita cita, 
//                          Principal principal, 
//                          RedirectAttributes redirectAttributes) {
//     try {
//         Usuario usuario = usuarioRepository.findByCorreoUser(principal.getName())
//             .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
//         // Validar datos personales (sin cambios)
//         if (!validarDatosInmutables(cita, usuario)) {
//             redirectAttributes.addFlashAttribute("error", "No puedes modificar tus datos personales");
//             return "redirect:/usuario/cita";
//         }
        
//         // Solo validar vehículo si el tipo es "Interés en Vehículo"
//         if ("Interés en Vehículo".equals(cita.getTipo())) {
//             Vehiculo vehiculo = vehiculoService.obtenerPorId(cita.getVehiculoId());
//             if (vehiculo == null) {
//                 redirectAttributes.addFlashAttribute("error", "Vehículo no encontrado");
//                 return "redirect:/usuario/cita";
//             }
//             citaService.crearCitaConEmbedding(cita, usuario, vehiculo);
//         } else {
//             // Guardar cita sin vehículo
//             // cita.setVehiculoId(null); // Asegurar que sea null
//             // cita.setColorVehiculo(null);
//             // citaService.guardarCita(cita); // Método alternativo sin vehículo
//         }
        
//         return "redirect:/usuario/perfil?success=Cita+agendada+exitosamente";
//     } catch (Exception e) {
//         redirectAttributes.addFlashAttribute("error", "Error al agendar la cita: " + e.getMessage());
//         return "redirect:/usuario/cita";
//     }
// }
    
// }
