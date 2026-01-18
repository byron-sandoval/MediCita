package com.medicita.app.service;

import com.medicita.app.service.dto.HistoriaClinicaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicita.app.domain.HistoriaClinica}.
 */
public interface HistoriaClinicaService {
    /**
     * Save a historiaClinica.
     *
     * @param historiaClinicaDTO the entity to save.
     * @return the persisted entity.
     */
    HistoriaClinicaDTO save(HistoriaClinicaDTO historiaClinicaDTO);

    /**
     * Updates a historiaClinica.
     *
     * @param historiaClinicaDTO the entity to update.
     * @return the persisted entity.
     */
    HistoriaClinicaDTO update(HistoriaClinicaDTO historiaClinicaDTO);

    /**
     * Partially updates a historiaClinica.
     *
     * @param historiaClinicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoriaClinicaDTO> partialUpdate(HistoriaClinicaDTO historiaClinicaDTO);

    /**
     * Get all the historiaClinicas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistoriaClinicaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" historiaClinica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriaClinicaDTO> findOne(Long id);

    /**
     * Delete the "id" historiaClinica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
