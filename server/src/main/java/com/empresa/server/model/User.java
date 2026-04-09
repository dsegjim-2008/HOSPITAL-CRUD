package com.empresa.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios") // Cambiamos el nombre de la tabla a "usuarios" como pediste
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;

    // Al no poner nullable = false, la base de datos permite que sea nulo (Opcional)
    @Column(name = "segundo_nombre", length = 50) 
    private String segundoNombre;

    @Column(name = "primer_apellido", nullable = false, length = 50)
    private String primerApellido;

    // Opcional
    @Column(name = "segundo_apellido", length = 50) 
    private String segundoApellido;

    @Column(nullable = false)
    private String password;

    // @Enumerated(EnumType.STRING) le dice a la base de datos que guarde el texto ("ADMIN" o "USER") 
    // en lugar del número de posición del Enum (0 o 1), lo cual es mucho más seguro si el Enum cambia.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rol;
}