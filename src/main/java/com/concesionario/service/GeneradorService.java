package com.concesionario.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.concesionario.dto.UsuarioDTO;
import com.concesionario.dto.VehiculoDTO;
import com.concesionario.model.Cita;
import com.concesionario.model.Usuario;
import com.concesionario.model.Vehiculo;
import com.github.javafaker.Faker;
import java.util.*;

@Service
public class GeneradorService {
    @Autowired
    private MongoTemplate mongoTemplate;



    private final Faker faker = new Faker(new Locale("es"));
    private final Random random = new Random();
    // final int BATCH_SIZE = 1000;
    private static final int BATCH_SIZE = 1000;

    public GeneradorService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public void generarDatos(){
        // // Borrar datos existentes (opcional)
        // mongoTemplate.remove(new Query(), Usuario.class);
        // mongoTemplate.remove(new Query(), Vehiculo.class);
        // mongoTemplate.remove(new Query(), Cita.class);
        List<Usuario> usuarios = generarUsuarios(5000);
        List<Vehiculo> vehiculos = generarVehiculos(3000);
        generarCitas(15000, usuarios, vehiculos);

        // mongoTemplate.insertAll(usuarios);
        // mongoTemplate.insertAll(vehiculos);
        // mongoTemplate.insertAll(citas);
    }
    private List<Usuario> generarUsuarios(int cantidad){
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < cantidad; i++){
            Usuario u = new Usuario();
            u.setNombreUser(faker.name().firstName());
            u.setApellidoUser(faker.name().lastName());
            u.setIdentificacionUser(faker.idNumber().valid());
            u.setCorreoUser(faker.internet().emailAddress());
            u.setPasswordUser(faker.internet().password());
            u.setFechaCreacion(LocalDateTime.now().minusDays(random.nextInt(365)));
            usuarios.add(u);
        }

        for (int i = 0; i < usuarios.size(); i += BATCH_SIZE){    
            mongoTemplate.insertAll(new ArrayList<>(usuarios.subList(i, Math.min(i + BATCH_SIZE, usuarios.size()))));
        }
        return mongoTemplate.findAll(Usuario.class);
        
    }

    private List<Vehiculo> generarVehiculos(int cantidad){
        List<Vehiculo> vehiculos = new ArrayList<>();
        String[] marcas = {"Toyota", "Honda", "Ford", "Chevrolet", "Nissan", "Volkswagen", "Hyundai", "Mazda"};
        String[] modelos = {"Corrolla", "Civic", "Ranger", "Trancker", "Altima", "Jetta", "Elantra", "CX-5"};
        String[]categorias = {"Pick-Ups", "Camionetas", "Autómoviles", "Híbrido", "Deportivo", "Eléctrico"};

        for (int i = 0; i < cantidad; i++) {
            Vehiculo v = new Vehiculo();
            v.setMarca(marcas[random.nextInt(marcas.length)]);
            v.setModelo(modelos[random.nextInt(modelos.length)]);
            v.setAño(faker.number().numberBetween(2015, 2024));
            v.setPrecio(faker.number().randomDouble(2, 80000000, 150000000));
            v.setCategoria(categorias[random.nextInt(categorias.length)]);
            v.setImagenUrl(faker.internet().image());
            v.setMotor(faker.options().option("2.0L Turbo", "1.8L", "3.5L V6"));
            v.setTransmision(faker.options().option("Automática", "Manual", "CTV"));
            v.setCombustible(faker.options().option("Gasolina", "Diésel", "Eléctrico"));
            v.setPasajeros(faker.number().numberBetween(2, 7));
            v.setDescripcion(faker.lorem().sentence());
            v.setDestacado(faker.bool().bool());
            v.setColores(Arrays.asList(faker.color().name(), faker.color().name()));
            vehiculos.add(v);
        }
        for (int i = 0; i < vehiculos.size(); i += BATCH_SIZE) {
            mongoTemplate.insertAll(vehiculos.subList(i, Math.min(i + BATCH_SIZE, vehiculos.size())));
        }

        return mongoTemplate.findAll(Vehiculo.class);
    }

    private void generarCitas(int cantidad, List<Usuario> usuarios, List<Vehiculo> vehiculos){
        List<Cita> citas = new ArrayList<>();
        String[] tipos = {"Interés en Vehículo", "Información Taller", "Mantenimiento", "Garantías", "Otros"};
        String[] sedes = {"Norte", "Sur", "Centro", "Oriente"};

        for (int i = 0; i < cantidad; i++){
            Usuario usuario = usuarios.get(random.nextInt(usuarios.size()));
            Vehiculo vehiculo = random.nextBoolean() ? vehiculos.get(random.nextInt(vehiculos.size())) : null;
            String tipo = tipos[random.nextInt(tipos.length)];

            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNombre(usuario.getNombreUser());
            usuarioDTO.setApellido(usuario.getApellidoUser());
            usuarioDTO.setCorreo(usuario.getCorreoUser());
            usuarioDTO.setIdentificacion(usuario.getIdentificacionUser());

            VehiculoDTO vehiculoDTO = null;
            if (vehiculo != null && (tipo.equals("Interés en Vehículo") || tipo.equals("Garantías"))) {
                vehiculoDTO = new VehiculoDTO();
                vehiculoDTO.setId(vehiculo.getId());
                vehiculoDTO.setMarca(vehiculo.getMarca());
                vehiculoDTO.setModelo(vehiculo.getModelo());
                vehiculoDTO.setPrecio(vehiculo.getPrecio());
                vehiculoDTO.setCategoria(vehiculo.getCategoria());
            }

            Cita cita = new Cita();
            cita.setUsuario(usuarioDTO);
            cita.setVehiculo(vehiculoDTO);
            cita.setTipo(tipo);
            cita.setSede(sedes[random.nextInt(sedes.length)]);
            cita.setComentario(tipo.equals("Otros") ? faker.lorem().sentence() : null);
            cita.setAtendida(false);
            cita.setLeida(false);
            cita.setEstado("Pendiente");
            cita.setFechaCreacion(LocalDateTime.now().minusDays(random.nextInt(30)));
            cita.setFechaAsignada(LocalDateTime.now().plusDays(random.nextInt(60)));
            cita.setNotasAdmin(faker.bool().bool() ? faker.lorem().sentence() : null);

            citas.add(cita);
        }

        for (int i = 0; i < citas.size(); i += BATCH_SIZE) {
            mongoTemplate.insertAll(citas.subList(i, Math.min(i + BATCH_SIZE, citas.size())));
            System.out.println("Citas insertadas: " + (i + BATCH_SIZE));
        }
    }
    
}
