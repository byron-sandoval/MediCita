package com.medicita.app.web.rest;

import static com.medicita.app.domain.PacienteAsserts.*;
import static com.medicita.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicita.app.IntegrationTest;
import com.medicita.app.domain.Paciente;
import com.medicita.app.domain.enumeration.Genero;
import com.medicita.app.repository.PacienteRepository;
import com.medicita.app.service.dto.PacienteDTO;
import com.medicita.app.service.mapper.PacienteMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_CEDULA = "AAAAAAAAAA";
    private static final String UPDATED_CEDULA = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Genero DEFAULT_GENERO = Genero.MASCULINO;
    private static final Genero UPDATED_GENERO = Genero.FEMENINO;

    private static final String DEFAULT_KEYCLOAK_ID = "AAAAAAAAAA";
    private static final String UPDATED_KEYCLOAK_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    private Paciente insertedPaciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity() {
        return new Paciente()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .cedula(DEFAULT_CEDULA)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .genero(DEFAULT_GENERO)
            .keycloakId(DEFAULT_KEYCLOAK_ID)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity() {
        return new Paciente()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .cedula(UPDATED_CEDULA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .genero(UPDATED_GENERO)
            .keycloakId(UPDATED_KEYCLOAK_ID)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        paciente = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaciente != null) {
            pacienteRepository.delete(insertedPaciente);
            insertedPaciente = null;
        }
    }

    @Test
    @Transactional
    void createPaciente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);
        var returnedPacienteDTO = om.readValue(
            restPacienteMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PacienteDTO.class
        );

        // Validate the Paciente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaciente = pacienteMapper.toEntity(returnedPacienteDTO);
        assertPacienteUpdatableFieldsEquals(returnedPaciente, getPersistedPaciente(returnedPaciente));

        insertedPaciente = returnedPaciente;
    }

    @Test
    @Transactional
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setNombre(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setApellido(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCedulaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setCedula(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setEmail(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setTelefono(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setFechaNacimiento(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKeycloakIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setKeycloakId(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setActivo(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPacientes() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())))
            .andExpect(jsonPath("$.[*].keycloakId").value(hasItem(DEFAULT_KEYCLOAK_ID)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.cedula").value(DEFAULT_CEDULA))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()))
            .andExpect(jsonPath("$.keycloakId").value(DEFAULT_KEYCLOAK_ID))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .cedula(UPDATED_CEDULA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .genero(UPDATED_GENERO)
            .keycloakId(UPDATED_KEYCLOAK_ID)
            .activo(UPDATED_ACTIVO);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(updatedPaciente);

        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteToMatchAllProperties(updatedPaciente);
    }

    @Test
    @Transactional
    void putNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.cedula(UPDATED_CEDULA).fechaNacimiento(UPDATED_FECHA_NACIMIENTO).activo(UPDATED_ACTIVO);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaciente, paciente), getPersistedPaciente(paciente));
    }

    @Test
    @Transactional
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .cedula(UPDATED_CEDULA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .genero(UPDATED_GENERO)
            .keycloakId(UPDATED_KEYCLOAK_ID)
            .activo(UPDATED_ACTIVO);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(partialUpdatedPaciente, getPersistedPaciente(partialUpdatedPaciente));
    }

    @Test
    @Transactional
    void patchNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paciente
        restPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paciente.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteRepository.count();
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

    protected Paciente getPersistedPaciente(Paciente paciente) {
        return pacienteRepository.findById(paciente.getId()).orElseThrow();
    }

    protected void assertPersistedPacienteToMatchAllProperties(Paciente expectedPaciente) {
        assertPacienteAllPropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }

    protected void assertPersistedPacienteToMatchUpdatableProperties(Paciente expectedPaciente) {
        assertPacienteAllUpdatablePropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }
}
