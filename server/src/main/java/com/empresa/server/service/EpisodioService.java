package com.empresa.server.service;

import com.empresa.server.dto.EpisodioDTO;
import com.empresa.server.model.Episodio;
import com.empresa.server.repository.EpisodioRepository;
import com.empresa.server.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Servicio que contiene la lógica de negocio para la gestión de episodios clínicos.
 *
 * Proporciona métodos para:
 * - Registrar nuevos episodios en el historial de un paciente
 *
 * Los métodos de escritura están marcados con @Transactional para garantizar
 * la integridad de los datos en la base de datos.
 *
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see EpisodioDTO
 * @see EpisodioRepository
 */
@Service
public class EpisodioService {

    /**
     * Repositorio para acceso a datos de episodios clínicos.
     */
    @Autowired
    private EpisodioRepository episodioRepository;

    /**
     * Repositorio para acceso a datos de pacientes.
     */
    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Añade un nuevo episodio clínico al historial de un paciente.
     *
     * Registra un evento médico con fecha, diagnóstico y tratamiento.
     * Si no se especifica fecha en el DTO, se utiliza la fecha actual del sistema.
     * La operación se ejecuta dentro de una transacción.
     *
     * @param pacienteId Identificador del paciente al que pertenece el episodio
     * @param dto        Datos del episodio (fecha, diagnostico, tratamiento)
     * @throws RuntimeException si el paciente con el ID especificado no existe
     */
    @Transactional
    public void añadirEpisodio(Long pacienteId, EpisodioDTO dto) {
        pacienteRepository.findById(pacienteId).ifPresentOrElse(
                paciente -> {
                    Episodio episodio = new Episodio();
                    episodio.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
                    episodio.setDiagnostico(dto.getDiagnostico());
                    episodio.setTratamiento(dto.getTratamiento());
                    episodio.setPaciente(paciente);
                    episodioRepository.save(episodio);
                },
                () -> { throw new RuntimeException("Paciente con ID " + pacienteId + " no encontrado"); }
        );
    }
}
