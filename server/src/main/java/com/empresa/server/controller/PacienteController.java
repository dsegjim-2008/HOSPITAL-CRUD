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

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:5173")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // GET: Listar todos los pacientes
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.obtenerTodos());
    }

    // POST: Registrar un paciente (y asignarle un médico)
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

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPaciente(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pacienteService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // En PacienteController.java
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO dto) {
        pacienteService.actualizar(id, dto);
        return ResponseEntity.ok("Paciente actualizado con éxito");
    }

    @GetMapping("/huerfanos")
    public ResponseEntity<List<PacienteDTO>> listarHuerfanos() {
        return ResponseEntity.ok(pacienteService.obtenerHuerfanos());
    }
}