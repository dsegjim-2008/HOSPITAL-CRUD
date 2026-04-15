package com.empresa.server.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para transferir información de médicos entre capas.
 * 
 * Este DTO es utilizado para transferir datos de médicos desde la capa de servicio
 * hacia los controladores REST y posteriormente al cliente frontend. Contiene los
 * datos necesarios para displayar y gestionar información de profesionales médicos.
 * 
 * Utiliza la anotación @Data de Lombok que genera automáticamente:
 * - Getters y setters para todos los atributos
 * - Método toString()
 * - Método equals() y hashCode()
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see com.empresa.server.model.Medico
 * @see com.empresa.server.service.MedicoService
 */
@Data
public class MedicoDTO {
    /**
     * Identificador único del médico.
     */
    private Long id;
    
    /**
     * Primer nombre del profesional médico.
     */
    private String primerNombre;
    
    /**
     * Segundo nombre del profesional médico (opcional).
     */
    private String segundoNombre;
    
    /**
     * Primer apellido del profesional médico.
     */
    private String primerApellido;
    
    /**
     * Segundo apellido del profesional médico (opcional).
     */
    private String segundoApellido;
    
    /**
     * Nombre completo del médico (formato: Nombre(s) Apellido(s)).
     * Se genera combinando primerNombre, segundoNombre, primerApellido y segundoApellido.
     */
    private String nombreCompleto;
    
    /**
     * Especialidad médica del profesional (ej: Cardiología, Neurología, etc.).
     */
    private String especialidad;
    
    /**
     * Número de colegiación profesional.
     * Identificador único del médico ante el colegio profesional.
     */
    private String numeroColegiado;
    
    /**
     * Nombre de usuario para acceso al sistema.
     * Se genera automáticamente con formato: primNombrePrimApellidoAño
     */
    private String usuario;
    
    /**
     * Fecha de nacimiento del profesional médico.
     */
    private LocalDate fechaNacimiento;
}