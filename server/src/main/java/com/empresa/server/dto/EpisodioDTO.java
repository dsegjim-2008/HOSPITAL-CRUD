package com.empresa.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para transferir información de episodios clínicos entre capas.
 * 
 * Este DTO es utilizado para transferir datos de episodios clínicos desde la capa de servicio
 * hacia los controladores REST y posteriormente al cliente frontend. Contiene el registro
 * de un evento médico con su diagnóstico y tratamiento.
 * 
 * Utiliza las anotaciones de Lombok para generar automáticamente:
 * - @Data: Getters, setters, toString(), equals() y hashCode()
 * - @NoArgsConstructor: Constructor sin argumentos
 * - @AllArgsConstructor: Constructor con todos los argumentos
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see com.empresa.server.model.Episodio
 * @see com.empresa.server.controller.EpisodioController
 * @see PacienteDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodioDTO {
    /**
     * Identificador único del episodio.
     */
    private Long id;
    
    /**
     * Fecha en la que ocurrió el evento clínico.
     */
    private LocalDate fecha;
    
    /**
     * Diagnóstico médico establecido en este episodio.
     * Describe las condiciones clínicas identificadas.
     */
    private String diagnostico;
    
    /**
     * Tratamiento prescrito para este episodio.
     * Describe las acciones médicas, medicamentos y recomendaciones.
     */
    private String tratamiento;
}