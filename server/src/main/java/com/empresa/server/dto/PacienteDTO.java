package com.empresa.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * DTO (Data Transfer Object) para transferir información de pacientes entre capas.
 * 
 * Este DTO es utilizado para transferir datos de pacientes desde la capa de servicio
 * hacia los controladores REST y posteriormente al cliente frontend. Incluye información
 * del paciemte básica, el médico asignado y su historial de episodios clínicos.
 * 
 * Utiliza las anotaciones de Lombok para generar automáticamente:
 * - @Data: Getters, setters, toString(), equals() y hashCode()
 * - @NoArgsConstructor: Constructor sin argumentos
 * - @AllArgsConstructor: Constructor con todos los argumentos
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see com.empresa.server.model.Paciente
 * @see com.empresa.server.service.PacienteService
 * @see EpisodioDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    /**
     * Identificador único del paciente.
     */
    private Long id;
    
    /**
     * Nombre del paciente.
     */
    private String nombre;
    
    /**
     * Apellido del paciente.
     */
    private String apellido;
    
    /**
     * Número de Seguridad Social del paciente.
     * Identificador administrativo y de salud del paciente.
     */
    private String nss;
    
    /**
     * Identificador del médico asignado al paciente.
     * Puede ser null si el paciente es "huérfano" (sin médico asignado).
     */
    private Long medicoId;
    
    /**
     * Nombre completo del médico asignado al paciente.
     * Se utiliza para mostrar en la interfaz el nombre del profesional responsable.
     */
    private String nombreMedico;
    
    /**
     * Lista de episodios clínicos del paciente.
     * Contiene el historial médico del paciente con diagnósticos y tratamientos.
     */
    private List<EpisodioDTO> episodios;
}