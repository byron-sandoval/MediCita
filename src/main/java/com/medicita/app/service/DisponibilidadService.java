package com.medicita.app.service;

import com.medicita.app.service.dto.DisponibilidadDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicita.app.domain.Disponibilidad}.
 */
public interface DisponibilidadService {
    /**
     * Save a disponibilidad.
     *
     * @param disponibilidadDTO the entity to save.
     * @return the persisted entity.
     */
    DisponibilidadDTO save(DisponibilidadDTO disponibilidadDTO);

    /**
     * Updates a disponibilidad.
     *
     * @param disponibilidadDTO the entity to update.
     * @return the persisted entity.
     */
    DisponibilidadDTO update(DisponibilidadDTO disponibilidadDTO);

    /**
     * Partially updates a disponibilidad.
     *
     * @param disponibilidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DisponibilidadDTO> partialUpdate(DisponibilidadDTO disponibilidadDTO);

    /**
     * Get all the disponibilidads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DisponibilidadDTO> findAll(Pageable pageable);

    /**
     * Get the "id" disponibilidad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DisponibilidadDTO> findOne(Long id);

    /**
     * Delete the "id" disponibilidad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
