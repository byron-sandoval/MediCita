package com.medicita.app.web.rest;

import static com.medicita.app.domain.AdjuntoAsserts.*;
import static com.medicita.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicita.app.IntegrationTest;
import com.medicita.app.domain.Adjunto;
import com.medicita.app.repository.AdjuntoRepository;
import com.medicita.app.service.dto.AdjuntoDTO;
import com.medicita.app.service.mapper.AdjuntoMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AdjuntoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdjuntoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENIDO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENIDO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENIDO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENIDO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TIPO_CONTENIDO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_CONTENIDO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_SUBIDA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_SUBIDA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/adjuntos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdjuntoRepository adjuntoRepository;

    @Autowired
    private AdjuntoMapper adjuntoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdjuntoMockMvc;

    private Adjunto adjunto;

    private Adjunto insertedAdjunto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adjunto createEntity() {
        return new Adjunto()
            .nombre(DEFAULT_NOMBRE)
            .contenido(DEFAULT_CONTENIDO)
            .contenidoContentType(DEFAULT_CONTENIDO_CONTENT_TYPE)
            .tipoContenido(DEFAULT_TIPO_CONTENIDO)
            .fechaSubida(DEFAULT_FECHA_SUBIDA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adjunto createUpdatedEntity() {
        return new Adjunto()
            .nombre(UPDATED_NOMBRE)
            .contenido(UPDATED_CONTENIDO)
            .contenidoContentType(UPDATED_CONTENIDO_CONTENT_TYPE)
            .tipoContenido(UPDATED_TIPO_CONTENIDO)
            .fechaSubida(UPDATED_FECHA_SUBIDA);
    }

    @BeforeEach
    void initTest() {
        adjunto = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAdjunto != null) {
            adjuntoRepository.delete(insertedAdjunto);
            insertedAdjunto = null;
        }
    }

    @Test
    @Transactional
    void createAdjunto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Adjunto
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);
        var returnedAdjuntoDTO = om.readValue(
            restAdjuntoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adjuntoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdjuntoDTO.class
        );

        // Validate the Adjunto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdjunto = adjuntoMapper.toEntity(returnedAdjuntoDTO);
        assertAdjuntoUpdatableFieldsEquals(returnedAdjunto, getPersistedAdjunto(returnedAdjunto));

        insertedAdjunto = returnedAdjunto;
    }

    @Test
    @Transactional
    void createAdjuntoWithExistingId() throws Exception {
        // Create the Adjunto with an existing ID
        adjunto.setId(1L);
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdjuntoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adjuntoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Adjunto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adjunto.setNombre(null);

        // Create the Adjunto, which fails.
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);

        restAdjuntoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adjuntoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdjuntos() throws Exception {
        // Initialize the database
        insertedAdjunto = adjuntoRepository.saveAndFlush(adjunto);

        // Get all the adjuntoList
        restAdjuntoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adjunto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].contenidoContentType").value(hasItem(DEFAULT_CONTENIDO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contenido").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENIDO))))
            .andExpect(jsonPath("$.[*].tipoContenido").value(hasItem(DEFAULT_TIPO_CONTENIDO)))
            .andExpect(jsonPath("$.[*].fechaSubida").value(hasItem(DEFAULT_FECHA_SUBIDA.toString())));
    }

    @Test
    @Transactional
    void getAdjunto() throws Exception {
        // Initialize the database
        insertedAdjunto = adjuntoRepository.saveAndFlush(adjunto);

        // Get the adjunto
        restAdjuntoMockMvc
            .perform(get(ENTITY_API_URL_ID, adjunto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adjunto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.contenidoContentType").value(DEFAULT_CONTENIDO_CONTENT_TYPE))
            .andExpect(jsonPath("$.contenido").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENIDO)))
            .andExpect(jsonPath("$.tipoContenido").value(DEFAULT_TIPO_CONTENIDO))
            .andExpect(jsonPath("$.fechaSubida").value(DEFAULT_FECHA_SUBIDA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAdjunto() throws Exception {
        // Get the adjunto
        restAdjuntoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdjunto() throws Exception {
        // Initialize the database
        insertedAdjunto = adjuntoRepository.saveAndFlush(adjunto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adjunto
        Adjunto updatedAdjunto = adjuntoRepository.findById(adjunto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdjunto are not directly saved in db
        em.detach(updatedAdjunto);
        updatedAdjunto
            .nombre(UPDATED_NOMBRE)
            .contenido(UPDATED_CONTENIDO)
            .contenidoContentType(UPDATED_CONTENIDO_CONTENT_TYPE)
            .tipoContenido(UPDATED_TIPO_CONTENIDO)
            .fechaSubida(UPDATED_FECHA_SUBIDA);
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(updatedAdjunto);

        restAdjuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adjuntoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adjuntoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Adjunto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdjuntoToMatchAllProperties(updatedAdjunto);
    }

    @Test
    @Transactional
    void putNonExistingAdjunto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adjunto.setId(longCount.incrementAndGet());

        // Create the Adjunto
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdjuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adjuntoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adjuntoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adjunto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdjunto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adjunto.setId(longCount.incrementAndGet());

        // Create the Adjunto
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdjuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adjuntoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adjunto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdjunto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adjunto.setId(longCount.incrementAndGet());

        // Create the Adjunto
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdjuntoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adjuntoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adjunto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdjuntoWithPatch() throws Exception {
        // Initialize the database
        insertedAdjunto = adjuntoRepository.saveAndFlush(adjunto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adjunto using partial update
        Adjunto partialUpdatedAdjunto = new Adjunto();
        partialUpdatedAdjunto.setId(adjunto.getId());

        partialUpdatedAdjunto.tipoContenido(UPDATED_TIPO_CONTENIDO);

        restAdjuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdjunto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdjunto))
            )
            .andExpect(status().isOk());

        // Validate the Adjunto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdjuntoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAdjunto, adjunto), getPersistedAdjunto(adjunto));
    }

    @Test
    @Transactional
    void fullUpdateAdjuntoWithPatch() throws Exception {
        // Initialize the database
        insertedAdjunto = adjuntoRepository.saveAndFlush(adjunto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adjunto using partial update
        Adjunto partialUpdatedAdjunto = new Adjunto();
        partialUpdatedAdjunto.setId(adjunto.getId());

        partialUpdatedAdjunto
            .nombre(UPDATED_NOMBRE)
            .contenido(UPDATED_CONTENIDO)
            .contenidoContentType(UPDATED_CONTENIDO_CONTENT_TYPE)
            .tipoContenido(UPDATED_TIPO_CONTENIDO)
            .fechaSubida(UPDATED_FECHA_SUBIDA);

        restAdjuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdjunto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdjunto))
            )
            .andExpect(status().isOk());

        // Validate the Adjunto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdjuntoUpdatableFieldsEquals(partialUpdatedAdjunto, getPersistedAdjunto(partialUpdatedAdjunto));
    }

    @Test
    @Transactional
    void patchNonExistingAdjunto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adjunto.setId(longCount.incrementAndGet());

        // Create the Adjunto
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdjuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adjuntoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adjuntoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adjunto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdjunto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adjunto.setId(longCount.incrementAndGet());

        // Create the Adjunto
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdjuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adjuntoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adjunto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdjunto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adjunto.setId(longCount.incrementAndGet());

        // Create the Adjunto
        AdjuntoDTO adjuntoDTO = adjuntoMapper.toDto(adjunto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdjuntoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(adjuntoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adjunto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdjunto() throws Exception {
        // Initialize the database
        insertedAdjunto = adjuntoRepository.saveAndFlush(adjunto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the adjunto
        restAdjuntoMockMvc
            .perform(delete(ENTITY_API_URL_ID, adjunto.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return adjuntoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Adjunto getPersistedAdjunto(Adjunto adjunto) {
        return adjuntoRepository.findById(adjunto.getId()).orElseThrow();
    }

    protected void assertPersistedAdjuntoToMatchAllProperties(Adjunto expectedAdjunto) {
        assertAdjuntoAllPropertiesEquals(expectedAdjunto, getPersistedAdjunto(expectedAdjunto));
    }

    protected void assertPersistedAdjuntoToMatchUpdatableProperties(Adjunto expectedAdjunto) {
        assertAdjuntoAllUpdatablePropertiesEquals(expectedAdjunto, getPersistedAdjunto(expectedAdjunto));
    }
}
