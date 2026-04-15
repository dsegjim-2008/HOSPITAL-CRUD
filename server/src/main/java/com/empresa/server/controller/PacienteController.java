package com.empresa.server.controller;

import com.empresa.server.dto.PacienteDTO;
import com.empresa.server.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST que gestiona los endpoints para operaciones CRUD de Pacientes.
 * 
 * Mapea las peticiones HTTP a métodos de servicio para registrar, consultar,
 * actualizar y eliminar pacientes en el sistema.
 * 
 * Endpoints disponibles:
 * - GET /api/pacientes - Obtiene la lista de todos los pacientes
 * - POST /api/pacientes - Registra un nuevo paciente
 * - GET /api/pacientes/{id} - Obtiene un paciente específíco con su historial
 * - PUT /api/pacientes/{id} - Actualiza datos de un paciente
 * - GET /api/pacientes/huerfanos - Obtiene pacientes sin médico asignado
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see PacienteService
 * @see PacienteDTO
 */
@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:5173")
public class PacienteController {

    /**
     * Servicio de lógica de negocio para la gestión de pacientes.
     */
    @Autowired
    private PacienteService pacienteService;

    /**
     * Obtiene la lista de todos los pacientes registrados en el sistema.
     * 
     * @return ResponseEntity con lista de DTOs de pacientes
     */
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.obtenerTodos());
    }

    /**
     * Registra un nuevo paciente en el sistema.
     * 
     * Si se proporciona un médico_id válido, el paciente se asigna automáticamente
     * a ese médico. Si el médico no existe, se retorna un error 400.
     * 
     * @param pacienteDTO Datos del paciente (nombre, apellido, NSS, medicoId)
     * @return ResponseEntity con el paciente creado (201) o error (400)
     */
    @PostMapping
    public ResponseEntity<?> registrarPaciente(@RequestBody PacienteDTO pacienteDTO) {
        try {
            PacienteDTO nuevoPaciente = pacienteService.registrarPaciente(pacienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaciente);
        } catch (RuntimeException e) {
            // Si el médico no existe, el Servicio lanza un error. Aquí lo capturamos y lo enviamos bonito a React.
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Obtiene los detalles de un paciente específíco incluyendo su historial.
     * 
     * El historial incluye todos los episodios clínicos registrados del paciente.
     * 
     * @param id Identificador del paciente
     * @return ResponseEntity con los datos del paciente (200) o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPaciente(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pacienteService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un paciente existente.
     * 
     * Permite cambiar el nombre, apellido, NSS y/o reasignar a otro médico.
     * Si se envía medicoId en null, el paciente queda sin médico (huérfano).
     * 
     * @param id Identificador del paciente a actualizar
     * @param dto Datos actualizados del paciente
     * @return ResponseEntity con mensaje de éxito
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO dto) {
        pacienteService.actualizar(id, dto);
        return ResponseEntity.ok("Paciente actualizado con éxito");
    }

    /**
     * Obtiene la lista de pacientes 'huérfanos' (sin médico asignado).
     * 
     * Estos son pacientes registrados en el sistema que están disponibles
     * para ser asignados a un médico.
     * 
     * @return ResponseEntity con lista de pacientes sin médico
     */
    @GetMapping("/huerfanos")
    public ResponseEntity<List<PacienteDTO>> listarHuerfanos() {
        return ResponseEntity.ok(pacienteService.obtenerHuerfanos());
    }
}