package com.empresa.server.controller;

import com.empresa.server.dto.CitaDTO;
import com.empresa.server.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que gestiona los endpoints para operaciones CRUD de Citas Médicas.
 *
 * Delega toda la lógica de negocio en {@link CitaService}.
 *
 * Endpoints disponibles:
 * - GET /api/citas/medico/{medicoId} - Obtiene citas de un médico específico
 * - POST /api/citas - Programa una nueva cita
 * - PUT /api/citas/{id} - Actualiza una cita existente
 * - DELETE /api/citas/{id} - Cancela una cita
 *
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see CitaService
 * @see CitaDTO
 */
@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173")
public class CitaController {

    /**
     * Servicio de lógica de negocio para la gestión de citas.
     */
    @Autowired
    private CitaService citaService;

    /**
     * Obtiene todas las citas programadas para un médico específico,
     * ordenadas cronológicamente por fecha y hora.
     *
     * @param medicoId Identificador del médico
     * @return Lista de DTOs de citas en orden cronológico
     */
    @GetMapping("/medico/{medicoId}")
    public List<CitaDTO> listarPorMedico(@PathVariable Long medicoId) {
        return citaService.listarPorMedico(medicoId);
    }

    /**
     * Programa una nueva cita médica.
     *
     * Crea una cita entre un médico y un paciente en la fecha, hora y sala especificadas.
     * Ambos el médico y el paciente deben existir en el sistema.
     *
     * @param dto Datos de la cita (fechaHora, motivo, sala, medicoId, pacienteId)
     * @return ResponseEntity con mensaje de confirmación (200) o error (400)
     */
    @PostMapping
    public ResponseEntity<?> crearCita(@RequestBody CitaDTO dto) {
        try {
            citaService.crearCita(dto);
            return ResponseEntity.ok("Cita confirmada");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualiza los datos de una cita existente.
     *
     * Permite cambiar fecha, hora, motivo, sala y/o el paciente asociado.
     *
     * @param id  Identificador de la cita a actualizar
     * @param dto Datos actualizados de la cita
     * @return ResponseEntity con mensaje de confirmación (200) o error (400)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody CitaDTO dto) {
        try {
            citaService.actualizarCita(id, dto);
            return ResponseEntity.ok("Cita actualizada");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Cancela una cita eliminando su registro del sistema.
     *
     * @param id Identificador de la cita a eliminar
     * @return ResponseEntity con mensaje de confirmación (200) o error 404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCita(@PathVariable Long id) {
        try {
            citaService.eliminarCita(id);
            return ResponseEntity.ok("Cita eliminada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}