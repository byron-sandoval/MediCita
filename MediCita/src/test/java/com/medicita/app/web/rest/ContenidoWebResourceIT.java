package com.medicita.app.web.rest;

import static com.medicita.app.domain.ContenidoWebAsserts.*;
import static com.medicita.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicita.app.IntegrationTest;
import com.medicita.app.domain.ContenidoWeb;
import com.medicita.app.repository.ContenidoWebRepository;
import com.medicita.app.service.dto.ContenidoWebDTO;
import com.medicita.app.service.mapper.ContenidoWebMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ContenidoWebResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContenidoWebResourceIT {

    private static final String DEFAULT_CLAVE = "AAAAAAAAAA";
    private static final String UPDATED_CLAVE = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR_TEXTO = "AAAAAAAAAA";
    private static final String UPDATED_VALOR_TEXTO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/contenido-webs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContenidoWebRepository contenidoWebRepository;

    @Autowired
    private ContenidoWebMapper contenidoWebMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContenidoWebMockMvc;

    private ContenidoWeb contenidoWeb;

    private ContenidoWeb insertedContenidoWeb;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContenidoWeb createEntity() {
        return new ContenidoWeb()
            .clave(DEFAULT_CLAVE)
            .valorTexto(DEFAULT_VALOR_TEXTO)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContenidoWeb createUpdatedEntity() {
        return new ContenidoWeb()
            .clave(UPDATED_CLAVE)
            .valorTexto(UPDATED_VALOR_TEXTO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        contenidoWeb = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContenidoWeb != null) {
            contenidoWebRepository.delete(insertedContenidoWeb);
            insertedContenidoWeb = null;
        }
    }

    @Test
    @Transactional
    void createContenidoWeb() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContenidoWeb
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);
        var returnedContenidoWebDTO = om.readValue(
            restContenidoWebMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenidoWebDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContenidoWebDTO.class
        );

        // Validate the ContenidoWeb in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContenidoWeb = contenidoWebMapper.toEntity(returnedContenidoWebDTO);
        assertContenidoWebUpdatableFieldsEquals(returnedContenidoWeb, getPersistedContenidoWeb(returnedContenidoWeb));

        insertedContenidoWeb = returnedContenidoWeb;
    }

    @Test
    @Transactional
    void createContenidoWebWithExistingId() throws Exception {
        // Create the ContenidoWeb with an existing ID
        contenidoWeb.setId(1L);
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContenidoWebMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenidoWeb in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClaveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contenidoWeb.setClave(null);

        // Create the ContenidoWeb, which fails.
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        restContenidoWebMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contenidoWeb.setActivo(null);

        // Create the ContenidoWeb, which fails.
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        restContenidoWebMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContenidoWebs() throws Exception {
        // Initialize the database
        insertedContenidoWeb = contenidoWebRepository.saveAndFlush(contenidoWeb);

        // Get all the contenidoWebList
        restContenidoWebMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contenidoWeb.getId().intValue())))
            .andExpect(jsonPath("$.[*].clave").value(hasItem(DEFAULT_CLAVE)))
            .andExpect(jsonPath("$.[*].valorTexto").value(hasItem(DEFAULT_VALOR_TEXTO)))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getContenidoWeb() throws Exception {
        // Initialize the database
        insertedContenidoWeb = contenidoWebRepository.saveAndFlush(contenidoWeb);

        // Get the contenidoWeb
        restContenidoWebMockMvc
            .perform(get(ENTITY_API_URL_ID, contenidoWeb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contenidoWeb.getId().intValue()))
            .andExpect(jsonPath("$.clave").value(DEFAULT_CLAVE))
            .andExpect(jsonPath("$.valorTexto").value(DEFAULT_VALOR_TEXTO))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64.getEncoder().encodeToString(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getNonExistingContenidoWeb() throws Exception {
        // Get the contenidoWeb
        restContenidoWebMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContenidoWeb() throws Exception {
        // Initialize the database
        insertedContenidoWeb = contenidoWebRepository.saveAndFlush(contenidoWeb);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contenidoWeb
        ContenidoWeb updatedContenidoWeb = contenidoWebRepository.findById(contenidoWeb.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContenidoWeb are not directly saved in db
        em.detach(updatedContenidoWeb);
        updatedContenidoWeb
            .clave(UPDATED_CLAVE)
            .valorTexto(UPDATED_VALOR_TEXTO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .activo(UPDATED_ACTIVO);
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(updatedContenidoWeb);

        restContenidoWebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contenidoWebDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContenidoWeb in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContenidoWebToMatchAllProperties(updatedContenidoWeb);
    }

    @Test
    @Transactional
    void putNonExistingContenidoWeb() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenidoWeb.setId(longCount.incrementAndGet());

        // Create the ContenidoWeb
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContenidoWebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contenidoWebDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenidoWeb in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContenidoWeb() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenidoWeb.setId(longCount.incrementAndGet());

        // Create the ContenidoWeb
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenidoWebMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenidoWeb in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContenidoWeb() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenidoWeb.setId(longCount.incrementAndGet());

        // Create the ContenidoWeb
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenidoWebMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContenidoWeb in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContenidoWebWithPatch() throws Exception {
        // Initialize the database
        insertedContenidoWeb = contenidoWebRepository.saveAndFlush(contenidoWeb);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contenidoWeb using partial update
        ContenidoWeb partialUpdatedContenidoWeb = new ContenidoWeb();
        partialUpdatedContenidoWeb.setId(contenidoWeb.getId());

        partialUpdatedContenidoWeb
            .valorTexto(UPDATED_VALOR_TEXTO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .activo(UPDATED_ACTIVO);

        restContenidoWebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContenidoWeb.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContenidoWeb))
            )
            .andExpect(status().isOk());

        // Validate the ContenidoWeb in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContenidoWebUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContenidoWeb, contenidoWeb),
            getPersistedContenidoWeb(contenidoWeb)
        );
    }

    @Test
    @Transactional
    void fullUpdateContenidoWebWithPatch() throws Exception {
        // Initialize the database
        insertedContenidoWeb = contenidoWebRepository.saveAndFlush(contenidoWeb);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contenidoWeb using partial update
        ContenidoWeb partialUpdatedContenidoWeb = new ContenidoWeb();
        partialUpdatedContenidoWeb.setId(contenidoWeb.getId());

        partialUpdatedContenidoWeb
            .clave(UPDATED_CLAVE)
            .valorTexto(UPDATED_VALOR_TEXTO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .activo(UPDATED_ACTIVO);

        restContenidoWebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContenidoWeb.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContenidoWeb))
            )
            .andExpect(status().isOk());

        // Validate the ContenidoWeb in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContenidoWebUpdatableFieldsEquals(partialUpdatedContenidoWeb, getPersistedContenidoWeb(partialUpdatedContenidoWeb));
    }

    @Test
    @Transactional
    void patchNonExistingContenidoWeb() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenidoWeb.setId(longCount.incrementAndGet());

        // Create the ContenidoWeb
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContenidoWebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contenidoWebDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenidoWeb in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContenidoWeb() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenidoWeb.setId(longCount.incrementAndGet());

        // Create the ContenidoWeb
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenidoWebMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenidoWeb in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContenidoWeb() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenidoWeb.setId(longCount.incrementAndGet());

        // Create the ContenidoWeb
        ContenidoWebDTO contenidoWebDTO = contenidoWebMapper.toDto(contenidoWeb);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenidoWebMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contenidoWebDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContenidoWeb in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContenidoWeb() throws Exception {
        // Initialize the database
        insertedContenidoWeb = contenidoWebRepository.saveAndFlush(contenidoWeb);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contenidoWeb
        restContenidoWebMockMvc
            .perform(delete(ENTITY_API_URL_ID, contenidoWeb.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contenidoWebRepository.count();
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

    protected ContenidoWeb getPersistedContenidoWeb(ContenidoWeb contenidoWeb) {
        return contenidoWebRepository.findById(contenidoWeb.getId()).orElseThrow();
    }

    protected void assertPersistedContenidoWebToMatchAllProperties(ContenidoWeb expectedContenidoWeb) {
        assertContenidoWebAllPropertiesEquals(expectedContenidoWeb, getPersistedContenidoWeb(expectedContenidoWeb));
    }

    protected void assertPersistedContenidoWebToMatchUpdatableProperties(ContenidoWeb expectedContenidoWeb) {
        assertContenidoWebAllUpdatablePropertiesEquals(expectedContenidoWeb, getPersistedContenidoWeb(expectedContenidoWeb));
    }
}
