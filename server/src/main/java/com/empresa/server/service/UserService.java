package com.empresa.server.service;

import com.empresa.server.model.User;
import com.empresa.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User crearUsuario(User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        return userRepository.save(user);
    }

    public List<User> obtenerTodos() {
        return userRepository.findAll();
    }

    public Optional<User> obtenerPorId(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> obtenerPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> actualizarUsuario(Long id, User usuarioActualizado) {
        return userRepository.findById(id).map(userExistente -> {
            userExistente.setPrimerNombre(usuarioActualizado.getPrimerNombre());
            userExistente.setSegundoNombre(usuarioActualizado.getSegundoNombre());
            userExistente.setPrimerApellido(usuarioActualizado.getPrimerApellido());
            userExistente.setSegundoApellido(usuarioActualizado.getSegundoApellido());
            userExistente.setEmail(usuarioActualizado.getEmail());
            userExistente.setRol(usuarioActualizado.getRol());

            // Solo hasheamos y guardamos la contraseña si han enviado una nueva
            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().trim().isEmpty()) {
                userExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
            }

            return userRepository.save(userExistente);
        });
    }

    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }
}