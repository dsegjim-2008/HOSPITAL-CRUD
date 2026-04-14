package com.empresa.server.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MedicoDTO {
    private Long id;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String nombreCompleto;
    private String especialidad;
    private String numeroColegiado; // <-- IMPORTANTE
    private String usuario;
    private LocalDate fechaNacimiento;
}