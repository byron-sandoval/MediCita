package com.medicita.app.web.rest;

import static com.medicita.app.domain.DisponibilidadAsserts.*;
import static com.medicita.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicita.app.IntegrationTest;
import com.medicita.app.domain.Disponibilidad;
import com.medicita.app.domain.enumeration.DiaSemana;
import com.medicita.app.repository.DisponibilidadRepository;
import com.medicita.app.service.dto.DisponibilidadDTO;
import com.medicita.app.service.mapper.DisponibilidadMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalTime;
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
 * Integration tests for the {@link DisponibilidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisponibilidadResourceIT {

    private static final DiaSemana DEFAULT_DIA = DiaSemana.LUNES;
    private static final DiaSemana UPDATED_DIA = DiaSemana.MARTES;

    private static final LocalTime DEFAULT_HORA_INICIO = LocalTime.NOON;
    private static final LocalTime UPDATED_HORA_INICIO = LocalTime.MAX.withNano(0);

    private static final LocalTime DEFAULT_HORA_FIN = LocalTime.NOON;
    private static final LocalTime UPDATED_HORA_FIN = LocalTime.MAX.withNano(0);

    private static final Boolean DEFAULT_ES_DESCANSO = false;
    private static final Boolean UPDATED_ES_DESCANSO = true;

    private static final String ENTITY_API_URL = "/api/disponibilidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private DisponibilidadMapper disponibilidadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisponibilidadMockMvc;

    private Disponibilidad disponibilidad;

    private Disponibilidad insertedDisponibilidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disponibilidad createEntity() {
        return new Disponibilidad()
            .dia(DEFAULT_DIA)
            .horaInicio(DEFAULT_HORA_INICIO)
            .horaFin(DEFAULT_HORA_FIN)
            .esDescanso(DEFAULT_ES_DESCANSO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disponibilidad createUpdatedEntity() {
        return new Disponibilidad()
            .dia(UPDATED_DIA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN)
            .esDescanso(UPDATED_ES_DESCANSO);
    }

    @BeforeEach
    void initTest() {
        disponibilidad = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDisponibilidad != null) {
            disponibilidadRepository.delete(insertedDisponibilidad);
            insertedDisponibilidad = null;
        }
    }

    @Test
    @Transactional
    void createDisponibilidad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Disponibilidad
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);
        var returnedDisponibilidadDTO = om.readValue(
            restDisponibilidadMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(disponibilidadDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DisponibilidadDTO.class
        );

        // Validate the Disponibilidad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDisponibilidad = disponibilidadMapper.toEntity(returnedDisponibilidadDTO);
        assertDisponibilidadUpdatableFieldsEquals(returnedDisponibilidad, getPersistedDisponibilidad(returnedDisponibilidad));

        insertedDisponibilidad = returnedDisponibilidad;
    }

    @Test
    @Transactional
    void createDisponibilidadWithExistingId() throws Exception {
        // Create the Disponibilidad with an existing ID
        disponibilidad.setId(1L);
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisponibilidadMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilidad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDiaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disponibilidad.setDia(null);

        // Create the Disponibilidad, which fails.
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        restDisponibilidadMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoraInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disponibilidad.setHoraInicio(null);

        // Create the Disponibilidad, which fails.
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        restDisponibilidadMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoraFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disponibilidad.setHoraFin(null);

        // Create the Disponibilidad, which fails.
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        restDisponibilidadMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDisponibilidads() throws Exception {
        // Initialize the database
        insertedDisponibilidad = disponibilidadRepository.saveAndFlush(disponibilidad);

        // Get all the disponibilidadList
        restDisponibilidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA.toString())))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].horaFin").value(hasItem(DEFAULT_HORA_FIN.toString())))
            .andExpect(jsonPath("$.[*].esDescanso").value(hasItem(DEFAULT_ES_DESCANSO)));
    }

    @Test
    @Transactional
    void getDisponibilidad() throws Exception {
        // Initialize the database
        insertedDisponibilidad = disponibilidadRepository.saveAndFlush(disponibilidad);

        // Get the disponibilidad
        restDisponibilidadMockMvc
            .perform(get(ENTITY_API_URL_ID, disponibilidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disponibilidad.getId().intValue()))
            .andExpect(jsonPath("$.dia").value(DEFAULT_DIA.toString()))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.horaFin").value(DEFAULT_HORA_FIN.toString()))
            .andExpect(jsonPath("$.esDescanso").value(DEFAULT_ES_DESCANSO));
    }

    @Test
    @Transactional
    void getNonExistingDisponibilidad() throws Exception {
        // Get the disponibilidad
        restDisponibilidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisponibilidad() throws Exception {
        // Initialize the database
        insertedDisponibilidad = disponibilidadRepository.saveAndFlush(disponibilidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilidad
        Disponibilidad updatedDisponibilidad = disponibilidadRepository.findById(disponibilidad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisponibilidad are not directly saved in db
        em.detach(updatedDisponibilidad);
        updatedDisponibilidad.dia(UPDATED_DIA).horaInicio(UPDATED_HORA_INICIO).horaFin(UPDATED_HORA_FIN).esDescanso(UPDATED_ES_DESCANSO);
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(updatedDisponibilidad);

        restDisponibilidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disponibilidadDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDisponibilidadToMatchAllProperties(updatedDisponibilidad);
    }

    @Test
    @Transactional
    void putNonExistingDisponibilidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilidad.setId(longCount.incrementAndGet());

        // Create the Disponibilidad
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibilidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disponibilidadDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisponibilidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilidad.setId(longCount.incrementAndGet());

        // Create the Disponibilidad
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisponibilidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilidad.setId(longCount.incrementAndGet());

        // Create the Disponibilidad
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disponibilidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisponibilidadWithPatch() throws Exception {
        // Initialize the database
        insertedDisponibilidad = disponibilidadRepository.saveAndFlush(disponibilidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilidad using partial update
        Disponibilidad partialUpdatedDisponibilidad = new Disponibilidad();
        partialUpdatedDisponibilidad.setId(disponibilidad.getId());

        partialUpdatedDisponibilidad.horaFin(UPDATED_HORA_FIN);

        restDisponibilidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilidad.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisponibilidad))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilidad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisponibilidadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDisponibilidad, disponibilidad),
            getPersistedDisponibilidad(disponibilidad)
        );
    }

    @Test
    @Transactional
    void fullUpdateDisponibilidadWithPatch() throws Exception {
        // Initialize the database
        insertedDisponibilidad = disponibilidadRepository.saveAndFlush(disponibilidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilidad using partial update
        Disponibilidad partialUpdatedDisponibilidad = new Disponibilidad();
        partialUpdatedDisponibilidad.setId(disponibilidad.getId());

        partialUpdatedDisponibilidad
            .dia(UPDATED_DIA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN)
            .esDescanso(UPDATED_ES_DESCANSO);

        restDisponibilidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilidad.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisponibilidad))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilidad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisponibilidadUpdatableFieldsEquals(partialUpdatedDisponibilidad, getPersistedDisponibilidad(partialUpdatedDisponibilidad));
    }

    @Test
    @Transactional
    void patchNonExistingDisponibilidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilidad.setId(longCount.incrementAndGet());

        // Create the Disponibilidad
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibilidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disponibilidadDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisponibilidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilidad.setId(longCount.incrementAndGet());

        // Create the Disponibilidad
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisponibilidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilidad.setId(longCount.incrementAndGet());

        // Create the Disponibilidad
        DisponibilidadDTO disponibilidadDTO = disponibilidadMapper.toDto(disponibilidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibilidadMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disponibilidadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disponibilidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisponibilidad() throws Exception {
        // Initialize the database
        insertedDisponibilidad = disponibilidadRepository.saveAndFlush(disponibilidad);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the disponibilidad
        restDisponibilidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, disponibilidad.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return disponibilidadRepository.count();
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

    protected Disponibilidad getPersistedDisponibilidad(Disponibilidad disponibilidad) {
        return disponibilidadRepository.findById(disponibilidad.getId()).orElseThrow();
    }

    protected void assertPersistedDisponibilidadToMatchAllProperties(Disponibilidad expectedDisponibilidad) {
        assertDisponibilidadAllPropertiesEquals(expectedDisponibilidad, getPersistedDisponibilidad(expectedDisponibilidad));
    }

    protected void assertPersistedDisponibilidadToMatchUpdatableProperties(Disponibilidad expectedDisponibilidad) {
        assertDisponibilidadAllUpdatablePropertiesEquals(expectedDisponibilidad, getPersistedDisponibilidad(expectedDisponibilidad));
    }
}
