package com.empresa.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para transferir información de citas médicas entre capas.
 * 
 * Este DTO es utilizado para transferir datos de citas desde la capa de servicio
 * hacia los controladores REST y posteriormente al cliente frontend. Contiene
 * información de la cita así como los datos del médico y paciente involucrados.
 * 
 * Utiliza las anotaciones de Lombok para generar automáticamente:
 * - @Data: Getters, setters, toString(), equals() y hashCode()
 * - @NoArgsConstructor: Constructor sin argumentos
 * - @AllArgsConstructor: Constructor con todos los argumentos
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see com.empresa.server.model.Cita
 * @see com.empresa.server.controller.CitaController
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {
    /**
     * Identificador único de la cita.
     */
    private Long id;
    
    /**
     * Fecha y hora en la que se realiza o se programó la cita médica.
     */
    private LocalDateTime fechaHora;
    
    /**
     * Motivo de la consulta (ej: "Revisión general", "Seguimiento", "Diagnóstico").
     */
    private String motivo;
    
    /**
     * Número o identificación de la sala donde se realiza la cita.
     */
    private String sala;
    
    /**
     * Identificador del médico que atiende la cita.
     */
    private Long medicoId;
    
    /**
     * Nombre completo del médico que atiende la cita.
     */
    private String nombreMedico;
    
    /**
     * Identificador del paciente que tiene la cita.
     */
    private Long pacienteId;
    
    /**
     * Nombre completo del paciente que tiene la cita.
     */
    private String nombrePaciente;
}