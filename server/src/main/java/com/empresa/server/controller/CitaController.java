package com.empresa.server.controller;

import com.empresa.server.dto.CitaDTO;
import com.empresa.server.model.*;
import com.empresa.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173")
public class CitaController {

    @Autowired private CitaRepository citaRepository;
    @Autowired private MedicoRepository medicoRepository;
    @Autowired private PacienteRepository pacienteRepository;

    @GetMapping("/medico/{medicoId}")
    public List<CitaDTO> listarPorMedico(@PathVariable Long medicoId) {
        return citaRepository.findByMedicoIdOrderByFechaHoraAsc(medicoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> crearCita(@RequestBody CitaDTO dto) {
        Medico medico = medicoRepository.findById(dto.getMedicoId()).orElseThrow();
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId()).orElseThrow();

        Cita cita = new Cita();
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        cita.setSala(dto.getSala());
        cita.setMedico(medico);
        cita.setPaciente(paciente);

        citaRepository.save(cita);
        return ResponseEntity.ok("Cita confirmada");
    }

    private CitaDTO convertirADTO(Cita c) {
        return new CitaDTO(c.getId(), c.getFechaHora(), c.getMotivo(), c.getSala(),
                c.getMedico().getId(), c.getMedico().getNombreCompleto(),
                c.getPaciente().getId(), c.getPaciente().getNombre() + " " + c.getPaciente().getApellido());
    }
}