package com.empresa.server.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una cita médica en el sistema hospitalario.
 * 
 * Una cita es un encuentro programado entre un médico y un paciente en una sala específica
 * en una fecha y hora determinada. Cada cita tiene asociado un motivo que describe el
 * propósito de la consulta.
 * 
 * Esta entidad se mapea a la tabla "citas" en la base de datos y utiliza relaciones
 * ManyToOne con las entidades Medico y Paciente para establecer las asociaciones
 * necesarias.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "citas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Cita {
    
    /**
     * Identificador único de la cita.
     * Se genera automáticamente por la base de datos usando estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora en la que se realizará la cita.
     * Se almacena como LocalDateTime para mantener precisión temporal.
     * 
     * Formato esperado: YYYY-MM-DD HH:mm:ss
     */
    private LocalDateTime fechaHora;
    
    /**
     * Motivo o descripción de la cita médica.
     * Describe el propósito de la consulta (ej: "Revisión general", "Seguimiento", etc.)
     */
    private String motivo;
    
    /**
     * Número o identificación de la sala donde se realizará la cita.
     * Identifica la ubicación física donde tendrá lugar la consulta.
     */
    private String sala;

    /**
     * Referencia a la entidad Médico asociada a esta cita.
     * Relación ManyToOne: varios pacientes pueden tener citas con el mismo médico.
     * FetchType.LAZY: se carga bajo demanda para optimizar consultas.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    /**
     * Referencia a la entidad Paciente asociada a esta cita.
     * Relación ManyToOne: un paciente puede tener múltiples citas.
     * FetchType.LAZY: se carga bajo demanda para optimizar consultas.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}