package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.ResultIndicateurs;
import com.betterfly.repository.ResultIndicateursRepository;
import com.betterfly.repository.search.ResultIndicateursSearchRepository;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ResultIndicateursResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ResultIndicateursResourceIT {

    private static final Integer DEFAULT_ANNEE = 1;
    private static final Integer UPDATED_ANNEE = 2;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/result-indicateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/result-indicateurs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResultIndicateursRepository resultIndicateursRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.ResultIndicateursSearchRepositoryMockConfiguration
     */
    @Autowired
    private ResultIndicateursSearchRepository mockResultIndicateursSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultIndicateursMockMvc;

    private ResultIndicateurs resultIndicateurs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultIndicateurs createEntity(EntityManager em) {
        ResultIndicateurs resultIndicateurs = new ResultIndicateurs().annee(DEFAULT_ANNEE).observation(DEFAULT_OBSERVATION);
        return resultIndicateurs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultIndicateurs createUpdatedEntity(EntityManager em) {
        ResultIndicateurs resultIndicateurs = new ResultIndicateurs().annee(UPDATED_ANNEE).observation(UPDATED_OBSERVATION);
        return resultIndicateurs;
    }

    @BeforeEach
    public void initTest() {
        resultIndicateurs = createEntity(em);
    }

    @Test
    @Transactional
    void createResultIndicateurs() throws Exception {
        int databaseSizeBeforeCreate = resultIndicateursRepository.findAll().size();
        // Create the ResultIndicateurs
        restResultIndicateursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultIndicateurs))
            )
            .andExpect(status().isCreated());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeCreate + 1);
        ResultIndicateurs testResultIndicateurs = resultIndicateursList.get(resultIndicateursList.size() - 1);
        assertThat(testResultIndicateurs.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testResultIndicateurs.getObservation()).isEqualTo(DEFAULT_OBSERVATION);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(1)).save(testResultIndicateurs);
    }

    @Test
    @Transactional
    void createResultIndicateursWithExistingId() throws Exception {
        // Create the ResultIndicateurs with an existing ID
        resultIndicateurs.setId(1L);

        int databaseSizeBeforeCreate = resultIndicateursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultIndicateursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultIndicateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeCreate);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(0)).save(resultIndicateurs);
    }

    @Test
    @Transactional
    void getAllResultIndicateurs() throws Exception {
        // Initialize the database
        resultIndicateursRepository.saveAndFlush(resultIndicateurs);

        // Get all the resultIndicateursList
        restResultIndicateursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultIndicateurs.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }

    @Test
    @Transactional
    void getResultIndicateurs() throws Exception {
        // Initialize the database
        resultIndicateursRepository.saveAndFlush(resultIndicateurs);

        // Get the resultIndicateurs
        restResultIndicateursMockMvc
            .perform(get(ENTITY_API_URL_ID, resultIndicateurs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultIndicateurs.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getNonExistingResultIndicateurs() throws Exception {
        // Get the resultIndicateurs
        restResultIndicateursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResultIndicateurs() throws Exception {
        // Initialize the database
        resultIndicateursRepository.saveAndFlush(resultIndicateurs);

        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();

        // Update the resultIndicateurs
        ResultIndicateurs updatedResultIndicateurs = resultIndicateursRepository.findById(resultIndicateurs.getId()).get();
        // Disconnect from session so that the updates on updatedResultIndicateurs are not directly saved in db
        em.detach(updatedResultIndicateurs);
        updatedResultIndicateurs.annee(UPDATED_ANNEE).observation(UPDATED_OBSERVATION);

        restResultIndicateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResultIndicateurs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResultIndicateurs))
            )
            .andExpect(status().isOk());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);
        ResultIndicateurs testResultIndicateurs = resultIndicateursList.get(resultIndicateursList.size() - 1);
        assertThat(testResultIndicateurs.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testResultIndicateurs.getObservation()).isEqualTo(UPDATED_OBSERVATION);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository).save(testResultIndicateurs);
    }

    @Test
    @Transactional
    void putNonExistingResultIndicateurs() throws Exception {
        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();
        resultIndicateurs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultIndicateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultIndicateurs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultIndicateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(0)).save(resultIndicateurs);
    }

    @Test
    @Transactional
    void putWithIdMismatchResultIndicateurs() throws Exception {
        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();
        resultIndicateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultIndicateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultIndicateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(0)).save(resultIndicateurs);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResultIndicateurs() throws Exception {
        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();
        resultIndicateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultIndicateursMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultIndicateurs))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(0)).save(resultIndicateurs);
    }

    @Test
    @Transactional
    void partialUpdateResultIndicateursWithPatch() throws Exception {
        // Initialize the database
        resultIndicateursRepository.saveAndFlush(resultIndicateurs);

        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();

        // Update the resultIndicateurs using partial update
        ResultIndicateurs partialUpdatedResultIndicateurs = new ResultIndicateurs();
        partialUpdatedResultIndicateurs.setId(resultIndicateurs.getId());

        restResultIndicateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultIndicateurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultIndicateurs))
            )
            .andExpect(status().isOk());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);
        ResultIndicateurs testResultIndicateurs = resultIndicateursList.get(resultIndicateursList.size() - 1);
        assertThat(testResultIndicateurs.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testResultIndicateurs.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdateResultIndicateursWithPatch() throws Exception {
        // Initialize the database
        resultIndicateursRepository.saveAndFlush(resultIndicateurs);

        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();

        // Update the resultIndicateurs using partial update
        ResultIndicateurs partialUpdatedResultIndicateurs = new ResultIndicateurs();
        partialUpdatedResultIndicateurs.setId(resultIndicateurs.getId());

        partialUpdatedResultIndicateurs.annee(UPDATED_ANNEE).observation(UPDATED_OBSERVATION);

        restResultIndicateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultIndicateurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultIndicateurs))
            )
            .andExpect(status().isOk());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);
        ResultIndicateurs testResultIndicateurs = resultIndicateursList.get(resultIndicateursList.size() - 1);
        assertThat(testResultIndicateurs.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testResultIndicateurs.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingResultIndicateurs() throws Exception {
        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();
        resultIndicateurs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultIndicateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultIndicateurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultIndicateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(0)).save(resultIndicateurs);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResultIndicateurs() throws Exception {
        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();
        resultIndicateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultIndicateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultIndicateurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(0)).save(resultIndicateurs);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResultIndicateurs() throws Exception {
        int databaseSizeBeforeUpdate = resultIndicateursRepository.findAll().size();
        resultIndicateurs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultIndicateursMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultIndicateurs))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultIndicateurs in the database
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(0)).save(resultIndicateurs);
    }

    @Test
    @Transactional
    void deleteResultIndicateurs() throws Exception {
        // Initialize the database
        resultIndicateursRepository.saveAndFlush(resultIndicateurs);

        int databaseSizeBeforeDelete = resultIndicateursRepository.findAll().size();

        // Delete the resultIndicateurs
        restResultIndicateursMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultIndicateurs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResultIndicateurs> resultIndicateursList = resultIndicateursRepository.findAll();
        assertThat(resultIndicateursList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ResultIndicateurs in Elasticsearch
        verify(mockResultIndicateursSearchRepository, times(1)).deleteById(resultIndicateurs.getId());
    }

    @Test
    @Transactional
    void searchResultIndicateurs() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        resultIndicateursRepository.saveAndFlush(resultIndicateurs);
        when(mockResultIndicateursSearchRepository.search(queryStringQuery("id:" + resultIndicateurs.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(resultIndicateurs), PageRequest.of(0, 1), 1));

        // Search the resultIndicateurs
        restResultIndicateursMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + resultIndicateurs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultIndicateurs.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
}
