package com.medicita.app.repository;

import com.medicita.app.domain.Paciente;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Paciente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
