package com.empresa.server.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "medicos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String primerNombre;
    private String segundoNombre;
    @Column(nullable = false)
    private String primerApellido;
    private String segundoApellido;

    private String especialidad;
    private String numeroColegiado;
    private LocalDate fechaNacimiento;
    
    @Column(unique = true)
    private String usuario;
    private String password = "123";

    @OneToMany(mappedBy = "medico")
    private List<Paciente> pacientes;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Cita> citas;

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

    public String getNombreCompleto() {
        return primerNombre + (segundoNombre != null ? " " + segundoNombre : "") + 
               " " + primerApellido + (segundoApellido != null ? " " + segundoApellido : "");
    }
}