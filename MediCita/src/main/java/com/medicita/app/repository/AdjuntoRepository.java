package com.medicita.app.repository;

import com.medicita.app.domain.Adjunto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Adjunto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdjuntoRepository extends JpaRepository<Adjunto, Long> {}
