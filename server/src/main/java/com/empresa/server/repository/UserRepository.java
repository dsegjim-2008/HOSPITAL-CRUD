package com.empresa.server.repository;

import com.empresa.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Ahora el email será nuestro único método de búsqueda para el login
    Optional<User> findByEmail(String email);
    
}