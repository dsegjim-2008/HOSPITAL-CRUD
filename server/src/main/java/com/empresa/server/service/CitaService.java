package com.empresa.server.service;

import com.empresa.server.dto.CitaDTO;
import com.empresa.server.model.Cita;
import com.empresa.server.model.Medico;
import com.empresa.server.model.Paciente;
import com.empresa.server.repository.CitaRepository;
import com.empresa.server.repository.MedicoRepository;
import com.empresa.server.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que contiene la lógica de negocio para la gestión de citas médicas.
 *
 * Proporciona métodos para:
 * - Obtener citas de un médico ordenadas cronológicamente
 * - Programar nuevas citas entre médico y paciente
 * - Actualizar datos de una cita existente
 * - Cancelar (eliminar) una cita del sistema
 * - Convertir entidades a DTOs para transferencia de datos
 *
 * Los métodos de escritura están marcados con @Transactional para garantizar
 * la integridad de los datos en la base de datos.
 *
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see CitaDTO
 * @see CitaRepository
 */
@Service
public class CitaService {

    /**
     * Repositorio para acceso a datos de citas.
     */
    @Autowired
    private CitaRepository citaRepository;

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
     * Obtiene todas las citas de un médico ordenadas cronológicamente.
     *
     * Operación de solo lectura optimizada.
     *
     * @param medicoId Identificador del médico
     * @return Lista de DTOs de citas en orden ascendente de fecha y hora
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorMedico(Long medicoId) {
        return citaRepository.findByMedicoIdOrderByFechaHoraAsc(medicoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Programa una nueva cita médica entre un médico y un paciente.
     *
     * Ambos el médico y el paciente deben existir en el sistema.
     * La operación se ejecuta dentro de una transacción.
     *
     * @param dto Datos de la cita (fechaHora, motivo, sala, medicoId, pacienteId)
     * @throws RuntimeException si el médico o paciente especificado no existe
     */
    @Transactional
    public void crearCita(CitaDTO dto) {
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico con ID " + dto.getMedicoId() + " no encontrado"));
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente con ID " + dto.getPacienteId() + " no encontrado"));

        Cita cita = new Cita();
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        cita.setSala(dto.getSala());
        cita.setMedico(medico);
        cita.setPaciente(paciente);

        citaRepository.save(cita);
    }

    /**
     * Actualiza los datos de una cita existente.
     *
     * Permite cambiar fecha, hora, motivo, sala y el paciente asociado.
     * La operación se ejecuta dentro de una transacción.
     *
     * @param id  Identificador de la cita a actualizar
     * @param dto Datos actualizados de la cita
     * @throws RuntimeException si la cita o el paciente especificado no existe
     */
    @Transactional
    public void actualizarCita(Long id, CitaDTO dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita con ID " + id + " no encontrada"));

        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        cita.setSala(dto.getSala());

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente con ID " + dto.getPacienteId() + " no encontrado"));
        cita.setPaciente(paciente);

        citaRepository.save(cita);
    }

    /**
     * Cancela y elimina una cita del sistema.
     *
     * La operación se ejecuta dentro de una transacción.
     *
     * @param id Identificador de la cita a eliminar
     * @throws RuntimeException si la cita no existe
     */
    @Transactional
    public void eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita con ID " + id + " no encontrada");
        }
        citaRepository.deleteById(id);
    }

    /**
     * Convierte una entidad Cita a su correspondiente DTO.
     *
     * Mapea los datos de la cita incluyendo información del médico
     * y paciente involucrados.
     *
     * @param c Entidad Cita a convertir
     * @return CitaDTO con los datos mapeados
     */
    private CitaDTO convertirADTO(Cita c) {
        return new CitaDTO(
                c.getId(),
                c.getFechaHora(),
                c.getMotivo(),
                c.getSala(),
                c.getMedico().getId(),
                c.getMedico().getNombreCompleto(),
                c.getPaciente().getId(),
                c.getPaciente().getNombre() + " " + c.getPaciente().getApellido()
        );
    }
}
