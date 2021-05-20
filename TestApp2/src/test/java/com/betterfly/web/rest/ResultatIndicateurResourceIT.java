package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.ResultatIndicateur;
import com.betterfly.domain.enumeration.Mois;
import com.betterfly.repository.ResultatIndicateurRepository;
import com.betterfly.repository.search.ResultatIndicateurSearchRepository;
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
 * Integration tests for the {@link ResultatIndicateurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ResultatIndicateurResourceIT {

    private static final Mois DEFAULT_MOIS = Mois.JAN;
    private static final Mois UPDATED_MOIS = Mois.FEV;

    private static final Float DEFAULT_CIBLE = 1F;
    private static final Float UPDATED_CIBLE = 2F;

    private static final Float DEFAULT_RESULTAT = 1F;
    private static final Float UPDATED_RESULTAT = 2F;

    private static final String ENTITY_API_URL = "/api/resultat-indicateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/resultat-indicateurs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResultatIndicateurRepository resultatIndicateurRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.ResultatIndicateurSearchRepositoryMockConfiguration
     */
    @Autowired
    private ResultatIndicateurSearchRepository mockResultatIndicateurSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultatIndicateurMockMvc;

    private ResultatIndicateur resultatIndicateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultatIndicateur createEntity(EntityManager em) {
        ResultatIndicateur resultatIndicateur = new ResultatIndicateur().mois(DEFAULT_MOIS).cible(DEFAULT_CIBLE).resultat(DEFAULT_RESULTAT);
        return resultatIndicateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultatIndicateur createUpdatedEntity(EntityManager em) {
        ResultatIndicateur resultatIndicateur = new ResultatIndicateur().mois(UPDATED_MOIS).cible(UPDATED_CIBLE).resultat(UPDATED_RESULTAT);
        return resultatIndicateur;
    }

    @BeforeEach
    public void initTest() {
        resultatIndicateur = createEntity(em);
    }

    @Test
    @Transactional
    void createResultatIndicateur() throws Exception {
        int databaseSizeBeforeCreate = resultatIndicateurRepository.findAll().size();
        // Create the ResultatIndicateur
        restResultatIndicateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultatIndicateur))
            )
            .andExpect(status().isCreated());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeCreate + 1);
        ResultatIndicateur testResultatIndicateur = resultatIndicateurList.get(resultatIndicateurList.size() - 1);
        assertThat(testResultatIndicateur.getMois()).isEqualTo(DEFAULT_MOIS);
        assertThat(testResultatIndicateur.getCible()).isEqualTo(DEFAULT_CIBLE);
        assertThat(testResultatIndicateur.getResultat()).isEqualTo(DEFAULT_RESULTAT);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(1)).save(testResultatIndicateur);
    }

    @Test
    @Transactional
    void createResultatIndicateurWithExistingId() throws Exception {
        // Create the ResultatIndicateur with an existing ID
        resultatIndicateur.setId(1L);

        int databaseSizeBeforeCreate = resultatIndicateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultatIndicateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultatIndicateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeCreate);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(0)).save(resultatIndicateur);
    }

    @Test
    @Transactional
    void getAllResultatIndicateurs() throws Exception {
        // Initialize the database
        resultatIndicateurRepository.saveAndFlush(resultatIndicateur);

        // Get all the resultatIndicateurList
        restResultatIndicateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultatIndicateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].mois").value(hasItem(DEFAULT_MOIS.toString())))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].resultat").value(hasItem(DEFAULT_RESULTAT.doubleValue())));
    }

    @Test
    @Transactional
    void getResultatIndicateur() throws Exception {
        // Initialize the database
        resultatIndicateurRepository.saveAndFlush(resultatIndicateur);

        // Get the resultatIndicateur
        restResultatIndicateurMockMvc
            .perform(get(ENTITY_API_URL_ID, resultatIndicateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultatIndicateur.getId().intValue()))
            .andExpect(jsonPath("$.mois").value(DEFAULT_MOIS.toString()))
            .andExpect(jsonPath("$.cible").value(DEFAULT_CIBLE.doubleValue()))
            .andExpect(jsonPath("$.resultat").value(DEFAULT_RESULTAT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingResultatIndicateur() throws Exception {
        // Get the resultatIndicateur
        restResultatIndicateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResultatIndicateur() throws Exception {
        // Initialize the database
        resultatIndicateurRepository.saveAndFlush(resultatIndicateur);

        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();

        // Update the resultatIndicateur
        ResultatIndicateur updatedResultatIndicateur = resultatIndicateurRepository.findById(resultatIndicateur.getId()).get();
        // Disconnect from session so that the updates on updatedResultatIndicateur are not directly saved in db
        em.detach(updatedResultatIndicateur);
        updatedResultatIndicateur.mois(UPDATED_MOIS).cible(UPDATED_CIBLE).resultat(UPDATED_RESULTAT);

        restResultatIndicateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResultatIndicateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResultatIndicateur))
            )
            .andExpect(status().isOk());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);
        ResultatIndicateur testResultatIndicateur = resultatIndicateurList.get(resultatIndicateurList.size() - 1);
        assertThat(testResultatIndicateur.getMois()).isEqualTo(UPDATED_MOIS);
        assertThat(testResultatIndicateur.getCible()).isEqualTo(UPDATED_CIBLE);
        assertThat(testResultatIndicateur.getResultat()).isEqualTo(UPDATED_RESULTAT);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository).save(testResultatIndicateur);
    }

    @Test
    @Transactional
    void putNonExistingResultatIndicateur() throws Exception {
        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();
        resultatIndicateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatIndicateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultatIndicateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultatIndicateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(0)).save(resultatIndicateur);
    }

    @Test
    @Transactional
    void putWithIdMismatchResultatIndicateur() throws Exception {
        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();
        resultatIndicateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatIndicateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultatIndicateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(0)).save(resultatIndicateur);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResultatIndicateur() throws Exception {
        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();
        resultatIndicateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatIndicateurMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultatIndicateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(0)).save(resultatIndicateur);
    }

    @Test
    @Transactional
    void partialUpdateResultatIndicateurWithPatch() throws Exception {
        // Initialize the database
        resultatIndicateurRepository.saveAndFlush(resultatIndicateur);

        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();

        // Update the resultatIndicateur using partial update
        ResultatIndicateur partialUpdatedResultatIndicateur = new ResultatIndicateur();
        partialUpdatedResultatIndicateur.setId(resultatIndicateur.getId());

        partialUpdatedResultatIndicateur.mois(UPDATED_MOIS).resultat(UPDATED_RESULTAT);

        restResultatIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultatIndicateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultatIndicateur))
            )
            .andExpect(status().isOk());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);
        ResultatIndicateur testResultatIndicateur = resultatIndicateurList.get(resultatIndicateurList.size() - 1);
        assertThat(testResultatIndicateur.getMois()).isEqualTo(UPDATED_MOIS);
        assertThat(testResultatIndicateur.getCible()).isEqualTo(DEFAULT_CIBLE);
        assertThat(testResultatIndicateur.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void fullUpdateResultatIndicateurWithPatch() throws Exception {
        // Initialize the database
        resultatIndicateurRepository.saveAndFlush(resultatIndicateur);

        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();

        // Update the resultatIndicateur using partial update
        ResultatIndicateur partialUpdatedResultatIndicateur = new ResultatIndicateur();
        partialUpdatedResultatIndicateur.setId(resultatIndicateur.getId());

        partialUpdatedResultatIndicateur.mois(UPDATED_MOIS).cible(UPDATED_CIBLE).resultat(UPDATED_RESULTAT);

        restResultatIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultatIndicateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultatIndicateur))
            )
            .andExpect(status().isOk());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);
        ResultatIndicateur testResultatIndicateur = resultatIndicateurList.get(resultatIndicateurList.size() - 1);
        assertThat(testResultatIndicateur.getMois()).isEqualTo(UPDATED_MOIS);
        assertThat(testResultatIndicateur.getCible()).isEqualTo(UPDATED_CIBLE);
        assertThat(testResultatIndicateur.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void patchNonExistingResultatIndicateur() throws Exception {
        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();
        resultatIndicateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultatIndicateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultatIndicateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(0)).save(resultatIndicateur);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResultatIndicateur() throws Exception {
        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();
        resultatIndicateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultatIndicateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(0)).save(resultatIndicateur);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResultatIndicateur() throws Exception {
        int databaseSizeBeforeUpdate = resultatIndicateurRepository.findAll().size();
        resultatIndicateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultatIndicateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultatIndicateur in the database
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(0)).save(resultatIndicateur);
    }

    @Test
    @Transactional
    void deleteResultatIndicateur() throws Exception {
        // Initialize the database
        resultatIndicateurRepository.saveAndFlush(resultatIndicateur);

        int databaseSizeBeforeDelete = resultatIndicateurRepository.findAll().size();

        // Delete the resultatIndicateur
        restResultatIndicateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultatIndicateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResultatIndicateur> resultatIndicateurList = resultatIndicateurRepository.findAll();
        assertThat(resultatIndicateurList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ResultatIndicateur in Elasticsearch
        verify(mockResultatIndicateurSearchRepository, times(1)).deleteById(resultatIndicateur.getId());
    }

    @Test
    @Transactional
    void searchResultatIndicateur() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        resultatIndicateurRepository.saveAndFlush(resultatIndicateur);
        when(mockResultatIndicateurSearchRepository.search(queryStringQuery("id:" + resultatIndicateur.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(resultatIndicateur), PageRequest.of(0, 1), 1));

        // Search the resultatIndicateur
        restResultatIndicateurMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + resultatIndicateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultatIndicateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].mois").value(hasItem(DEFAULT_MOIS.toString())))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].resultat").value(hasItem(DEFAULT_RESULTAT.doubleValue())));
    }
}
