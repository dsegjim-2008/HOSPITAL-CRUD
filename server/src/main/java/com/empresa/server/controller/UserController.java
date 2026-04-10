package com.empresa.server.controller;

import com.empresa.server.dto.UserDTO; // ¡Aquí está la importación que faltaba!
import com.empresa.server.model.User;
import com.empresa.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    // --- MÉTODOS AUXILIARES ---

    private Map<String, Object> crearRespuestaError(String mensaje, List<String> detalles) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", mensaje);
        if (detalles != null && !detalles.isEmpty()) {
            respuesta.put("detalles", detalles);
        }
        return respuesta;
    }

    private List<String> validarDatosObligatorios(User user, boolean esActualizacion) {
        List<String> errores = new ArrayList<>();
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) errores.add("El email es obligatorio.");
        if (user.getPrimerNombre() == null || user.getPrimerNombre().trim().isEmpty()) errores.add("El primer nombre es obligatorio.");
        if (user.getPrimerApellido() == null || user.getPrimerApellido().trim().isEmpty()) errores.add("El primer apellido es obligatorio.");
        if (user.getRol() == null) errores.add("El rol es obligatorio.");
        
        if (!esActualizacion && (user.getPassword() == null || user.getPassword().trim().isEmpty())) {
            errores.add("La contraseña es obligatoria para nuevos usuarios.");
        }
        
        return errores;
    }

    // --- ENDPOINTS CRUD ---

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody User user) {
        try {
            List<String> erroresValidacion = validarDatosObligatorios(user, false);
            if (!erroresValidacion.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(crearRespuestaError("Faltan datos obligatorios o son inválidos.", erroresValidacion));
            }

            if (userService.obtenerPorEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(crearRespuestaError("El email '" + user.getEmail() + "' ya está registrado.", null));
            }

            // AHORA RECIBIMOS UN UserDTO
            UserDTO nuevoUsuario = userService.crearUsuario(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno al crear el usuario.", List.of(e.getMessage())));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            // AHORA RECIBIMOS UNA LISTA DE UserDTO
            List<UserDTO> usuarios = userService.obtenerTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno al obtener la lista de usuarios.", List.of(e.getMessage())));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            // AHORA RECIBIMOS UN Optional<UserDTO>
            Optional<UserDTO> user = userService.obtenerPorId(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(crearRespuestaError("No se encontró ningún usuario con el ID: " + id, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno al buscar el usuario.", List.of(e.getMessage())));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody User user) {
        try {
            List<String> erroresValidacion = validarDatosObligatorios(user, true);
            if (!erroresValidacion.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(crearRespuestaError("Faltan datos obligatorios o son inválidos.", erroresValidacion));
            }

            Optional<User> usuarioConEseEmail = userService.obtenerPorEmail(user.getEmail());
            if (usuarioConEseEmail.isPresent() && !usuarioConEseEmail.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(crearRespuestaError("El email '" + user.getEmail() + "' ya está en uso por otro usuario.", null));
            }

            // AHORA RECIBIMOS UN Optional<UserDTO>
            Optional<UserDTO> usuarioActualizado = userService.actualizarUsuario(id, user);
            if (usuarioActualizado.isPresent()) {
                return ResponseEntity.ok(usuarioActualizado.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(crearRespuestaError("No se puede actualizar. El usuario con ID " + id + " no existe.", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno al actualizar el usuario.", List.of(e.getMessage())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (userService.obtenerPorId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(crearRespuestaError("No se puede eliminar. El usuario con ID " + id + " no existe.", null));
            }
            
            userService.eliminarUsuario(id);
            
            Map<String, String> exito = new HashMap<>();
            exito.put("mensaje", "Usuario eliminado correctamente.");
            return ResponseEntity.ok(exito);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno al eliminar el usuario.", List.of(e.getMessage())));
        }
    }
}