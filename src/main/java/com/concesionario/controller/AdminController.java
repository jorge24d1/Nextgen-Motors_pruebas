package com.concesionario.controller;

import com.concesionario.model.Rol;
import com.concesionario.model.Trabajador;
import com.concesionario.model.Vehiculo;
import com.concesionario.service.*;
import com.concesionario.repository.CitaRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import com.concesionario.dto.UsuarioDTO;
import com.concesionario.model.Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.concesionario.repository.TrabajadorRepository;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public AdminController(
            VehiculoService vehiculoService,
            CitaService citaService,
            NotificacionService notificacionService,
            PasswordEncoder passwordEncoder) {

        this.vehiculoService = vehiculoService;
        this.citaService = citaService;
        this.notificacionService = notificacionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private CitaService citaService;
    @Autowired
    private NotificacionService notificacionService;

    // ❌ ELIMINADO: Ya no necesitamos uploadDir local
    // @Value("${upload.dir}")
    // private String uploadDir;

    @Autowired
    private TrabajadorRepository TrabajadorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/Dashboard")
    public String dashboard(Model model) {
        // Estadísticas
        long totalCitas = citaService.contarTodasLasCitas();
        long totalUsuarios = usuarioService.contarUsuarios();
        long totalVehiculos = vehiculoService.contarTodosVehiculos();

        // Listados
        List<Vehiculo> vehiculos = vehiculoService.obtenerVehiculosNormales();
        List<Vehiculo> anuncios = vehiculoService.obtenerDestacados();
        List<Cita> citasPendientes = citaService.obtenerCitasPendientes();

        // Agregar atributos al modelo
        model.addAttribute("totalCitas", totalCitas);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalVehiculos", totalVehiculos);
        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("anuncios", anuncios);
        model.addAttribute("citas", citasPendientes);
        model.addAttribute("numeroNotificaciones", notificacionService.contarCitasNoLeidas());
        model.addAttribute("citasNoLeidas", notificacionService.obtenerCitasNoLeidas());

        return "Admin/Dashboard";
    }

    @PostMapping("/RegistroT")
    public String registroTrabajador(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("correo") String correo,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("password") String password,
            @RequestParam("horaInicio") @DateTimeFormat(pattern = "HH:mm") LocalTime horaInicio,
            @RequestParam("horaFin") @DateTimeFormat(pattern = "HH:mm") LocalTime horaFin,
            @RequestParam(value = "diasTrabajo", required = false) List<String> diasTrabajo,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validación básica
        if (diasTrabajo == null || diasTrabajo.isEmpty()) {
            model.addAttribute("error", "Debe seleccionar al menos un día de trabajo");
            return manejarErrorRegistro(model, nombre, apellido, correo, identificacion, horaInicio, horaFin);
        }

        try {
            Trabajador trabajador = new Trabajador();
            trabajador.setNombre(nombre);
            trabajador.setApellido(apellido);
            trabajador.setCorreo(correo);
            trabajador.setIdentificacion(identificacion);
            trabajador.setPassword(passwordEncoder.encode(password));
            trabajador.setHoraInicioTrabajo(horaInicio);
            trabajador.setHoraFinTrabajo(horaFin);
            trabajador.setDiasTrabajo(diasTrabajo);

            TrabajadorRepository.save(trabajador);

            redirectAttributes.addFlashAttribute("success", "Trabajador registrado exitosamente");
            return "redirect:/admin/Dashboard";

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Error: El correo o identificación ya existen");
            return manejarErrorRegistro(model, nombre, apellido, correo, identificacion, horaInicio, horaFin);
        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado: " + e.getMessage());
            return manejarErrorRegistro(model, nombre, apellido, correo, identificacion, horaInicio, horaFin);
        }
    }

    private String manejarErrorRegistro(Model model, String nombre, String apellido,
                                        String correo, String identificacion,
                                        LocalTime horaInicio, LocalTime horaFin) {
        model.addAttribute("nombre", nombre);
        model.addAttribute("apellido", apellido);
        model.addAttribute("correo", correo);
        model.addAttribute("identificacion", identificacion);
        model.addAttribute("horaInicio", horaInicio != null ? horaInicio.toString() : "");
        model.addAttribute("horaFin", horaFin != null ? horaFin.toString() : "");
        return "Admin/Dashboard";
    }

    @GetMapping("/notificaciones")
    @ResponseBody
    public List<Map<String, Object>> obtenerNotificaciones() {
        List<Cita> citas = notificacionService.obtenerCitasNoLeidas();

        return citas.stream().map(cita -> {
            Map<String, Object> citaMap = new HashMap<>();
            citaMap.put("id", cita.getId());

            UsuarioDTO usuario = cita.getUsuario();
            citaMap.put("nombres", usuario != null ? usuario.getNombre() : "");
            citaMap.put("apellidos", usuario != null ? usuario.getApellido() : "");

            citaMap.put("tipo", cita.getTipo());
            citaMap.put("fechaCreacion", cita.getFechaCreacion().toString());
            citaMap.put("comentario", cita.getComentario());
            return citaMap;
        }).collect(Collectors.toList());
    }

    @PostMapping("/marcar-leida/{id}")
    @ResponseBody
    public String marcarComoLeida(@PathVariable String id) {
        notificacionService.marcarComoLeida(id);
        return "OK";
    }

    @PostMapping("/marcar-todas-leidas")
    @ResponseBody
    public String marcarTodasComoLeidas() {
        notificacionService.marcarTodasComoLeidas();
        return "OK";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "Admin/Nuevo";
    }

    @GetMapping("/obtener-vehiculo/{id}")
    @ResponseBody
    public Vehiculo obtenerVehiculoParaEdicion(@PathVariable String id) {
        Vehiculo vehiculo = vehiculoService.obtenerPorId(id);
        if (vehiculo.getColores() == null) {
            vehiculo.setColores(new ArrayList<>());
        }
        return vehiculo;
    }

    @PostMapping("/editar/{id}")
    public String editarVehiculo(
            @PathVariable String id,
            @ModelAttribute Vehiculo vehiculo,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam("motor") String motor,
            @RequestParam("transmision") String transmision,
            @RequestParam("combustible") String combustible,
            @RequestParam("pasajeros") Integer pasajeros,
            @RequestParam String colores,
            @RequestParam("descripcion") String descripcion,
            Model model) {

        try {
            Vehiculo vehiculoExistente = vehiculoService.obtenerPorId(id);

            if (vehiculoExistente.getColores() == null) {
                vehiculoExistente.setColores(new ArrayList<>());
            }
            if (colores != null && !colores.isEmpty()) {
                List<String> listaColores = Arrays.stream(colores.split(","))
                        .map(String::trim)
                        .filter(color -> !color.isEmpty())
                        .collect(Collectors.toList());
                vehiculoExistente.setColores(listaColores);
            } else {
                vehiculoExistente.setColores(new ArrayList<>());
            }

            // Campos básicos
            vehiculoExistente.setMarca(vehiculo.getMarca());
            vehiculoExistente.setModelo(vehiculo.getModelo());
            vehiculoExistente.setAño(vehiculo.getAño());
            vehiculoExistente.setPrecio(vehiculo.getPrecio());
            vehiculoExistente.setCategoria(vehiculo.getCategoria());
            vehiculoExistente.setMotor(motor);
            vehiculoExistente.setTransmision(transmision);
            vehiculoExistente.setCombustible(combustible);
            vehiculoExistente.setPasajeros(pasajeros);
            vehiculoExistente.setDescripcion(descripcion);

            // Procesar colores
            if (colores != null && !colores.isEmpty()) {
                List<String> listaColores = Arrays.stream(colores.split(","))
                        .map(String::trim)
                        .filter(color -> !color.isEmpty())
                        .collect(Collectors.toList());
                vehiculoExistente.setColores(listaColores);
            } else {
                vehiculoExistente.setColores(new ArrayList<>());
            }

            // ✅ MODIFICADO: Ya no necesitamos manejar la imagen aquí porque
            // el VehiculoService ahora usa Cloudinary automáticamente
            // Solo llamamos al servicio para guardar
            vehiculoService.guardarVehiculo(vehiculoExistente);
            return "redirect:/admin/Dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Error al editar el vehículo: " + e.getMessage());
            return "redirect:/admin/Dashboard";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable String id) {
        // ✅ MODIFICADO: Ya no necesitamos eliminar archivos locales
        // Cloudinary maneja el almacenamiento automáticamente
        vehiculoService.eliminarVehiculo(id);
        return "redirect:/admin/Dashboard";
    }

    // ❌ ELIMINADO: Ya no necesitamos el método guardarImagen local
    // private String guardarImagen(MultipartFile imagen) throws IOException {
    //     // Este método ha sido eliminado porque ahora usamos Cloudinary
    // }

    @GetMapping("/citas")
    public String listarCitas(Model model) {
        List<Cita> citas = citaService.obtenerTodasLasCitas();
        model.addAttribute("citas", citas);
        model.addAttribute("numeroNotificaciones", notificacionService.contarCitasNoLeidas());
        return "Admin/CitasLista";
    }

    @ResponseBody
    @PostMapping("/citas/{id}/cambiar-estado")
    public String cambiarEstadoCita(@PathVariable String id, @RequestParam String estado) {
        Cita cita = citaService.obtenerCitaPorId(id);
        if (cita == null) {
            return "Cita no encontrada";
        }

        cita.setEstado(estado);
        cita.setLeida(true);
        citaService.guardarCita(cita);

        return "OK";
    }

    @ResponseBody
    @PostMapping("/citas/{id}/asignar-fecha")
    public String asignarFechaCita(@PathVariable String id, @RequestParam String fecha) {
        Cita cita = citaService.obtenerCitaPorId(id);
        if (cita == null) {
            return "Cita no encontrada";
        }

        LocalDateTime fechaHora = LocalDateTime.parse(fecha.replace(" ", "T"));
        cita.setFechaAsignada(fechaHora);
        citaService.guardarCita(cita);

        return "OK";
    }

    @ResponseBody
    @GetMapping("/citas/{id}/notas")
    public String obtenerNotasCita(@PathVariable String id) {
        Cita cita = citaService.obtenerCitaPorId(id);
        return (cita != null && cita.getNotasAdmin() != null) ? cita.getNotasAdmin() : "";
    }

    @ResponseBody
    @PostMapping("/citas/{id}/guardar-notas")
    public String guardarNotasCita(@PathVariable String id, @RequestParam String notas) {
        Cita cita = citaService.obtenerCitaPorId(id);
        if (cita == null) {
            return "Cita no encontrada";
        }

        cita.setNotasAdmin(notas);
        citaService.guardarCita(cita);

        return "OK";
    }

    @ResponseBody
    @PostMapping("/citas/{id}/cancelar-fecha")
    public String cancelarFechaCita(@PathVariable String id) {
        Cita cita = citaService.obtenerCitaPorId(id);
        if (cita == null) {
            return "Cita no encontrada";
        }

        cita.setFechaAsignada(null);
        citaService.guardarCita(cita);

        return "OK";
    }

    @ResponseBody
    @PostMapping("/citas/{id}/volver-pendiente")
    public String volverAPendiente(@PathVariable String id) {
        Cita cita = citaService.obtenerCitaPorId(id);
        if (cita == null) {
            return "Cita no encontrada";
        }

        cita.setEstado("Pendiente");
        cita.setFechaAsignada(null);
        citaService.guardarCita(cita);

        return "OK";
    }

    @ResponseBody
    @GetMapping("/citas/{id}/datos")
    public Map<String, Object> obtenerDatosCita(@PathVariable String id) {
        Cita cita = citaService.obtenerCitaPorId(id);
        if (cita == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada");
        }

        Map<String, Object> datos = new HashMap<>();
        datos.put("estado", cita.getEstado());
        datos.put("fechaAsignada", cita.getFechaAsignada());
        datos.put("notasAdmin", cita.getNotasAdmin());

        return datos;
    }

    @GetMapping("/anuncios")
    public String listarAnuncios(Model model) {
        model.addAttribute("anuncios", vehiculoService.obtenerDestacados());
        return "Admin/Dashboard";
    }

    @PostMapping("/guardar-anuncio")
    public String guardarAnuncio(
            @RequestParam String marca,
            @RequestParam String modelo,
            @RequestParam int año,
            @RequestParam double precio,
            @RequestParam String categoria,
            @RequestParam String motor,
            @RequestParam String transmision,
            @RequestParam String combustible,
            @RequestParam int pasajeros,
            @RequestParam String descripcion,
            @RequestParam MultipartFile imagen,
            Model model) throws IOException {

        try {
            Vehiculo anuncio = new Vehiculo();
            anuncio.setMarca(marca);
            anuncio.setModelo(modelo);
            anuncio.setAño(año);
            anuncio.setPrecio(precio);
            anuncio.setCategoria(categoria);
            anuncio.setMotor(motor);
            anuncio.setTransmision(transmision);
            anuncio.setCombustible(combustible);
            anuncio.setPasajeros(pasajeros);
            anuncio.setDescripcion(descripcion);
            anuncio.setDestacado(true); // Es un anuncio

            vehiculoService.crearAnuncio(anuncio, imagen);
            return "redirect:/admin/Dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el anuncio: " + e.getMessage());
            return "redirect:/admin/Dashboard?error=Error+al+crear+anuncio";
        }
    }

    @PostMapping("/anuncios/eliminar/{id}")
    public String eliminarAnuncio(@PathVariable String id) {
        vehiculoService.eliminarVehiculo(id);
        return "redirect:/admin/anuncios";
    }

    @PostMapping("/guardar-vehiculo")
    public String guardarVehiculoNormal(
            @RequestParam String marca,
            @RequestParam String modelo,
            @RequestParam int año,
            @RequestParam double precio,
            @RequestParam String categoria,
            @RequestParam String motor,
            @RequestParam String transmision,
            @RequestParam String combustible,
            @RequestParam int pasajeros,
            @RequestParam String descripcion,
            @RequestParam String colores,
            @RequestParam MultipartFile imagen) throws IOException {

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAño(año);
        vehiculo.setPrecio(precio);
        vehiculo.setCategoria(categoria);
        vehiculo.setMotor(motor);
        vehiculo.setTransmision(transmision);
        vehiculo.setCombustible(combustible);
        vehiculo.setPasajeros(pasajeros);
        vehiculo.setDescripcion(descripcion);
        vehiculo.setDestacado(false);

        // Procesar los colores
        if (colores != null && !colores.isEmpty()) {
            List<String> listaColores = Arrays.stream(colores.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            vehiculo.setColores(listaColores);
        }

        vehiculoService.crearVehiculoNormal(vehiculo, imagen);
        return "redirect:/admin/Dashboard";
    }
}
