package com.medicita.app.service;

import com.medicita.app.service.dto.AdjuntoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicita.app.domain.Adjunto}.
 */
public interface AdjuntoService {
    /**
     * Save a adjunto.
     *
     * @param adjuntoDTO the entity to save.
     * @return the persisted entity.
     */
    AdjuntoDTO save(AdjuntoDTO adjuntoDTO);

    /**
     * Updates a adjunto.
     *
     * @param adjuntoDTO the entity to update.
     * @return the persisted entity.
     */
    AdjuntoDTO update(AdjuntoDTO adjuntoDTO);

    /**
     * Partially updates a adjunto.
     *
     * @param adjuntoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdjuntoDTO> partialUpdate(AdjuntoDTO adjuntoDTO);

    /**
     * Get all the adjuntos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdjuntoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" adjunto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdjuntoDTO> findOne(Long id);

    /**
     * Delete the "id" adjunto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
