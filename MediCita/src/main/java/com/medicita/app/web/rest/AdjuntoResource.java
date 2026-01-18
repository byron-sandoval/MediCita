package com.medicita.app.web.rest;

import com.medicita.app.repository.AdjuntoRepository;
import com.medicita.app.service.AdjuntoService;
import com.medicita.app.service.dto.AdjuntoDTO;
import com.medicita.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.medicita.app.domain.Adjunto}.
 */
@RestController
@RequestMapping("/api/adjuntos")
public class AdjuntoResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdjuntoResource.class);

    private static final String ENTITY_NAME = "mediCitaAdjunto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdjuntoService adjuntoService;

    private final AdjuntoRepository adjuntoRepository;

    public AdjuntoResource(AdjuntoService adjuntoService, AdjuntoRepository adjuntoRepository) {
        this.adjuntoService = adjuntoService;
        this.adjuntoRepository = adjuntoRepository;
    }

    /**
     * {@code POST  /adjuntos} : Create a new adjunto.
     *
     * @param adjuntoDTO the adjuntoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new adjuntoDTO, or with status {@code 400 (Bad Request)} if
     *         the adjunto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_MEDICO', 'ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<AdjuntoDTO> createAdjunto(@Valid @RequestBody AdjuntoDTO adjuntoDTO)
            throws URISyntaxException {
        LOG.debug("REST request to save Adjunto : {}", adjuntoDTO);
        if (adjuntoDTO.getId() != null) {
            throw new BadRequestAlertException("A new adjunto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        adjuntoDTO = adjuntoService.save(adjuntoDTO);
        return ResponseEntity.created(new URI("/api/adjuntos/" + adjuntoDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME,
                        adjuntoDTO.getId().toString()))
                .body(adjuntoDTO);
    }

    /**
     * {@code PUT  /adjuntos/:id} : Updates an existing adjunto.
     *
     * @param id         the id of the adjuntoDTO to save.
     * @param adjuntoDTO the adjuntoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated adjuntoDTO,
     *         or with status {@code 400 (Bad Request)} if the adjuntoDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the adjuntoDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdjuntoDTO> updateAdjunto(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody AdjuntoDTO adjuntoDTO) throws URISyntaxException {
        LOG.debug("REST request to update Adjunto : {}, {}", id, adjuntoDTO);
        if (adjuntoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adjuntoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adjuntoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        adjuntoDTO = adjuntoService.update(adjuntoDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME,
                        adjuntoDTO.getId().toString()))
                .body(adjuntoDTO);
    }

    /**
     * {@code PATCH  /adjuntos/:id} : Partial updates given fields of an existing
     * adjunto, field will ignore if it is null
     *
     * @param id         the id of the adjuntoDTO to save.
     * @param adjuntoDTO the adjuntoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated adjuntoDTO,
     *         or with status {@code 400 (Bad Request)} if the adjuntoDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the adjuntoDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the adjuntoDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdjuntoDTO> partialUpdateAdjunto(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody AdjuntoDTO adjuntoDTO) throws URISyntaxException {
        LOG.debug("REST request to partial update Adjunto partially : {}, {}", id, adjuntoDTO);
        if (adjuntoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adjuntoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adjuntoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdjuntoDTO> result = adjuntoService.partialUpdate(adjuntoDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adjuntoDTO.getId().toString()));
    }

    /**
     * {@code GET  /adjuntos} : get all the adjuntos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of adjuntos in body.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MEDICO', 'ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<List<AdjuntoDTO>> getAllAdjuntos(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Adjuntos");
        Page<AdjuntoDTO> page = adjuntoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /adjuntos/:id} : get the "id" adjunto.
     *
     * @param id the id of the adjuntoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the adjuntoDTO, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MEDICO', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<AdjuntoDTO> getAdjunto(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Adjunto : {}", id);
        Optional<AdjuntoDTO> adjuntoDTO = adjuntoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adjuntoDTO);
    }

    /**
     * {@code DELETE  /adjuntos/:id} : delete the "id" adjunto.
     *
     * @param id the id of the adjuntoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdjunto(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Adjunto : {}", id);
        adjuntoService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
