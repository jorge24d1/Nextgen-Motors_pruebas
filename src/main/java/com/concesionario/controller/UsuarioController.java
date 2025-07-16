package com.concesionario.controller;



import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.concesionario.model.Trabajador;
import com.concesionario.repository.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.concesionario.model.Cita;
import com.concesionario.model.Usuario;
import com.concesionario.model.Vehiculo;
import com.concesionario.repository.UsuarioRepository;
import com.concesionario.service.CitaService;
import com.concesionario.service.UsuarioService;
import com.concesionario.service.VehiculoService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private CitaService citaService;
    @Autowired
    TrabajadorRepository TrabajadorRepository;
    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;



    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        String email = principal.getName();

        // Ya lanza excepción internamente
        Usuario usuario = usuarioService.findByCorreoUser(email);
        List<Cita> citas = citaService.obtenerCitasPorUsuarioId(usuario.getId());

        // List<Cita> citas = citaService.findByUsuarioId(usuario.getId());
        model.addAttribute("nombreUsuario", usuario.getNombreUser());
        model.addAttribute("citas", citas);
        return "usuario/perfil";
    }

    @GetMapping("/agendamiento")
    public String Inicioff() {
        return "agendamiento";
    }


    @GetMapping("/Inicio")
    public String Inicio(Model model) {
        List<Vehiculo> vehiculos = vehiculoService.obtenerTodos();

        // Agrupar por categoría con manejo de nulos
        Map<String, List<Vehiculo>> vehiculosPorCategoria = vehiculos.stream()
                .filter(v -> v.getCategoria() != null) // Filtra vehículos sin categoría
                .collect(Collectors.groupingBy(Vehiculo::getCategoria));

        // Obtener destacados con verificación
        List<Vehiculo> vehiculosDestacados = Optional.ofNullable(vehiculoService.obtenerDestacados())
                .orElse(Collections.emptyList());

        // Añadir atributos al modelo
        model.addAttribute("categorias", vehiculosPorCategoria.keySet());
        model.addAttribute("vehiculosPorCategoria", vehiculosPorCategoria);
        model.addAttribute("destacados", vehiculosDestacados);

        return "index";
    }


    @GetMapping("/cita")
    public String mostrarFormularioCita(
            @RequestParam(required = false) String vehiculoId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String color,
            Principal principal,
            Model model) {

        // Obtener usuario autenticado
        Usuario usuario = usuarioRepository.findByCorreoUser(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear nueva cita con datos del usuario
        Cita cita = new Cita();
        cita.setNombres(usuario.getNombreUser());
        cita.setApellidos(usuario.getApellidoUser());
        cita.setCedula(usuario.getIdentificacionUser());
        cita.setCorreoElectronico(usuario.getCorreoUser());
        // cita.setTelefono(usuario.getTelefonoUser()); // Si tienes teléfono en usuario

        List<Vehiculo> vehiculos = vehiculoService.obtenerTodos();

        // Configuración basada en parámetros
        if (vehiculoId != null) {
            Vehiculo vehiculo = vehiculoService.obtenerPorId(vehiculoId);
            if (vehiculo != null) {
                // cita.setTipo("Interés en Vehículo"); //esto hay que quitarlo:
                cita.setVehiculoId(vehiculo.getId());
                cita.setNombreVehiculo(vehiculo.getMarca() + " " + vehiculo.getModelo());
                cita.setColorVehiculo(color);
                model.addAttribute("coloresVehiculo",
                        vehiculo.getColores() != null ? vehiculo.getColores() : Collections.emptyList());
            }
        } else if (tipo != null) {
            cita.setTipo(tipo);
        } else {
            cita.setTipo("Otros");
        }
        List<Trabajador> trabajadores = TrabajadorRepository.findAll();
        model.addAttribute("trabajadores", trabajadores);
        model.addAttribute("cita", cita);
        model.addAttribute("vehiculos", vehiculos);
        return "cita";
    }



    @PostMapping("/cita/guardar")
    public String guardarCita(@ModelAttribute Cita cita,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        try {
            // 1. Validar que el trabajador exista
            Trabajador trabajador = TrabajadorRepository.findById(cita.getTrabajadorId())
                    .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

            // 2. Validar que la fecha sea un día laboral del trabajador
            if (!esDiaLaboral(trabajador, cita.getFechaCita())) {
                redirectAttributes.addFlashAttribute("error", "El trabajador no labora ese día");
                return "redirect:/usuario/cita";
            }

            // 3. Validar disponibilidad de hora
            if (!citaService.isHoraDisponible(cita.getTrabajadorId(), cita.getFechaCita(), cita.getHoraCita())) {
                redirectAttributes.addFlashAttribute("error", "La hora seleccionada ya está ocupada para este trabajador");
                return "redirect:/usuario/cita";
            }

            // Resto de la lógica de guardado...
            Usuario usuario = usuarioRepository.findByCorreoUser(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!validarDatosInmutables(cita, usuario)) {
                redirectAttributes.addFlashAttribute("error", "No puedes modificar tus datos personales");
                return "redirect:/usuario/cita";
            }

            Vehiculo vehiculo = null;
            if (cita.getVehiculoId() != null) {
                vehiculo = vehiculoService.obtenerPorId(cita.getVehiculoId());
                if (vehiculo == null) {
                    redirectAttributes.addFlashAttribute("error", "Vehículo no encontrado");
                    return "redirect:/usuario/cita";
                }
            }

            if("Otros".equals(cita.getTipo())) {
                citaService.guardarCitaSimple(cita, usuario, vehiculo);
            } else {
                citaService.crearCitaConEmbedding(cita, usuario, vehiculo);
            }

            return "redirect:/usuario/perfil?success=Cita+agendada+exitosamente";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agendar la cita: " + e.getMessage());
            return "redirect:/usuario/cita";
        }
    }

    private boolean esDiaLaboral(Trabajador trabajador, String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            DayOfWeek diaSemana = fecha.getDayOfWeek();

            // Convertir el día de la semana a formato español (LUNES, MARTES, etc.)
            String diaEnEspanol = convertirDiaEspanol(diaSemana);

            // Verificar si el día está en los días laborales del trabajador
            return trabajador.getDiasTrabajo().contains(diaEnEspanol.toUpperCase());
        } catch (Exception e) {
            return false;
        }
    }

    private String convertirDiaEspanol(DayOfWeek dia) {
        switch (dia) {
            case MONDAY: return "LUNES";
            case TUESDAY: return "MARTES";
            case WEDNESDAY: return "MIERCOLES";
            case THURSDAY: return "JUEVES";
            case FRIDAY: return "VIERNES";
            case SATURDAY: return "SABADO";
            case SUNDAY: return "DOMINGO";
            default: return "";
        }
    }


    private boolean validarDatosInmutables(Cita cita, Usuario usuario) {
        return usuario.getNombreUser().equals(cita.getNombres()) &&
               usuario.getApellidoUser().equals(cita.getApellidos()) &&
               usuario.getIdentificacionUser().equals(cita.getCedula()) &&
               usuario.getCorreoUser().equals(cita.getCorreoElectronico());
    }

    @GetMapping("/mis-citas")
    public String verMisCitas(Model model, Principal principal) {

        Usuario usuario = usuarioRepository.findByCorreoUser(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        List<Cita> citas = citaService.obtenerCitasPorUsuarioId(usuario.getId());
        model.addAttribute("citas", citas);
        model.addAttribute("nombreUsuario", usuario.getNombreUser() + " " + usuario.getApellidoUser());
        model.addAttribute("citas", citas);
        return "Usuario/MisCitas";
    }
    @GetMapping("/check-disponibilidad")
    public ResponseEntity<?> checkDisponibilidad(
            @RequestParam String trabajadorId,
            @RequestParam String fecha,
            @RequestParam String hora) {

        boolean disponible = citaService.isHoraDisponible(trabajadorId, fecha, hora);

        HashMap<String, Object> response = new HashMap<>();
        response.put("disponible", disponible);

        if (!disponible) {
            response.put("mensaje", "La hora ya está ocupada para este trabajador");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene las horas ocupadas de un trabajador en una fecha específica
     * URL: /usuario/horas-ocupadas?trabajadorId=XXX&fecha=YYYY-MM-DD
     */
    @GetMapping("/horas-ocupadas")
    public ResponseEntity<?> getHorasOcupadas(
            @RequestParam String trabajadorId,
            @RequestParam String fecha) {

        List<String> horasOcupadas = citaService.getHorasOcupadas(trabajadorId, fecha);
        return ResponseEntity.ok(horasOcupadas);
    }
    @GetMapping("/check-dia-trabajador")
    public ResponseEntity<?> verificarDiaTrabajador(
            @RequestParam String trabajadorId,
            @RequestParam String fecha) {

        Map<String, Object> response = new HashMap<>();

        try {
            Trabajador trabajador = TrabajadorRepository.findById(trabajadorId)
                    .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

            LocalDate fechaCita = LocalDate.parse(fecha);
            DayOfWeek diaSemana = fechaCita.getDayOfWeek();
            String diaEnEspanol = convertirDiaEspanol(diaSemana);

            boolean diaValido = trabajador.getDiasTrabajo().contains(diaEnEspanol);

            response.put("valido", diaValido);
            if (!diaValido) {
                response.put("mensaje", "El trabajador no labora los " + diaEnEspanol);
                response.put("diasLaborales", trabajador.getDiasTrabajo());
            }

        } catch (Exception e) {
            response.put("error", "Error al validar fecha: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }


}
