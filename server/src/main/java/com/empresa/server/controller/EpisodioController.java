package com.empresa.server.controller;

import com.empresa.server.dto.EpisodioDTO;
import com.empresa.server.service.EpisodioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que gestiona los endpoints para registrar episodios clínicos.
 *
 * Un episodio es un evento médico en el historial de un paciente. Este controlador
 * delega en {@link EpisodioService} para añadir nuevos episodios (diagnósticos
 * y tratamientos) al registro médico de un paciente.
 *
 * Endpoints disponibles:
 * - POST /api/episodios/{pacienteId} - Añade un nuevo episodio clínico a un paciente
 *
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see EpisodioService
 * @see EpisodioDTO
 */
@RestController
@RequestMapping("/api/episodios")
@CrossOrigin(origins = "http://localhost:5173")
public class EpisodioController {

    /**
     * Servicio de lógica de negocio para la gestión de episodios clínicos.
     */
    @Autowired
    private EpisodioService episodioService;

    /**
     * Añade un nuevo episodio clínico al historial de un paciente.
     *
     * Registra un evento médico con fecha, diagnóstico y tratamiento.
     * Si no se especifica fecha en el DTO, se utiliza la fecha actual del sistema.
     *
     * @param pacienteId Identificador del paciente al que pertenece el episodio
     * @param dto        Datos del episodio (fecha, diagnostico, tratamiento)
     * @return ResponseEntity con mensaje de éxito (200) o error 404 si el paciente no existe
     */
    @PostMapping("/{pacienteId}")
    public ResponseEntity<?> añadirEpisodio(@PathVariable Long pacienteId, @RequestBody EpisodioDTO dto) {
        try {
            episodioService.añadirEpisodio(pacienteId, dto);
            return ResponseEntity.ok("Episodio añadido correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}