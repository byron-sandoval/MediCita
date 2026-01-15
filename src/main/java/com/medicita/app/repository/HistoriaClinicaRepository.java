package com.medicita.app.repository;

import com.medicita.app.domain.HistoriaClinica;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoriaClinica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {}
