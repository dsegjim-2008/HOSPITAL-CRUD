package com.empresa.server.service;

import com.empresa.server.dto.EpisodioDTO;
import com.empresa.server.dto.PacienteDTO;
import com.empresa.server.model.Medico;
import com.empresa.server.model.Paciente;
import com.empresa.server.repository.MedicoRepository;
import com.empresa.server.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public List<PacienteDTO> obtenerTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // NUEVO: Obtener un paciente específico con todo su historial
    @Transactional(readOnly = true)
    public PacienteDTO obtenerPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        return convertirADTO(paciente);
    }

    @Transactional
    public PacienteDTO registrarPaciente(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setNss(dto.getNss());

        if (dto.getMedicoId() != null) {
            Medico medico = medicoRepository.findById(dto.getMedicoId())
                    .orElseThrow(() -> new RuntimeException("El médico con ID " + dto.getMedicoId() + " no existe."));
            paciente.setMedico(medico);
        }

        Paciente guardado = pacienteRepository.save(paciente);
        return convertirADTO(guardado);
    }

    @Transactional
    public PacienteDTO actualizarPaciente(Long id, PacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Actualizamos datos básicos
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setNss(dto.getNss());

        // Lógica para reasignar médico si viene en el JSON
        if (dto.getMedicoId() != null) {
            Medico nuevoMedico = medicoRepository.findById(dto.getMedicoId())
                    .orElseThrow(() -> new RuntimeException("El nuevo médico no existe."));
            paciente.setMedico(nuevoMedico);
        } else {
            // Si mandan null, significa que lo dejan sin médico asignado
            paciente.setMedico(null); 
        }

        Paciente actualizado = pacienteRepository.save(paciente);
        return convertirADTO(actualizado);
    }

    @Transactional(readOnly = true)
    public List<PacienteDTO> obtenerHuerfanos() {
        return pacienteRepository.findByMedicoIsNull().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ACTUALIZADO: Ahora también mapea los episodios
    private PacienteDTO convertirADTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNombre(paciente.getNombre());
        dto.setApellido(paciente.getApellido());
        dto.setNss(paciente.getNss());

        if (paciente.getMedico() != null) {
            dto.setMedicoId(paciente.getMedico().getId());
            // CAMBIO AQUÍ: Usamos getNombreCompleto() en lugar de getNombre()
            dto.setNombreMedico(paciente.getMedico().getNombreCompleto());
        }

        // Si tiene episodios, los convertimos a DTO
        if (paciente.getEpisodios() != null) {
            List<EpisodioDTO> episodiosDTO = paciente.getEpisodios().stream().map(ep -> {
                EpisodioDTO epDTO = new EpisodioDTO();
                epDTO.setId(ep.getId());
                epDTO.setFecha(ep.getFecha());
                epDTO.setDiagnostico(ep.getDiagnostico());
                epDTO.setTratamiento(ep.getTratamiento());
                return epDTO;
            }).collect(Collectors.toList());
            
            dto.setEpisodios(episodiosDTO);
        }

        return dto;
    }
}