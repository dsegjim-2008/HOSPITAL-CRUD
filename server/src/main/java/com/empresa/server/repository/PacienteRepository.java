package com.empresa.server.repository;

import com.empresa.server.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interfaz Repositorio para la gestión de operaciones CRUD sobre la entidad Paciente.
 * 
 * Extiende JpaRepository proporcionando operaciones básicas de acceso a datos
 * como findAll(), findById(), save(), update(), delete() generadas automáticamente
 * por Spring Data JPA.
 * 
 * Además proporciona métodos de consulta personalizados para búsquedas específicas
 * de pacientes, como encontrar pacientes por médico asignado o pacientes sin médico
 * (huérfanos).
 * 
 * Mapea las consultas a la tabla "pacientes" en la base de datos.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see com.empresa.server.model.Paciente
 * @see com.empresa.server.service.PacienteService
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    /**
     * Busca todos los pacientes asignados a un médico específíco.
     * 
     * @param medicoId Identificador del médico
     * @return Lista de pacientes del médico especificado
     */
    List<Paciente> findByMedicoId(Long medicoId);
    
    /**
     * Busca todos los pacientes que no tienen médico asignado (huérfanos).
     * 
     * Estos son pacientes registrados en el sistema pero sin asignación de médico.
     * 
     * @return Lista de pacientes sin médico asignado
     */
    List<Paciente> findByMedicoIsNull();
}