package com.empresa.server.dto;

import com.empresa.server.model.Role;

// Un "record" en Java 21 es una clase inmutable perfecta para DTOs.
// No necesita @Data de Lombok, ya incluye getters automáticamente.
public record UserDTO(
    Long id,
    String email,
    String primerNombre,
    String segundoNombre,
    String primerApellido,
    String segundoApellido,
    Role rol
) {}