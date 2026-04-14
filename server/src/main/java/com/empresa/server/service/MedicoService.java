package com.empresa.server.service;

import com.empresa.server.dto.MedicoDTO;
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
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;
    
    @Autowired
    private PacienteRepository pacienteRepository;

    public List<MedicoDTO> obtenerTodos() {
        return medicoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicoDTO crearMedico(MedicoDTO dto) {
        Medico medico = new Medico();
        medico.setPrimerNombre(dto.getPrimerNombre());
        medico.setSegundoNombre(dto.getSegundoNombre());
        medico.setPrimerApellido(dto.getPrimerApellido());
        medico.setSegundoApellido(dto.getSegundoApellido());
        medico.setEspecialidad(dto.getEspecialidad());
        medico.setNumeroColegiado(dto.getNumeroColegiado());
        medico.setFechaNacimiento(dto.getFechaNacimiento());
        
        Medico guardado = medicoRepository.save(medico);
        return convertirADTO(guardado);
    }

    @Transactional
    public void eliminarMedicoSeguro(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // Desasignar pacientes antes de borrar
        if (medico.getPacientes() != null) {
            for (Paciente paciente : medico.getPacientes()) {
                paciente.setMedico(null);
                pacienteRepository.save(paciente);
            }
        }
        medicoRepository.delete(medico);
    }

    public MedicoDTO convertirADTO(Medico medico) {
        MedicoDTO dto = new MedicoDTO();
        dto.setId(medico.getId());
        dto.setPrimerNombre(medico.getPrimerNombre());
        dto.setSegundoNombre(medico.getSegundoNombre());
        dto.setPrimerApellido(medico.getPrimerApellido());
        dto.setSegundoApellido(medico.getSegundoApellido());
        dto.setNombreCompleto(medico.getNombreCompleto());
        dto.setEspecialidad(medico.getEspecialidad());
        dto.setNumeroColegiado(medico.getNumeroColegiado());
        dto.setUsuario(medico.getUsuario());
        dto.setFechaNacimiento(medico.getFechaNacimiento());
        return dto;
    }
}