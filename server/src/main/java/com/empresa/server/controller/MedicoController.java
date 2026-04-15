package com.empresa.server.controller;

import com.empresa.server.dto.MedicoDTO;
import com.empresa.server.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que gestiona los endpoints para operaciones CRUD de Médicos.
 * 
 * Mapea las peticiones HTTP a métodos de servicio para crear, leer, actualizar
 * y eliminar registros de médicos en la base de datos.
 * 
 * Endpoints disponibles:
 * - GET /api/medicos - Obtiene la lista de todos los médicos
 * - POST /api/medicos - Crea un nuevo médico
 * - DELETE /api/medicos/{id} - Elimina un médico y desasigna sus pacientes
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see MedicoService
 * @see MedicoDTO
 */
@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "http://localhost:5173")
public class MedicoController {

    /**
     * Servicio de lógica de negocio para la gestión de médicos.
     */
    @Autowired
    private MedicoService medicoService;

    /**
     * Obtiene la lista de todos los médicos registrados en el sistema.
     * 
     * @return Lista de DTOs de médicos (nombre, especialidad, credenciales, etc.)
     */
    @GetMapping
    public List<MedicoDTO> listarMedicos() {
        return medicoService.obtenerTodos();
    }

    /**
     * Crea un nuevo médico en el sistema.
     * 
     * El usuario se genera automáticamente con formato: primNombrePrimApellidoAño
     * 
     * @param medicoDTO Datos del médico a crear (nombre, apellido, especialidad, etc.)
     * @return ResponseEntity con el médico creado y código HTTP 200
     */
    @PostMapping
    public ResponseEntity<MedicoDTO> crearMedico(@RequestBody MedicoDTO medicoDTO) {
        MedicoDTO nuevoMedico = medicoService.crearMedico(medicoDTO);
        return ResponseEntity.ok(nuevoMedico);
    }

    /**
     * Elimina un médico del sistema de forma segura.
     * 
     * Al eliminar un médico, todos sus pacientes asignados son desasignados
     * (quedan como huérfanos sin médico). Las citas del médico también se eliminan.
     * 
     * @param id Identificador del médico a eliminar
     * @return ResponseEntity con mensaje de éxito o error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedico(@PathVariable Long id) {
        try {
            medicoService.eliminarMedicoSeguro(id);
            return ResponseEntity.ok("Médico eliminado y pacientes desasignados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}