package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.PolitiqueQSE;
import com.betterfly.repository.PolitiqueQSERepository;
import com.betterfly.repository.search.PolitiqueQSESearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PolitiqueQSEResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PolitiqueQSEResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_AXE_POLITIQUE_QSE = "AAAAAAAAAA";
    private static final String UPDATED_AXE_POLITIQUE_QSE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIF_QSE = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIF_QSE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VIGUEUR = false;
    private static final Boolean UPDATED_VIGUEUR = true;

    private static final String ENTITY_API_URL = "/api/politique-qses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/politique-qses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PolitiqueQSERepository politiqueQSERepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.PolitiqueQSESearchRepositoryMockConfiguration
     */
    @Autowired
    private PolitiqueQSESearchRepository mockPolitiqueQSESearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPolitiqueQSEMockMvc;

    private PolitiqueQSE politiqueQSE;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolitiqueQSE createEntity(EntityManager em) {
        PolitiqueQSE politiqueQSE = new PolitiqueQSE()
            .date(DEFAULT_DATE)
            .axePolitiqueQSE(DEFAULT_AXE_POLITIQUE_QSE)
            .objectifQSE(DEFAULT_OBJECTIF_QSE)
            .vigueur(DEFAULT_VIGUEUR);
        return politiqueQSE;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolitiqueQSE createUpdatedEntity(EntityManager em) {
        PolitiqueQSE politiqueQSE = new PolitiqueQSE()
            .date(UPDATED_DATE)
            .axePolitiqueQSE(UPDATED_AXE_POLITIQUE_QSE)
            .objectifQSE(UPDATED_OBJECTIF_QSE)
            .vigueur(UPDATED_VIGUEUR);
        return politiqueQSE;
    }

    @BeforeEach
    public void initTest() {
        politiqueQSE = createEntity(em);
    }

    @Test
    @Transactional
    void createPolitiqueQSE() throws Exception {
        int databaseSizeBeforeCreate = politiqueQSERepository.findAll().size();
        // Create the PolitiqueQSE
        restPolitiqueQSEMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(politiqueQSE)))
            .andExpect(status().isCreated());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeCreate + 1);
        PolitiqueQSE testPolitiqueQSE = politiqueQSEList.get(politiqueQSEList.size() - 1);
        assertThat(testPolitiqueQSE.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPolitiqueQSE.getAxePolitiqueQSE()).isEqualTo(DEFAULT_AXE_POLITIQUE_QSE);
        assertThat(testPolitiqueQSE.getObjectifQSE()).isEqualTo(DEFAULT_OBJECTIF_QSE);
        assertThat(testPolitiqueQSE.getVigueur()).isEqualTo(DEFAULT_VIGUEUR);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(1)).save(testPolitiqueQSE);
    }

    @Test
    @Transactional
    void createPolitiqueQSEWithExistingId() throws Exception {
        // Create the PolitiqueQSE with an existing ID
        politiqueQSE.setId(1L);

        int databaseSizeBeforeCreate = politiqueQSERepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolitiqueQSEMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(politiqueQSE)))
            .andExpect(status().isBadRequest());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeCreate);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(0)).save(politiqueQSE);
    }

    @Test
    @Transactional
    void getAllPolitiqueQSES() throws Exception {
        // Initialize the database
        politiqueQSERepository.saveAndFlush(politiqueQSE);

        // Get all the politiqueQSEList
        restPolitiqueQSEMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(politiqueQSE.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].axePolitiqueQSE").value(hasItem(DEFAULT_AXE_POLITIQUE_QSE)))
            .andExpect(jsonPath("$.[*].objectifQSE").value(hasItem(DEFAULT_OBJECTIF_QSE)))
            .andExpect(jsonPath("$.[*].vigueur").value(hasItem(DEFAULT_VIGUEUR.booleanValue())));
    }

    @Test
    @Transactional
    void getPolitiqueQSE() throws Exception {
        // Initialize the database
        politiqueQSERepository.saveAndFlush(politiqueQSE);

        // Get the politiqueQSE
        restPolitiqueQSEMockMvc
            .perform(get(ENTITY_API_URL_ID, politiqueQSE.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(politiqueQSE.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.axePolitiqueQSE").value(DEFAULT_AXE_POLITIQUE_QSE))
            .andExpect(jsonPath("$.objectifQSE").value(DEFAULT_OBJECTIF_QSE))
            .andExpect(jsonPath("$.vigueur").value(DEFAULT_VIGUEUR.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPolitiqueQSE() throws Exception {
        // Get the politiqueQSE
        restPolitiqueQSEMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPolitiqueQSE() throws Exception {
        // Initialize the database
        politiqueQSERepository.saveAndFlush(politiqueQSE);

        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();

        // Update the politiqueQSE
        PolitiqueQSE updatedPolitiqueQSE = politiqueQSERepository.findById(politiqueQSE.getId()).get();
        // Disconnect from session so that the updates on updatedPolitiqueQSE are not directly saved in db
        em.detach(updatedPolitiqueQSE);
        updatedPolitiqueQSE
            .date(UPDATED_DATE)
            .axePolitiqueQSE(UPDATED_AXE_POLITIQUE_QSE)
            .objectifQSE(UPDATED_OBJECTIF_QSE)
            .vigueur(UPDATED_VIGUEUR);

        restPolitiqueQSEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPolitiqueQSE.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPolitiqueQSE))
            )
            .andExpect(status().isOk());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);
        PolitiqueQSE testPolitiqueQSE = politiqueQSEList.get(politiqueQSEList.size() - 1);
        assertThat(testPolitiqueQSE.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPolitiqueQSE.getAxePolitiqueQSE()).isEqualTo(UPDATED_AXE_POLITIQUE_QSE);
        assertThat(testPolitiqueQSE.getObjectifQSE()).isEqualTo(UPDATED_OBJECTIF_QSE);
        assertThat(testPolitiqueQSE.getVigueur()).isEqualTo(UPDATED_VIGUEUR);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository).save(testPolitiqueQSE);
    }

    @Test
    @Transactional
    void putNonExistingPolitiqueQSE() throws Exception {
        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();
        politiqueQSE.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolitiqueQSEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, politiqueQSE.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(politiqueQSE))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(0)).save(politiqueQSE);
    }

    @Test
    @Transactional
    void putWithIdMismatchPolitiqueQSE() throws Exception {
        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();
        politiqueQSE.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolitiqueQSEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(politiqueQSE))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(0)).save(politiqueQSE);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPolitiqueQSE() throws Exception {
        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();
        politiqueQSE.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolitiqueQSEMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(politiqueQSE)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(0)).save(politiqueQSE);
    }

    @Test
    @Transactional
    void partialUpdatePolitiqueQSEWithPatch() throws Exception {
        // Initialize the database
        politiqueQSERepository.saveAndFlush(politiqueQSE);

        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();

        // Update the politiqueQSE using partial update
        PolitiqueQSE partialUpdatedPolitiqueQSE = new PolitiqueQSE();
        partialUpdatedPolitiqueQSE.setId(politiqueQSE.getId());

        partialUpdatedPolitiqueQSE.axePolitiqueQSE(UPDATED_AXE_POLITIQUE_QSE).vigueur(UPDATED_VIGUEUR);

        restPolitiqueQSEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolitiqueQSE.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPolitiqueQSE))
            )
            .andExpect(status().isOk());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);
        PolitiqueQSE testPolitiqueQSE = politiqueQSEList.get(politiqueQSEList.size() - 1);
        assertThat(testPolitiqueQSE.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPolitiqueQSE.getAxePolitiqueQSE()).isEqualTo(UPDATED_AXE_POLITIQUE_QSE);
        assertThat(testPolitiqueQSE.getObjectifQSE()).isEqualTo(DEFAULT_OBJECTIF_QSE);
        assertThat(testPolitiqueQSE.getVigueur()).isEqualTo(UPDATED_VIGUEUR);
    }

    @Test
    @Transactional
    void fullUpdatePolitiqueQSEWithPatch() throws Exception {
        // Initialize the database
        politiqueQSERepository.saveAndFlush(politiqueQSE);

        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();

        // Update the politiqueQSE using partial update
        PolitiqueQSE partialUpdatedPolitiqueQSE = new PolitiqueQSE();
        partialUpdatedPolitiqueQSE.setId(politiqueQSE.getId());

        partialUpdatedPolitiqueQSE
            .date(UPDATED_DATE)
            .axePolitiqueQSE(UPDATED_AXE_POLITIQUE_QSE)
            .objectifQSE(UPDATED_OBJECTIF_QSE)
            .vigueur(UPDATED_VIGUEUR);

        restPolitiqueQSEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolitiqueQSE.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPolitiqueQSE))
            )
            .andExpect(status().isOk());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);
        PolitiqueQSE testPolitiqueQSE = politiqueQSEList.get(politiqueQSEList.size() - 1);
        assertThat(testPolitiqueQSE.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPolitiqueQSE.getAxePolitiqueQSE()).isEqualTo(UPDATED_AXE_POLITIQUE_QSE);
        assertThat(testPolitiqueQSE.getObjectifQSE()).isEqualTo(UPDATED_OBJECTIF_QSE);
        assertThat(testPolitiqueQSE.getVigueur()).isEqualTo(UPDATED_VIGUEUR);
    }

    @Test
    @Transactional
    void patchNonExistingPolitiqueQSE() throws Exception {
        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();
        politiqueQSE.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolitiqueQSEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, politiqueQSE.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(politiqueQSE))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(0)).save(politiqueQSE);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPolitiqueQSE() throws Exception {
        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();
        politiqueQSE.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolitiqueQSEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(politiqueQSE))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(0)).save(politiqueQSE);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPolitiqueQSE() throws Exception {
        int databaseSizeBeforeUpdate = politiqueQSERepository.findAll().size();
        politiqueQSE.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolitiqueQSEMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(politiqueQSE))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolitiqueQSE in the database
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(0)).save(politiqueQSE);
    }

    @Test
    @Transactional
    void deletePolitiqueQSE() throws Exception {
        // Initialize the database
        politiqueQSERepository.saveAndFlush(politiqueQSE);

        int databaseSizeBeforeDelete = politiqueQSERepository.findAll().size();

        // Delete the politiqueQSE
        restPolitiqueQSEMockMvc
            .perform(delete(ENTITY_API_URL_ID, politiqueQSE.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PolitiqueQSE> politiqueQSEList = politiqueQSERepository.findAll();
        assertThat(politiqueQSEList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PolitiqueQSE in Elasticsearch
        verify(mockPolitiqueQSESearchRepository, times(1)).deleteById(politiqueQSE.getId());
    }

    @Test
    @Transactional
    void searchPolitiqueQSE() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        politiqueQSERepository.saveAndFlush(politiqueQSE);
        when(mockPolitiqueQSESearchRepository.search(queryStringQuery("id:" + politiqueQSE.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(politiqueQSE), PageRequest.of(0, 1), 1));

        // Search the politiqueQSE
        restPolitiqueQSEMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + politiqueQSE.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(politiqueQSE.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].axePolitiqueQSE").value(hasItem(DEFAULT_AXE_POLITIQUE_QSE)))
            .andExpect(jsonPath("$.[*].objectifQSE").value(hasItem(DEFAULT_OBJECTIF_QSE)))
            .andExpect(jsonPath("$.[*].vigueur").value(hasItem(DEFAULT_VIGUEUR.booleanValue())));
    }
}
