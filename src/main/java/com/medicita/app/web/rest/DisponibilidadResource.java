package com.medicita.app.web.rest;

import com.medicita.app.repository.DisponibilidadRepository;
import com.medicita.app.service.DisponibilidadService;
import com.medicita.app.service.dto.DisponibilidadDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.medicita.app.domain.Disponibilidad}.
 */
@RestController
@RequestMapping("/api/disponibilidads")
public class DisponibilidadResource {

    private static final Logger LOG = LoggerFactory.getLogger(DisponibilidadResource.class);

    private static final String ENTITY_NAME = "mediCitaDisponibilidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisponibilidadService disponibilidadService;

    private final DisponibilidadRepository disponibilidadRepository;

    public DisponibilidadResource(DisponibilidadService disponibilidadService, DisponibilidadRepository disponibilidadRepository) {
        this.disponibilidadService = disponibilidadService;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    /**
     * {@code POST  /disponibilidads} : Create a new disponibilidad.
     *
     * @param disponibilidadDTO the disponibilidadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disponibilidadDTO, or with status {@code 400 (Bad Request)} if the disponibilidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DisponibilidadDTO> createDisponibilidad(@Valid @RequestBody DisponibilidadDTO disponibilidadDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Disponibilidad : {}", disponibilidadDTO);
        if (disponibilidadDTO.getId() != null) {
            throw new BadRequestAlertException("A new disponibilidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        disponibilidadDTO = disponibilidadService.save(disponibilidadDTO);
        return ResponseEntity.created(new URI("/api/disponibilidads/" + disponibilidadDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, disponibilidadDTO.getId().toString()))
            .body(disponibilidadDTO);
    }

    /**
     * {@code PUT  /disponibilidads/:id} : Updates an existing disponibilidad.
     *
     * @param id the id of the disponibilidadDTO to save.
     * @param disponibilidadDTO the disponibilidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibilidadDTO,
     * or with status {@code 400 (Bad Request)} if the disponibilidadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disponibilidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisponibilidadDTO> updateDisponibilidad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DisponibilidadDTO disponibilidadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Disponibilidad : {}, {}", id, disponibilidadDTO);
        if (disponibilidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibilidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibilidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        disponibilidadDTO = disponibilidadService.update(disponibilidadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disponibilidadDTO.getId().toString()))
            .body(disponibilidadDTO);
    }

    /**
     * {@code PATCH  /disponibilidads/:id} : Partial updates given fields of an existing disponibilidad, field will ignore if it is null
     *
     * @param id the id of the disponibilidadDTO to save.
     * @param disponibilidadDTO the disponibilidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibilidadDTO,
     * or with status {@code 400 (Bad Request)} if the disponibilidadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the disponibilidadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the disponibilidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisponibilidadDTO> partialUpdateDisponibilidad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DisponibilidadDTO disponibilidadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Disponibilidad partially : {}, {}", id, disponibilidadDTO);
        if (disponibilidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibilidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibilidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisponibilidadDTO> result = disponibilidadService.partialUpdate(disponibilidadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disponibilidadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /disponibilidads} : get all the disponibilidads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disponibilidads in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DisponibilidadDTO>> getAllDisponibilidads(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of Disponibilidads");
        Page<DisponibilidadDTO> page = disponibilidadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /disponibilidads/:id} : get the "id" disponibilidad.
     *
     * @param id the id of the disponibilidadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disponibilidadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadDTO> getDisponibilidad(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Disponibilidad : {}", id);
        Optional<DisponibilidadDTO> disponibilidadDTO = disponibilidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(disponibilidadDTO);
    }

    /**
     * {@code DELETE  /disponibilidads/:id} : delete the "id" disponibilidad.
     *
     * @param id the id of the disponibilidadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisponibilidad(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Disponibilidad : {}", id);
        disponibilidadService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
