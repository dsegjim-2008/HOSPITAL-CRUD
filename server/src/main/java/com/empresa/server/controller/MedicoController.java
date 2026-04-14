package com.empresa.server.controller;

import com.empresa.server.dto.MedicoDTO;
import com.empresa.server.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "http://localhost:5173")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public List<MedicoDTO> listarMedicos() {
        return medicoService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> crearMedico(@RequestBody MedicoDTO medicoDTO) {
        MedicoDTO nuevoMedico = medicoService.crearMedico(medicoDTO);
        return ResponseEntity.ok(nuevoMedico);
    }

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