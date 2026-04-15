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

/**
 * Servicio que contiene la lógica de negocio para la gestión de médicos.
 * 
 * Proporciona métodos para:
 * - Obtener listados de médicos
 * - Crear nuevos médicos (el usuario se genera automáticamente)
 * - Eliminar médicos de forma segura (desasignando pacientes)
 * - Convertir entidades a DTOs para transferencia de datos
 * 
 * Los métodos marcados con @Transactional garantizan que las operaciones
 * se ejecuten dentro de una transacción de base de datos.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see MedicoDTO
 * @see MedicoRepository
 */
@Service
public class MedicoService {


    /**
     * Repositorio para acceso a datos de médicos.
     */
    @Autowired
    private MedicoRepository medicoRepository;
    
    /**
     * Repositorio para acceso a datos de pacientes.
     */
    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Obtiene la lista de todos los médicos registrados en el sistema.
     * 
     * @return Lista de DTOs de médicos
     */
    public List<MedicoDTO> obtenerTodos() {
        return medicoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo médico en el sistema.
     * 
     * El usuario se genera automáticamente mediante el método @PrePersist de la entidad.
     * El formato del usuario es: primerosTres(nombre)+primerosTres(apellido)+año
     * 
     * La operación se ejecuta dentro de una transacción para garantizar
     * la integridad de los datos.
     * 
     * @param dto DTO con los datos del médico (nombre, apellido, especialidad, etc.)
     * @return MedicoDTO del médico creado
     */
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

    /**
     * Elimina un médico del sistema de forma segura.
     * 
     * Antes de eliminar el médico, desasigna todos sus pacientes asociados,
     * dejando a los pacientes como "huérfanos" (sin médico). Esto evita
     * que se pierdan registros de pacientes al eliminar un médico.
     * 
     * Las citas del médico también se eliminan debido a la configuración
     * de cascade en la relación OneToMany.
     * 
     * La operación se ejecuta dentro de una transacción.
     * 
     * @param id Identificador del médico a eliminar
     * @throws RuntimeException si el médico no existe
     */
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

    /**
     * Convierte una entidad Médico a su DTO correspondiente.
     * 
     * Mapea todos los campos incluyendo el nombre completo formateado.
     * 
     * @param medico Entidad Médico a convertir
     * @return MedicoDTO con los datos mapeados
     */
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