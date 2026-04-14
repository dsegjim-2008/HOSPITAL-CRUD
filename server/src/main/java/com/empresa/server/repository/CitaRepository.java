package com.empresa.server.repository;

import com.empresa.server.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    // Para que el médico vea solo sus citas
    List<Cita> findByMedicoIdOrderByFechaHoraAsc(Long medicoId);
    
    // Para ver las citas de un paciente concreto
    List<Cita> findByPacienteId(Long pacienteId);
}