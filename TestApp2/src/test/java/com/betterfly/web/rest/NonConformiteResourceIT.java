package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.NonConformite;
import com.betterfly.repository.NonConformiteRepository;
import com.betterfly.repository.search.NonConformiteSearchRepository;
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
 * Integration tests for the {@link NonConformiteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NonConformiteResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CAUSES_POTENTIELLES = "AAAAAAAAAA";
    private static final String UPDATED_CAUSES_POTENTIELLES = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/non-conformites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/non-conformites";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NonConformiteRepository nonConformiteRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.NonConformiteSearchRepositoryMockConfiguration
     */
    @Autowired
    private NonConformiteSearchRepository mockNonConformiteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNonConformiteMockMvc;

    private NonConformite nonConformite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NonConformite createEntity(EntityManager em) {
        NonConformite nonConformite = new NonConformite()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .causesPotentielles(DEFAULT_CAUSES_POTENTIELLES)
            .origine(DEFAULT_ORIGINE);
        return nonConformite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NonConformite createUpdatedEntity(EntityManager em) {
        NonConformite nonConformite = new NonConformite()
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .causesPotentielles(UPDATED_CAUSES_POTENTIELLES)
            .origine(UPDATED_ORIGINE);
        return nonConformite;
    }

    @BeforeEach
    public void initTest() {
        nonConformite = createEntity(em);
    }

    @Test
    @Transactional
    void createNonConformite() throws Exception {
        int databaseSizeBeforeCreate = nonConformiteRepository.findAll().size();
        // Create the NonConformite
        restNonConformiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nonConformite)))
            .andExpect(status().isCreated());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeCreate + 1);
        NonConformite testNonConformite = nonConformiteList.get(nonConformiteList.size() - 1);
        assertThat(testNonConformite.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testNonConformite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNonConformite.getCausesPotentielles()).isEqualTo(DEFAULT_CAUSES_POTENTIELLES);
        assertThat(testNonConformite.getOrigine()).isEqualTo(DEFAULT_ORIGINE);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(1)).save(testNonConformite);
    }

    @Test
    @Transactional
    void createNonConformiteWithExistingId() throws Exception {
        // Create the NonConformite with an existing ID
        nonConformite.setId(1L);

        int databaseSizeBeforeCreate = nonConformiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNonConformiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nonConformite)))
            .andExpect(status().isBadRequest());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeCreate);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(0)).save(nonConformite);
    }

    @Test
    @Transactional
    void getAllNonConformites() throws Exception {
        // Initialize the database
        nonConformiteRepository.saveAndFlush(nonConformite);

        // Get all the nonConformiteList
        restNonConformiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nonConformite.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].causesPotentielles").value(hasItem(DEFAULT_CAUSES_POTENTIELLES)))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }

    @Test
    @Transactional
    void getNonConformite() throws Exception {
        // Initialize the database
        nonConformiteRepository.saveAndFlush(nonConformite);

        // Get the nonConformite
        restNonConformiteMockMvc
            .perform(get(ENTITY_API_URL_ID, nonConformite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nonConformite.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.causesPotentielles").value(DEFAULT_CAUSES_POTENTIELLES))
            .andExpect(jsonPath("$.origine").value(DEFAULT_ORIGINE));
    }

    @Test
    @Transactional
    void getNonExistingNonConformite() throws Exception {
        // Get the nonConformite
        restNonConformiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNonConformite() throws Exception {
        // Initialize the database
        nonConformiteRepository.saveAndFlush(nonConformite);

        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();

        // Update the nonConformite
        NonConformite updatedNonConformite = nonConformiteRepository.findById(nonConformite.getId()).get();
        // Disconnect from session so that the updates on updatedNonConformite are not directly saved in db
        em.detach(updatedNonConformite);
        updatedNonConformite
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .causesPotentielles(UPDATED_CAUSES_POTENTIELLES)
            .origine(UPDATED_ORIGINE);

        restNonConformiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNonConformite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNonConformite))
            )
            .andExpect(status().isOk());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);
        NonConformite testNonConformite = nonConformiteList.get(nonConformiteList.size() - 1);
        assertThat(testNonConformite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testNonConformite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNonConformite.getCausesPotentielles()).isEqualTo(UPDATED_CAUSES_POTENTIELLES);
        assertThat(testNonConformite.getOrigine()).isEqualTo(UPDATED_ORIGINE);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository).save(testNonConformite);
    }

    @Test
    @Transactional
    void putNonExistingNonConformite() throws Exception {
        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();
        nonConformite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNonConformiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nonConformite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nonConformite))
            )
            .andExpect(status().isBadRequest());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(0)).save(nonConformite);
    }

    @Test
    @Transactional
    void putWithIdMismatchNonConformite() throws Exception {
        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();
        nonConformite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNonConformiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nonConformite))
            )
            .andExpect(status().isBadRequest());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(0)).save(nonConformite);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNonConformite() throws Exception {
        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();
        nonConformite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNonConformiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nonConformite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(0)).save(nonConformite);
    }

    @Test
    @Transactional
    void partialUpdateNonConformiteWithPatch() throws Exception {
        // Initialize the database
        nonConformiteRepository.saveAndFlush(nonConformite);

        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();

        // Update the nonConformite using partial update
        NonConformite partialUpdatedNonConformite = new NonConformite();
        partialUpdatedNonConformite.setId(nonConformite.getId());

        partialUpdatedNonConformite.date(UPDATED_DATE).description(UPDATED_DESCRIPTION).causesPotentielles(UPDATED_CAUSES_POTENTIELLES);

        restNonConformiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNonConformite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNonConformite))
            )
            .andExpect(status().isOk());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);
        NonConformite testNonConformite = nonConformiteList.get(nonConformiteList.size() - 1);
        assertThat(testNonConformite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testNonConformite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNonConformite.getCausesPotentielles()).isEqualTo(UPDATED_CAUSES_POTENTIELLES);
        assertThat(testNonConformite.getOrigine()).isEqualTo(DEFAULT_ORIGINE);
    }

    @Test
    @Transactional
    void fullUpdateNonConformiteWithPatch() throws Exception {
        // Initialize the database
        nonConformiteRepository.saveAndFlush(nonConformite);

        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();

        // Update the nonConformite using partial update
        NonConformite partialUpdatedNonConformite = new NonConformite();
        partialUpdatedNonConformite.setId(nonConformite.getId());

        partialUpdatedNonConformite
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .causesPotentielles(UPDATED_CAUSES_POTENTIELLES)
            .origine(UPDATED_ORIGINE);

        restNonConformiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNonConformite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNonConformite))
            )
            .andExpect(status().isOk());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);
        NonConformite testNonConformite = nonConformiteList.get(nonConformiteList.size() - 1);
        assertThat(testNonConformite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testNonConformite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNonConformite.getCausesPotentielles()).isEqualTo(UPDATED_CAUSES_POTENTIELLES);
        assertThat(testNonConformite.getOrigine()).isEqualTo(UPDATED_ORIGINE);
    }

    @Test
    @Transactional
    void patchNonExistingNonConformite() throws Exception {
        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();
        nonConformite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNonConformiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nonConformite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nonConformite))
            )
            .andExpect(status().isBadRequest());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(0)).save(nonConformite);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNonConformite() throws Exception {
        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();
        nonConformite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNonConformiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nonConformite))
            )
            .andExpect(status().isBadRequest());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(0)).save(nonConformite);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNonConformite() throws Exception {
        int databaseSizeBeforeUpdate = nonConformiteRepository.findAll().size();
        nonConformite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNonConformiteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nonConformite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NonConformite in the database
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(0)).save(nonConformite);
    }

    @Test
    @Transactional
    void deleteNonConformite() throws Exception {
        // Initialize the database
        nonConformiteRepository.saveAndFlush(nonConformite);

        int databaseSizeBeforeDelete = nonConformiteRepository.findAll().size();

        // Delete the nonConformite
        restNonConformiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, nonConformite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NonConformite> nonConformiteList = nonConformiteRepository.findAll();
        assertThat(nonConformiteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NonConformite in Elasticsearch
        verify(mockNonConformiteSearchRepository, times(1)).deleteById(nonConformite.getId());
    }

    @Test
    @Transactional
    void searchNonConformite() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nonConformiteRepository.saveAndFlush(nonConformite);
        when(mockNonConformiteSearchRepository.search(queryStringQuery("id:" + nonConformite.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nonConformite), PageRequest.of(0, 1), 1));

        // Search the nonConformite
        restNonConformiteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nonConformite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nonConformite.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].causesPotentielles").value(hasItem(DEFAULT_CAUSES_POTENTIELLES)))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }
}
