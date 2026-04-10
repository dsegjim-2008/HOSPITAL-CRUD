package com.empresa.server.service;

import com.empresa.server.dto.UserDTO;
import com.empresa.server.model.User;
import com.empresa.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // --- FUNCIÓN AUXILIAR: Transforma un User (BD) a un UserDTO (Limpio) ---
    private UserDTO convertirADTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPrimerNombre(),
                user.getSegundoNombre(),
                user.getPrimerApellido(),
                user.getSegundoApellido(),
                user.getRol()
        );
    }

    public UserDTO crearUsuario(User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        User usuarioGuardado = userRepository.save(user);
        return convertirADTO(usuarioGuardado);
    }

    public List<UserDTO> obtenerTodos() {
        return userRepository.findAll()
                .stream()
                .map(this::convertirADTO) // Convertimos la lista entera a DTOs
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> obtenerPorId(Long id) {
        return userRepository.findById(id).map(this::convertirADTO);
    }

    // Este lo dejamos devolviendo 'User' porque lo usa el controlador internamente para validar emails
    public Optional<User> obtenerPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserDTO> actualizarUsuario(Long id, User usuarioActualizado) {
        return userRepository.findById(id).map(userExistente -> {
            userExistente.setPrimerNombre(usuarioActualizado.getPrimerNombre());
            userExistente.setSegundoNombre(usuarioActualizado.getSegundoNombre());
            userExistente.setPrimerApellido(usuarioActualizado.getPrimerApellido());
            userExistente.setSegundoApellido(usuarioActualizado.getSegundoApellido());
            userExistente.setEmail(usuarioActualizado.getEmail());
            userExistente.setRol(usuarioActualizado.getRol());

            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().trim().isEmpty()) {
                userExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
            }

            User usuarioGuardado = userRepository.save(userExistente);
            return convertirADTO(usuarioGuardado);
        });
    }

    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }
}