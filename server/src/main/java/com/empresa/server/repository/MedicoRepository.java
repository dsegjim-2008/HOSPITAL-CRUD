package com.empresa.server.repository;

import com.empresa.server.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // <-- ¡Esta es la línea mágica que faltaba!

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByNumeroColegiado(String numeroColegiado);
}