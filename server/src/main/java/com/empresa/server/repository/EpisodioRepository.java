package com.empresa.server.repository;

import com.empresa.server.model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EpisodioRepository extends JpaRepository<Episodio, Long> {
    List<Episodio> findByPacienteId(Long pacienteId);
}