package com.empresa.server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/**
 * Entidad que representa un episodio clínico en el historial del paciente.
 * 
 * Un episodio es un evento médico específico que ocurre en una fecha determinada,
 * registrando el diagnóstico y el tratamiento aplicado. Cada episodio está asociado
 * a un paciente específico y forma parte de su historial médico.
 * 
 * Esta entidad se mapea a la tabla "episodios" en la base de datos y utiliza una
 * relación ManyToOne con la entidad Paciente para mantener el registro clínico.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "episodios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Episodio {
    /**
     * Identificador único del episodio.
     * Se genera automáticamente por la base de datos usando estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha en la que ocurrió el episodio clínico.
     */
    private LocalDate fecha;
    
    /**
     * Diagnóstico médico establecido en este episodio.
     * Describe las condiciones clínicas identificadas en el paciente.
     */
    private String diagnostico;
    
    /**
     * Tratamiento aplicado para este episodio.
     * Describe las acciones médicas y medicamentos prescritos.
     */
    private String tratamiento;

    /**
     * Referencia a la entidad Paciente asociada a este episodio.
     * Relación ManyToOne: un paciente puede tener múltiples episodios.
     * FetchType.LAZY: se carga bajo demanda para optimizar consultas.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}