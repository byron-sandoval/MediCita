package com.medicita.app.service;

import com.medicita.app.service.dto.PacienteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicita.app.domain.Paciente}.
 */
public interface PacienteService {
    /**
     * Save a paciente.
     *
     * @param pacienteDTO the entity to save.
     * @return the persisted entity.
     */
    PacienteDTO save(PacienteDTO pacienteDTO);

    /**
     * Updates a paciente.
     *
     * @param pacienteDTO the entity to update.
     * @return the persisted entity.
     */
    PacienteDTO update(PacienteDTO pacienteDTO);

    /**
     * Partially updates a paciente.
     *
     * @param pacienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO);

    /**
     * Get all the pacientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PacienteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paciente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PacienteDTO> findOne(Long id);

    /**
     * Get the paciente by keycloakId.
     *
     * @param keycloakId the keycloakId of the entity.
     * @return the entity.
     */
    Optional<PacienteDTO> findOneByKeycloakId(String keycloakId);

    /**
     * Delete the "id" paciente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
