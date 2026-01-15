package com.medicita.app.web.rest;

import static com.medicita.app.domain.HistoriaClinicaAsserts.*;
import static com.medicita.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicita.app.IntegrationTest;
import com.medicita.app.domain.HistoriaClinica;
import com.medicita.app.repository.HistoriaClinicaRepository;
import com.medicita.app.service.dto.HistoriaClinicaDTO;
import com.medicita.app.service.mapper.HistoriaClinicaMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link HistoriaClinicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriaClinicaResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DIAGNOSTICO = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO = "BBBBBBBBBB";

    private static final String DEFAULT_TRATAMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_TRATAMIENTO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/historia-clinicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private HistoriaClinicaMapper historiaClinicaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriaClinicaMockMvc;

    private HistoriaClinica historiaClinica;

    private HistoriaClinica insertedHistoriaClinica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriaClinica createEntity() {
        return new HistoriaClinica()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .diagnostico(DEFAULT_DIAGNOSTICO)
            .tratamiento(DEFAULT_TRATAMIENTO)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriaClinica createUpdatedEntity() {
        return new HistoriaClinica()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .diagnostico(UPDATED_DIAGNOSTICO)
            .tratamiento(UPDATED_TRATAMIENTO)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        historiaClinica = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoriaClinica != null) {
            historiaClinicaRepository.delete(insertedHistoriaClinica);
            insertedHistoriaClinica = null;
        }
    }

    @Test
    @Transactional
    void createHistoriaClinica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);
        var returnedHistoriaClinicaDTO = om.readValue(
            restHistoriaClinicaMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(historiaClinicaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoriaClinicaDTO.class
        );

        // Validate the HistoriaClinica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoriaClinica = historiaClinicaMapper.toEntity(returnedHistoriaClinicaDTO);
        assertHistoriaClinicaUpdatableFieldsEquals(returnedHistoriaClinica, getPersistedHistoriaClinica(returnedHistoriaClinica));

        insertedHistoriaClinica = returnedHistoriaClinica;
    }

    @Test
    @Transactional
    void createHistoriaClinicaWithExistingId() throws Exception {
        // Create the HistoriaClinica with an existing ID
        historiaClinica.setId(1L);
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaCreacionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiaClinica.setFechaCreacion(null);

        // Create the HistoriaClinica, which fails.
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        restHistoriaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiagnosticoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiaClinica.setDiagnostico(null);

        // Create the HistoriaClinica, which fails.
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        restHistoriaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiaClinica.setActivo(null);

        // Create the HistoriaClinica, which fails.
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        restHistoriaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicas() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiaClinica.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].diagnostico").value(hasItem(DEFAULT_DIAGNOSTICO)))
            .andExpect(jsonPath("$.[*].tratamiento").value(hasItem(DEFAULT_TRATAMIENTO)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getHistoriaClinica() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get the historiaClinica
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL_ID, historiaClinica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiaClinica.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.diagnostico").value(DEFAULT_DIAGNOSTICO))
            .andExpect(jsonPath("$.tratamiento").value(DEFAULT_TRATAMIENTO))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getNonExistingHistoriaClinica() throws Exception {
        // Get the historiaClinica
        restHistoriaClinicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriaClinica() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiaClinica
        HistoriaClinica updatedHistoriaClinica = historiaClinicaRepository.findById(historiaClinica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistoriaClinica are not directly saved in db
        em.detach(updatedHistoriaClinica);
        updatedHistoriaClinica
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .diagnostico(UPDATED_DIAGNOSTICO)
            .tratamiento(UPDATED_TRATAMIENTO)
            .activo(UPDATED_ACTIVO);
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(updatedHistoriaClinica);

        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiaClinicaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoriaClinicaToMatchAllProperties(updatedHistoriaClinica);
    }

    @Test
    @Transactional
    void putNonExistingHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiaClinicaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriaClinicaWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiaClinica using partial update
        HistoriaClinica partialUpdatedHistoriaClinica = new HistoriaClinica();
        partialUpdatedHistoriaClinica.setId(historiaClinica.getId());

        partialUpdatedHistoriaClinica.diagnostico(UPDATED_DIAGNOSTICO);

        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriaClinica.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriaClinica))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriaClinicaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoriaClinica, historiaClinica),
            getPersistedHistoriaClinica(historiaClinica)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoriaClinicaWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiaClinica using partial update
        HistoriaClinica partialUpdatedHistoriaClinica = new HistoriaClinica();
        partialUpdatedHistoriaClinica.setId(historiaClinica.getId());

        partialUpdatedHistoriaClinica
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .diagnostico(UPDATED_DIAGNOSTICO)
            .tratamiento(UPDATED_TRATAMIENTO)
            .activo(UPDATED_ACTIVO);

        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriaClinica.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriaClinica))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriaClinicaUpdatableFieldsEquals(
            partialUpdatedHistoriaClinica,
            getPersistedHistoriaClinica(partialUpdatedHistoriaClinica)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiaClinicaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriaClinica() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historiaClinica
        restHistoriaClinicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiaClinica.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historiaClinicaRepository.count();
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

    protected HistoriaClinica getPersistedHistoriaClinica(HistoriaClinica historiaClinica) {
        return historiaClinicaRepository.findById(historiaClinica.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriaClinicaToMatchAllProperties(HistoriaClinica expectedHistoriaClinica) {
        assertHistoriaClinicaAllPropertiesEquals(expectedHistoriaClinica, getPersistedHistoriaClinica(expectedHistoriaClinica));
    }

    protected void assertPersistedHistoriaClinicaToMatchUpdatableProperties(HistoriaClinica expectedHistoriaClinica) {
        assertHistoriaClinicaAllUpdatablePropertiesEquals(expectedHistoriaClinica, getPersistedHistoriaClinica(expectedHistoriaClinica));
    }
}
