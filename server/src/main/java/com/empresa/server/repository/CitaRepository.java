package com.empresa.server.repository;

import com.empresa.server.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interfaz Repositorio para la gestin de operaciones CRUD sobre la entidad Cita.
 * 
 * Extiende JpaRepository proporcionando operaciones básicas de acceso a datos
 * como findAll(), findById(), save(), update(), delete() generadas automáticamente
 * por Spring Data JPA.
 * 
 * Además proporciona métodos de consulta personalizados para búsquedas específicas
 * de citas, como encontrar citas por médico o paciente con diferentes criterios
 * de ordenamiento.
 * 
 * Mapea las consultas a la tabla "citas" en la base de datos.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see com.empresa.server.model.Cita
 * @see com.empresa.server.controller.CitaController
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    
    /**
     * Busca todas las citas de un médico específíco, ordenadas por fecha y hora ascendente.
     * 
     * El médico puede consultar sus citas programadas en orden cronológico.
     * 
     * @param medicoId Identificador del médico
     * @return Lista de citas del médico ordenadas cronológicamente
     */
    List<Cita> findByMedicoIdOrderByFechaHoraAsc(Long medicoId);
    
    /**
     * Busca todas las citas de un paciente específíco.
     * 
     * El paciente puede consultar todas sus citas programadas.
     * 
     * @param pacienteId Identificador del paciente
     * @return Lista de citas del paciente especificado
     */
    List<Cita> findByPacienteId(Long pacienteId);
}