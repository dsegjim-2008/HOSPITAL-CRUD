package com.empresa.server.exception;

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado en el sistema.
 *
 * Reemplaza el uso de RuntimeException genérico en la capa de servicio para
 * proporcionar mensajes de error más descriptivos y permitir un manejo
 * diferenciado en los controladores (tipicamente retornar HTTP 404).
 *
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construye la excepción con un mensaje descriptivo del recurso no encontrado.
     *
     * @param mensaje Descripción del recurso no encontrado (ej: "Paciente con ID 5 no encontrado")
     */
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
