package com.empresa.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class CitaDTO {
    private Long id;
    private LocalDateTime fechaHora;
    private String motivo;
    private String sala;
    private Long medicoId;
    private String nombreMedico;
    private Long pacienteId;
    private String nombrePaciente;
}