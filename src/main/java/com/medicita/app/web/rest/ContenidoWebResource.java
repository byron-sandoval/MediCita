package com.medicita.app.web.rest;

import com.medicita.app.repository.ContenidoWebRepository;
import com.medicita.app.service.ContenidoWebService;
import com.medicita.app.service.dto.ContenidoWebDTO;
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
 * REST controller for managing {@link com.medicita.app.domain.ContenidoWeb}.
 */
@RestController
@RequestMapping("/api/contenido-webs")
public class ContenidoWebResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContenidoWebResource.class);

    private static final String ENTITY_NAME = "mediCitaContenidoWeb";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContenidoWebService contenidoWebService;

    private final ContenidoWebRepository contenidoWebRepository;

    public ContenidoWebResource(ContenidoWebService contenidoWebService, ContenidoWebRepository contenidoWebRepository) {
        this.contenidoWebService = contenidoWebService;
        this.contenidoWebRepository = contenidoWebRepository;
    }

    /**
     * {@code POST  /contenido-webs} : Create a new contenidoWeb.
     *
     * @param contenidoWebDTO the contenidoWebDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contenidoWebDTO, or with status {@code 400 (Bad Request)} if the contenidoWeb has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContenidoWebDTO> createContenidoWeb(@Valid @RequestBody ContenidoWebDTO contenidoWebDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ContenidoWeb : {}", contenidoWebDTO);
        if (contenidoWebDTO.getId() != null) {
            throw new BadRequestAlertException("A new contenidoWeb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contenidoWebDTO = contenidoWebService.save(contenidoWebDTO);
        return ResponseEntity.created(new URI("/api/contenido-webs/" + contenidoWebDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, contenidoWebDTO.getId().toString()))
            .body(contenidoWebDTO);
    }

    /**
     * {@code PUT  /contenido-webs/:id} : Updates an existing contenidoWeb.
     *
     * @param id the id of the contenidoWebDTO to save.
     * @param contenidoWebDTO the contenidoWebDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contenidoWebDTO,
     * or with status {@code 400 (Bad Request)} if the contenidoWebDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contenidoWebDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContenidoWebDTO> updateContenidoWeb(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContenidoWebDTO contenidoWebDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContenidoWeb : {}, {}", id, contenidoWebDTO);
        if (contenidoWebDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contenidoWebDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contenidoWebRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contenidoWebDTO = contenidoWebService.update(contenidoWebDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contenidoWebDTO.getId().toString()))
            .body(contenidoWebDTO);
    }

    /**
     * {@code PATCH  /contenido-webs/:id} : Partial updates given fields of an existing contenidoWeb, field will ignore if it is null
     *
     * @param id the id of the contenidoWebDTO to save.
     * @param contenidoWebDTO the contenidoWebDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contenidoWebDTO,
     * or with status {@code 400 (Bad Request)} if the contenidoWebDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contenidoWebDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contenidoWebDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContenidoWebDTO> partialUpdateContenidoWeb(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContenidoWebDTO contenidoWebDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContenidoWeb partially : {}, {}", id, contenidoWebDTO);
        if (contenidoWebDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contenidoWebDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contenidoWebRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContenidoWebDTO> result = contenidoWebService.partialUpdate(contenidoWebDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contenidoWebDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contenido-webs} : get all the contenidoWebs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contenidoWebs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContenidoWebDTO>> getAllContenidoWebs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ContenidoWebs");
        Page<ContenidoWebDTO> page = contenidoWebService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contenido-webs/:id} : get the "id" contenidoWeb.
     *
     * @param id the id of the contenidoWebDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contenidoWebDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContenidoWebDTO> getContenidoWeb(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContenidoWeb : {}", id);
        Optional<ContenidoWebDTO> contenidoWebDTO = contenidoWebService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contenidoWebDTO);
    }

    /**
     * {@code DELETE  /contenido-webs/:id} : delete the "id" contenidoWeb.
     *
     * @param id the id of the contenidoWebDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenidoWeb(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContenidoWeb : {}", id);
        contenidoWebService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
