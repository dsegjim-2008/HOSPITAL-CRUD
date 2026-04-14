package com.empresa.server.controller;

import com.empresa.server.dto.EpisodioDTO;
import com.empresa.server.model.Episodio;
import com.empresa.server.model.Paciente;
import com.empresa.server.repository.EpisodioRepository;
import com.empresa.server.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/episodios")
@CrossOrigin(origins = "http://localhost:5173")
public class EpisodioController {

    @Autowired
    private EpisodioRepository episodioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping("/{pacienteId}")
    public ResponseEntity<?> añadirEpisodio(@PathVariable Long pacienteId, @RequestBody EpisodioDTO dto) {
        return pacienteRepository.findById(pacienteId).map(paciente -> {
            Episodio episodio = new Episodio();
            episodio.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
            episodio.setDiagnostico(dto.getDiagnostico());
            episodio.setTratamiento(dto.getTratamiento());
            episodio.setPaciente(paciente);
            
            episodioRepository.save(episodio);
            return ResponseEntity.ok("Episodio añadido correctamente");
        }).orElse(ResponseEntity.notFound().build());
    }
}