package com.medicita.app.repository;

import com.medicita.app.domain.Disponibilidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Disponibilidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {}
