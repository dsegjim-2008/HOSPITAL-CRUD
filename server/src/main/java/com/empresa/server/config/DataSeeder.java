package com.empresa.server.config;

import com.empresa.server.model.*;
import com.empresa.server.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MedicoRepository medicoRepo;
    private final PacienteRepository pacienteRepo;
    private final EpisodioRepository episodioRepo;
    private final CitaRepository citaRepo;

    public DataSeeder(MedicoRepository mr, PacienteRepository pr, EpisodioRepository er, CitaRepository cr) {
        this.medicoRepo = mr; this.pacienteRepo = pr; this.episodioRepo = er; this.citaRepo = cr;
    }

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