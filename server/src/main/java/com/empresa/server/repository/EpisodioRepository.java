package com.empresa.server.repository;

import com.empresa.server.model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interfaz Repositorio para la gestión de operaciones CRUD sobre la entidad Episodio.
 * 
 * Extiende JpaRepository proporcionando operaciones básicas de acceso a datos
 * como findAll(), findById(), save(), update(), delete() generadas automáticamente
 * por Spring Data JPA.
 * 
 * Además proporciona métodos de consulta personalizados para búsquedas específicas
 * de episodios clínicos associados a pacientes.
 * 
 * Mapea las consultas a la tabla "episodios" en la base de datos.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see com.empresa.server.model.Episodio
 * @see com.empresa.server.controller.EpisodioController
 */
@Repository
public interface EpisodioRepository extends JpaRepository<Episodio, Long> {
    
    /**
     * Busca todos los episodios clínicos de un paciente específíco.
     * 
     * Permite acceder al historial médico completo del paciente.
     * 
     * @param pacienteId Identificador del paciente
     * @return Lista de episodios del paciente especificado
     */
    List<Episodio> findByPacienteId(Long pacienteId);
}