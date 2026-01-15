package com.medicita.app.repository;

import com.medicita.app.domain.ContenidoWeb;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContenidoWeb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContenidoWebRepository extends JpaRepository<ContenidoWeb, Long> {}
