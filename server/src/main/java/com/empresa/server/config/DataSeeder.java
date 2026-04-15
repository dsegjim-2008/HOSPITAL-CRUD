package com.empresa.server.config;

import com.empresa.server.model.*;
import com.empresa.server.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Componente encargado de popular la base de datos con datos iniciales de prueba.
 * 
 * Se ejecuta automáticamente al iniciar la aplicación si la base de datos está vacía.
 * Crea 4 médicos con diferentes especialidades y 20 pacientes asignados aleatoriamente
 * a los médicos, junto con citas y episodios clínicos.
 * 
 * Los datos iniciales incluyen:
 * - 4 médicos con especialidades diversas (Neurología, Cardiología, Traumatología, Pediatría)
 * - 20 pacientes distribuidos aleatoriamente entre los médicos
 * - Citas programadas con fechas aleatorias
 * - Episodios clínicos para registrar el historial médico
 * 
 * Esta clase implementa CommandLineRunner, que asegura que el método run() se ejecute
 * después de que Spring Boot haya inicializado completamente la aplicación.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see CommandLineRunner
 */
@Component
public class DataSeeder implements CommandLineRunner {


    /**
     * Repositorio para gestionar operaciones CRUD de médicos.
     */
    private final MedicoRepository medicoRepo;
    
    /**
     * Repositorio para gestionar operaciones CRUD de pacientes.
     */
    private final PacienteRepository pacienteRepo;
    
    /**
     * Repositorio para gestionar operaciones CRUD de episodios clínicos.
     */
    private final EpisodioRepository episodioRepo;
    
    /**
     * Repositorio para gestionar operaciones CRUD de citas médicas.
     */
    private final CitaRepository citaRepo;

    /**
     * Constructor que inyecta autowired todos los repositorios necesarios.
     * 
     * @param mr Repositorio de Médicos
     * @param pr Repositorio de Pacientes
     * @param er Repositorio de Episodios
     * @param cr Repositorio de Citas
     */
    public DataSeeder(MedicoRepository mr, PacienteRepository pr, EpisodioRepository er, CitaRepository cr) {
        this.medicoRepo = mr; this.pacienteRepo = pr; this.episodioRepo = er; this.citaRepo = cr;
    }

    /**
     * Método que se ejecuta automáticamente al iniciar la aplicación.
     * 
     * Verifica si la base de datos está vacía (no hay médicos registrados).
     * Si está vacía, procede a generar e insertar datos de prueba para facilitar
     * el testing y demostración de la aplicación.
     * 
     * Flujo de datos creados:
     * 1. Crea 4 médicos con especialidades variadas
     * 2. Crea 20 pacientes asignados aleatoriamente a los médicos
     * 3. Genera citas médicas entre médicos y pacientes
     * 4. Registra episodios clínicos en el historial de pacientes
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     * @throws Exception si ocurre un error durante la inserción de datos
     */
    @Override
    public void run(String... args) {
        if (medicoRepo.count() == 0) {
            Random r = new Random();
            
            // 1. Médicos con nombres desglosados
            Object[][] pool = {
                {"David", null, "Segura", "Jiménez", "Neurología", 1990},
                {"Laura", "Elena", "García", null, "Cardiología", 1985},
                {"Marc", null, "Smith", "Johnson", "Traumatología", 1978},
                {"Ana", "Belén", "Pérez", "López", "Pediatría", 1992}
            };

            List<Medico> medicos = new ArrayList<>();
            for (Object[] d : pool) {
                Medico m = new Medico();
                m.setPrimerNombre((String)d[0]); m.setSegundoNombre((String)d[1]);
                m.setPrimerApellido((String)d[2]); m.setSegundoApellido((String)d[3]);
                m.setEspecialidad((String)d[4]);
                m.setFechaNacimiento(LocalDate.of((int)d[5], r.nextInt(11)+1, r.nextInt(27)+1));
                m.setNumeroColegiado("COL-" + (r.nextInt(8999)+1000));
                medicos.add(medicoRepo.save(m));
            }

            // 2. Pacientes y Citas
            String[] nomP = {"Carlos", "Julia", "Roberto", "Marta"};
            for (int i = 0; i < 20; i++) {
                Paciente p = new Paciente();
                p.setNombre(nomP[r.nextInt(nomP.length)]);
                p.setApellido("Paciente " + i);
                p.setNss("28-" + (r.nextInt(89999999)+10000000) + "-01");
                p.setMedico(medicos.get(r.nextInt(medicos.size())));
                Paciente guardado = pacienteRepo.save(p);

                // Crear Cita
                Cita c = new Cita();
                c.setFechaHora(LocalDateTime.now().plusDays(r.nextInt(10) + 1).withHour(9 + r.nextInt(6)).withMinute(0));
                c.setMotivo("Revisión ordinaria");
                c.setSala("Consultorio " + (r.nextInt(5)+1));
                c.setMedico(guardado.getMedico());
                c.setPaciente(guardado);
                citaRepo.save(c);
            }
            System.out.println("✅ Hospital cargado con estructura de nombres desglosada y citas.");
        }
    }
}