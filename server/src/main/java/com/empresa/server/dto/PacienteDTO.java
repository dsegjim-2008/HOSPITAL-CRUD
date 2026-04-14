package com.empresa.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String nss;
    private Long medicoId;
    private String nombreMedico;
    
    // Lista de Episodios DTO
    private List<EpisodioDTO> episodios; 
}