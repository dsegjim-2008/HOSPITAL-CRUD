package com.empresa.server.repository;

import com.empresa.server.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz Repositorio para la gestin de operaciones CRUD sobre la entidad Médico.
 * 
 * Extiende JpaRepository proporcionando operaciones básicas de acceso a datos
 * como findAll(), findById(), save(), update(), delete() generadas automáticamente
 * por Spring Data JPA.
 * 
 * Además proporciona métodos de consulta personalizados para búsquedas específicas
 * de médicos basadas en criterios como número de colegiado.
 * 
 * Mapea las consultas a la tabla "medicos" en la base de datos.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see com.empresa.server.model.Medico
 * @see com.empresa.server.service.MedicoService
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
    /**
     * Busca un médico por su número de colegiación.
     * 
     * @param numeroColegiado Número de colegiado único del médico
     * @return Optional que contiene el médico si existe, vacío si no
     */
    Optional<Medico> findByNumeroColegiado(String numeroColegiado);
}