package com.empresa.server.controller;

import com.empresa.server.dto.CitaDTO;
import com.empresa.server.model.*;
import com.empresa.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST que gestiona los endpoints para operaciones CRUD de Citas Médicas.
 * 
 * Mapea las peticiones HTTP a métodos de repositorio para crear, consultar,
 * actualizar y eliminar citas entre pacientes y médicos.
 * 
 * Endpoints disponibles:
 * - GET /api/citas/medico/{medicoId} - Obtiene citas de un médico específíco
 * - POST /api/citas - Programa una nueva cita
 * - PUT /api/citas/{id} - Actualiza una cita existente
 * - DELETE /api/citas/{id} - Cancela una cita
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see CitaDTO
 */
@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173")
public class CitaController {


    /**
     * Repositorio para acceso a datos de citas.
     */
    @Autowired private CitaRepository citaRepository;
    
    /**
     * Repositorio para acceso a datos de médicos.
     */
    @Autowired private MedicoRepository medicoRepository;
    
    /**
     * Repositorio para acceso a datos de pacientes.
     */
    @Autowired private PacienteRepository pacienteRepository;

    /**
     * Obtiene todas las citas programadas para un médico específíco,
     * ordenadas cronológicamente por fecha y hora.
     * 
     * @param medicoId Identificador del médico
     * @return Lista de citas del médico en orden cronológico
     */
    @GetMapping("/medico/{medicoId}")
    public List<CitaDTO> listarPorMedico(@PathVariable Long medicoId) {
        return citaRepository.findByMedicoIdOrderByFechaHoraAsc(medicoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Programa una nueva cita médica.
     * 
     * Crea una cita entre un médico y un paciente en una fecha, hora y sala especificada.
     * Ambos, el médico y paciente, deben existir en el sistema.
     * 
     * @param dto Datos de la cita (fechaHora, motivo, sala, medicoId, pacienteId)
     * @return ResponseEntity con mensaje de confirmación
     */
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

    /**
     * Actualiza los datos de una cita existente.
     * 
     * Permite cambiar fecha, hora, motivo, sala y/o el paciente asociado.
     * 
     * @param id Identificador de la cita a actualizar
     * @param dto Datos actualizados de la cita
     * @return ResponseEntity con mensaje de confirmación
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody CitaDTO dto) {
        Cita cita = citaRepository.findById(id).orElseThrow();
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        cita.setSala(dto.getSala());
        // Si cambia el paciente:
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId()).orElseThrow();
        cita.setPaciente(paciente);
        
        citaRepository.save(cita);
        return ResponseEntity.ok("Cita actualizada");
    }

    /**
     * Cancelala una cita eliminando su registro del sistema.
     * 
     * @param id Identificador de la cita a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCita(@PathVariable Long id) {
        citaRepository.deleteById(id);
        return ResponseEntity.ok("Cita eliminada");
    }

    /**
     * Convierte una entidad Cita a su correspondiente DTO.
     * 
     * Mapea los datos de la cita incluyendo información formateada del médico
     * y paciente involucrados.
     * 
     * @param c Entidad Cita a convertir
     * @return CitaDTO con los datos mapeados
     * @see CitaDTO
     */
    private CitaDTO convertirADTO(Cita c) {
        return new CitaDTO(c.getId(), c.getFechaHora(), c.getMotivo(), c.getSala(),
                c.getMedico().getId(), c.getMedico().getNombreCompleto(),
                c.getPaciente().getId(), c.getPaciente().getNombre() + " " + c.getPaciente().getApellido());
    }
}