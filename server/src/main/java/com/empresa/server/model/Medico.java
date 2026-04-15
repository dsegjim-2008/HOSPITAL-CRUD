package com.empresa.server.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa un médico en el sistema hospitalario.
 * 
 * Un médico es un profesional de la salud que atiende pacientes y realiza citas médicas.
 * Cada médico tiene información personal, especialidad, número de colegiado y credenciales
 * de acceso al sistema. El usuario se genera automáticamente basándose en el nombre, apellido
 * y fecha de nacimiento.
 * 
 * Esta entidad se mapea a la tabla "medicos" en la base de datos y mantiene relaciones
 * OneToMany con Paciente y Cita para registrar los pacientes asignados y las citas realizadas.
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "medicos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Medico {
    /**
     * Identificador único del médico.
     * Se genera automáticamente por la base de datos usando estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Primer nombre del médico.
     * Campo requerido, no puede ser nulo.
     */
    @Column(nullable = false)
    private String primerNombre;
    
    /**
     * Segundo nombre del médico (opcional).
     */
    private String segundoNombre;
    
    /**
     * Primer apellido del médico.
     * Campo requerido, no puede ser nulo.
     */
    @Column(nullable = false)
    private String primerApellido;
    
    /**
     * Segundo apellido del médico (opcional).
     */
    private String segundoApellido;

    /**
     * Especialidad médica del profesional (ej: Cardiología, Pediatría, etc.).
     */
    private String especialidad;
    
    /**
     * Número de colegiación profesional del médico.
     * Identifica al médico ante el colegio de médicos.
     */
    private String numeroColegiado;
    
    /**
     * Fecha de nacimiento del médico.
     * Se utiliza en la generación automática del usuario.
     */
    private LocalDate fechaNacimiento;
    
    /**
     * Nombre de usuario único para acceder al sistema.
     * Se genera automáticamente con formato: primNombrePrimApellidoAño
     * Debe ser único en la base de datos.
     */
    @Column(unique = true)
    private String usuario;
    
    /**
     * Contraseña para el acceso al sistema.
     * Valor por defecto: "123"
     */
    private String password = "123";

    /**
     * Lista de pacientes asignados a este médico.
     * Relación OneToMany: un médico puede atender múltiples pacientes.
     */
    @OneToMany(mappedBy = "medico")
    private List<Paciente> pacientes;

    /**
     * Lista de citas realizadas por este médico.
     * Relación OneToMany con cascade: eliminar el médico elimina sus citas.
     */
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Cita> citas;

    /**
     * Genera automáticamente el nombre de usuario del médico antes de guardar en BD.
     * Formato: primerosTres(nombre) + primerosTres(apellido) + año de nacimiento
     * Se ejecuta automáticamente mediante el hook @PrePersist de JPA.
     */
    @PrePersist
    public void generarUsuario() {
        if (this.primerNombre != null && this.primerApellido != null && this.fechaNacimiento != null) {
            String nom = primerNombre.toLowerCase().trim();
            String ape = primerApellido.toLowerCase().trim();
            String pNom = nom.length() >= 3 ? nom.substring(0, 3) : nom;
            String pApe = ape.length() >= 3 ? ape.substring(0, 3) : ape;
            this.usuario = pNom + pApe + this.fechaNacimiento.getYear();
        }
    }

    /**
     * Obtiene el nombre completo del médico combinando todos sus nombres y apellidos.
     * 
     * @return String con el formato: primerNombre [segundoNombre] primerApellido [segundoApellido]
     */
    public String getNombreCompleto() {
        return primerNombre + (segundoNombre != null ? " " + segundoNombre : "") + 
               " " + primerApellido + (segundoApellido != null ? " " + segundoApellido : "");
    }
}