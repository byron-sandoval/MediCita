package com.medicita.app.web.rest;

import static com.medicita.app.domain.MedicoAsserts.*;
import static com.medicita.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.medicita.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicita.app.IntegrationTest;
import com.medicita.app.domain.Medico;
import com.medicita.app.domain.enumeration.Especialidad;
import com.medicita.app.repository.MedicoRepository;
import com.medicita.app.service.dto.MedicoDTO;
import com.medicita.app.service.mapper.MedicoMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link MedicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicoResourceIT {

    private static final String DEFAULT_NUMERO_LICENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_LICENCIA = "BBBBBBBBBB";

    private static final Especialidad DEFAULT_ESPECIALIDAD = Especialidad.CARDIOLOGIA;
    private static final Especialidad UPDATED_ESPECIALIDAD = Especialidad.DERMATOLOGIA;

    private static final BigDecimal DEFAULT_TARIFA_CONSULTA = new BigDecimal(0);
    private static final BigDecimal UPDATED_TARIFA_CONSULTA = new BigDecimal(1);

    private static final String DEFAULT_KEYCLOAK_ID = "AAAAAAAAAA";
    private static final String UPDATED_KEYCLOAK_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private MedicoMapper medicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicoMockMvc;

    private Medico medico;

    private Medico insertedMedico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createEntity() {
        return new Medico()
            .numeroLicencia(DEFAULT_NUMERO_LICENCIA)
            .especialidad(DEFAULT_ESPECIALIDAD)
            .tarifaConsulta(DEFAULT_TARIFA_CONSULTA)
            .keycloakId(DEFAULT_KEYCLOAK_ID)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createUpdatedEntity() {
        return new Medico()
            .numeroLicencia(UPDATED_NUMERO_LICENCIA)
            .especialidad(UPDATED_ESPECIALIDAD)
            .tarifaConsulta(UPDATED_TARIFA_CONSULTA)
            .keycloakId(UPDATED_KEYCLOAK_ID)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        medico = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMedico != null) {
            medicoRepository.delete(insertedMedico);
            insertedMedico = null;
        }
    }

    @Test
    @Transactional
    void createMedico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);
        var returnedMedicoDTO = om.readValue(
            restMedicoMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicoDTO.class
        );

        // Validate the Medico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedico = medicoMapper.toEntity(returnedMedicoDTO);
        assertMedicoUpdatableFieldsEquals(returnedMedico, getPersistedMedico(returnedMedico));

        insertedMedico = returnedMedico;
    }

    @Test
    @Transactional
    void createMedicoWithExistingId() throws Exception {
        // Create the Medico with an existing ID
        medico.setId(1L);
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroLicenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setNumeroLicencia(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEspecialidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setEspecialidad(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTarifaConsultaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setTarifaConsulta(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKeycloakIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setKeycloakId(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setActivo(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicos() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medico.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroLicencia").value(hasItem(DEFAULT_NUMERO_LICENCIA)))
            .andExpect(jsonPath("$.[*].especialidad").value(hasItem(DEFAULT_ESPECIALIDAD.toString())))
            .andExpect(jsonPath("$.[*].tarifaConsulta").value(hasItem(sameNumber(DEFAULT_TARIFA_CONSULTA))))
            .andExpect(jsonPath("$.[*].keycloakId").value(hasItem(DEFAULT_KEYCLOAK_ID)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get the medico
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL_ID, medico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medico.getId().intValue()))
            .andExpect(jsonPath("$.numeroLicencia").value(DEFAULT_NUMERO_LICENCIA))
            .andExpect(jsonPath("$.especialidad").value(DEFAULT_ESPECIALIDAD.toString()))
            .andExpect(jsonPath("$.tarifaConsulta").value(sameNumber(DEFAULT_TARIFA_CONSULTA)))
            .andExpect(jsonPath("$.keycloakId").value(DEFAULT_KEYCLOAK_ID))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getNonExistingMedico() throws Exception {
        // Get the medico
        restMedicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico
        Medico updatedMedico = medicoRepository.findById(medico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedico are not directly saved in db
        em.detach(updatedMedico);
        updatedMedico
            .numeroLicencia(UPDATED_NUMERO_LICENCIA)
            .especialidad(UPDATED_ESPECIALIDAD)
            .tarifaConsulta(UPDATED_TARIFA_CONSULTA)
            .keycloakId(UPDATED_KEYCLOAK_ID)
            .activo(UPDATED_ACTIVO);
        MedicoDTO medicoDTO = medicoMapper.toDto(updatedMedico);

        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicoToMatchAllProperties(updatedMedico);
    }

    @Test
    @Transactional
    void putNonExistingMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico.tarifaConsulta(UPDATED_TARIFA_CONSULTA);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMedico, medico), getPersistedMedico(medico));
    }

    @Test
    @Transactional
    void fullUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico
            .numeroLicencia(UPDATED_NUMERO_LICENCIA)
            .especialidad(UPDATED_ESPECIALIDAD)
            .tarifaConsulta(UPDATED_TARIFA_CONSULTA)
            .keycloakId(UPDATED_KEYCLOAK_ID)
            .activo(UPDATED_ACTIVO);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicoUpdatableFieldsEquals(partialUpdatedMedico, getPersistedMedico(partialUpdatedMedico));
    }

    @Test
    @Transactional
    void patchNonExistingMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medico
        restMedicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medico.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicoRepository.count();
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

    protected Medico getPersistedMedico(Medico medico) {
        return medicoRepository.findById(medico.getId()).orElseThrow();
    }

    protected void assertPersistedMedicoToMatchAllProperties(Medico expectedMedico) {
        assertMedicoAllPropertiesEquals(expectedMedico, getPersistedMedico(expectedMedico));
    }

    protected void assertPersistedMedicoToMatchUpdatableProperties(Medico expectedMedico) {
        assertMedicoAllUpdatablePropertiesEquals(expectedMedico, getPersistedMedico(expectedMedico));
    }
}
