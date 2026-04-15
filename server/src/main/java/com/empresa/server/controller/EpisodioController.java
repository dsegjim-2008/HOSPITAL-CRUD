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

/**
 * Controlador REST que gestiona los endpoints para registrar episodios clínicos.
 * 
 * Un episodio es un evento médico en el historial de un paciente. Este controlador
 * permite añadir nuevos episodios (diagnósticos y tratamientos) al registro
 * médico de un paciente especfico.
 * 
 * Endpoints disponibles:
 * - POST /api/episodios/{pacienteId} - Añade un nuevo episodio clínico a un paciente
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see EpisodioDTO
 */
@RestController
@RequestMapping("/api/episodios")
@CrossOrigin(origins = "http://localhost:5173")
public class EpisodioController {


    /**
     * Repositorio para acceso a datos de episodios clínicos.
     */
    @Autowired
    private EpisodioRepository episodioRepository;

    /**
     * Repositorio para acceso a datos de pacientes.
     */
    @Autowired
    private PacienteRepository pacienteRepository;


    /**
     * Añade un nuevo episodio clínico al historial de un paciente.
     * 
     * Registra un evento médico con fecha, diagnóstico y tratamiento.
     * Si no se especifica fecha, se utiliza la fecha actual del sistema.
     * 
     * @param pacienteId Identificador del paciente propietario del episodio
     * @param dto Datos del episodio (fecha, diagnostico, tratamiento)
     * @return ResponseEntity con mensaje de éxito o error 404 si paciente no existe
     */
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