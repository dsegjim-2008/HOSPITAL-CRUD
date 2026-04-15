package com.empresa.server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Entidad que representa un paciente en el sistema hospitalario.
 * 
 * Un paciente es una persona que requiere atención médica en el hospital. Cada paciente
 * tiene información personal (nombre, apellido, NSS), está asignado a un médico específico
 * y puede tener múltiples citas médicas y episodios clínicos registrados.
 * 
 * Esta entidad se mapea a la tabla "pacientes" en la base de datos y mantiene relaciones
 * ManyToOne con Médico y OneToMany con Episodio y Cita para gestionar su historial médico.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    /**
     * Identificador único del paciente.
     * Se genera automáticamente por la base de datos usando estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * Identificador único para fines administrativos y de salud.
     */
    private String nss;

    /**
     * Lista de episodios clínicos del paciente.
     * Relación OneToMany: un paciente puede tener múltiples episodios.
     * Cascade: eliminar el paciente elimina todos sus episodios.
     */
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Episodio> episodios;

    /**
     * Referencia a la entidad Médico asignada a este paciente.
     * Relación ManyToOne: un médico puede atender múltiples pacientes.
     * FetchType.LAZY: se carga bajo demanda para optimizar consultas.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    /**
     * Lista de citas médicas del paciente.
     * Relación OneToMany: un paciente puede tener múltiples citas.
     * Cascade: eliminar el paciente elimina todas sus citas.
     */
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Cita> citas;   
}