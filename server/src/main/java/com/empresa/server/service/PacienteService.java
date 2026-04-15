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

/**
 * Servicio que contiene la lógica de negocio para la gestión de pacientes.
 * 
 * Proporciona métodos para:
 * - Obtener listados de pacientes (todos, específicos, huérfanos)
 * - Registrar nuevos pacientes con asignación opcional de médico
 * - Actualizar datos de pacientes y cambiar asignación de médico
 * - Convertir entidades a DTOs incluyendo historial de episodios
 * 
 * Los métodos marcados con @Transactional garantizan que las operaciones
 * se ejecuten dentro de una transacción de base de datos. Los marcados con
 * @Transactional(readOnly=true) son solo de lectura y optimizados para consultas.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see PacienteDTO
 * @see PacienteRepository
 */
@Service
public class PacienteService {


    /**
     * Repositorio para acceso a datos de pacientes.
     */
    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Repositorio para acceso a datos de médicos.
     */
    @Autowired
    private MedicoRepository medicoRepository;

    /**
     * Obtiene la lista de todos los pacientes registrados en el sistema.
     * 
     * Operación de solo lectura optimizada.
     * 
     * @return Lista de DTOs de todos los pacientes
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> obtenerTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un paciente específíco con todos sus datos incluyendo historial.
     * 
     * El historial incluye todos los episodios clínicos registrados del paciente.
     * 
     * Operación de solo lectura optimizada.
     * 
     * @param id Identificador del paciente
     * @return PacienteDTO con datos completos del paciente
     * @throws RuntimeException si el paciente no existe
     */
    @Transactional(readOnly = true)
    public PacienteDTO obtenerPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        return convertirADTO(paciente);
    }

    /**
     * Registra un nuevo paciente en el sistema.
     * 
     * Si se especifica un medicoId, el paciente se asigna automáticamente
     * a ese médico. Si el médico no existe, se lanza una excepción.
     * 
     * La operación se ejecuta dentro de una transacción.
     * 
     * @param dto DTO con los datos del paciente (nombre, apellido, NSS, medicoId)
     * @return PacienteDTO del paciente creado
     * @throws RuntimeException si el médico especificado no existe
     */
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

    /**
     * Actualiza los datos de un paciente existente (primera versión).
     * 
     * Permite cambiar nombre, apellido, NSS y reasignar médico.
     * Si medicoId es null, el paciente queda sin médico (huérfano).
     * 
     * La operación se ejecuta dentro de una transacción.
     * 
     * @param id Identificador del paciente a actualizar
     * @param dto DTO con los datos actualizados
     * @return PacienteDTO del paciente actualizado
     * @throws RuntimeException si el paciente o médico no existen
     * @deprecated Usar {@link #actualizar(Long, PacienteDTO)} en su lugar
     */
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

    /**
     * Obtiene la lista de pacientes huérfanos (sin médico asignado).
     * 
     * Estos son pacientes registrados en el sistema que están disponibles
     * para ser asignados a un médico.
     * 
     * Operación de solo lectura optimizada.
     * 
     * @return Lista de DTOs de pacientes sin médico asignado
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> obtenerHuerfanos() {
        return pacienteRepository.findByMedicoIsNull().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Paciente a su DTO correspondiente.
     * 
     * Mapea todos los campos incluyendo datos del médico asignado y el historial
     * de episodios clínicos del paciente.
     * 
     * @param paciente Entidad Paciente a convertir
     * @return PacienteDTO con los datos y historial mapeados
     */
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

    /**
     * Actualiza los datos de un paciente existente (segunda versión).
     * 
     * Permite cambiar nombre, apellido, NSS y reasignar médico.
     * Si medicoId es null, el paciente queda sin médico (huérfano).
     * 
     * La operación se ejecuta dentro de una transacción.
     * 
     * @param id Identificador del paciente a actualizar
     * @param dto DTO con los datos actualizados
     * @throws RuntimeException si el paciente no existe
     * @see #actualizarPaciente(Long, PacienteDTO)
     */
    @Transactional
    public void actualizar(Long id, PacienteDTO dto) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        p.setNombre(dto.getNombre());
        p.setApellido(dto.getApellido());
        p.setNss(dto.getNss());

        // Buscamos el médico si viene un ID, si no lo dejamos en null (huérfano)
        if (dto.getMedicoId() != null) {
            Medico m = medicoRepository.findById(dto.getMedicoId()).orElse(null);
            p.setMedico(m);
        } else {
            p.setMedico(null);
        }

        pacienteRepository.save(p);
    }
}