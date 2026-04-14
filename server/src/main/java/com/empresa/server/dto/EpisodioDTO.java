package com.empresa.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodioDTO {
    private Long id;
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;
}