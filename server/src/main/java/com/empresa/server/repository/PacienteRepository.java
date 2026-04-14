package com.empresa.server.repository;

import com.empresa.server.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByMedicoId(Long medicoId);
    
    // NUEVO: Busca todos los pacientes donde la columna medico_id sea NULL
    List<Paciente> findByMedicoIsNull();
}