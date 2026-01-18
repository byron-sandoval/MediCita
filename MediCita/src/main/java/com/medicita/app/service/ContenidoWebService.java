package com.medicita.app.service;

import com.medicita.app.service.dto.ContenidoWebDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicita.app.domain.ContenidoWeb}.
 */
public interface ContenidoWebService {
    /**
     * Save a contenidoWeb.
     *
     * @param contenidoWebDTO the entity to save.
     * @return the persisted entity.
     */
    ContenidoWebDTO save(ContenidoWebDTO contenidoWebDTO);

    /**
     * Updates a contenidoWeb.
     *
     * @param contenidoWebDTO the entity to update.
     * @return the persisted entity.
     */
    ContenidoWebDTO update(ContenidoWebDTO contenidoWebDTO);

    /**
     * Partially updates a contenidoWeb.
     *
     * @param contenidoWebDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContenidoWebDTO> partialUpdate(ContenidoWebDTO contenidoWebDTO);

    /**
     * Get all the contenidoWebs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContenidoWebDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contenidoWeb.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContenidoWebDTO> findOne(Long id);

    /**
     * Delete the "id" contenidoWeb.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
