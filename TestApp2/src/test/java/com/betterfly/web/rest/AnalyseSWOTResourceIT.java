package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.AnalyseSWOT;
import com.betterfly.domain.enumeration.TypeAnalyseSWOT;
import com.betterfly.repository.AnalyseSWOTRepository;
import com.betterfly.repository.search.AnalyseSWOTSearchRepository;
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
 * Integration tests for the {@link AnalyseSWOTResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AnalyseSWOTResourceIT {

    private static final LocalDate DEFAULT_DATE_IDENTIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_IDENTIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PILOTE = "AAAAAAAAAA";
    private static final String UPDATED_PILOTE = "BBBBBBBBBB";

    private static final TypeAnalyseSWOT DEFAULT_TYPE = TypeAnalyseSWOT.FORCE;
    private static final TypeAnalyseSWOT UPDATED_TYPE = TypeAnalyseSWOT.FAIBLESSE;

    private static final String DEFAULT_BU = "AAAAAAAAAA";
    private static final String UPDATED_BU = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AFFICHER = false;
    private static final Boolean UPDATED_AFFICHER = true;

    private static final String ENTITY_API_URL = "/api/analyse-swots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/analyse-swots";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnalyseSWOTRepository analyseSWOTRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.AnalyseSWOTSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnalyseSWOTSearchRepository mockAnalyseSWOTSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnalyseSWOTMockMvc;

    private AnalyseSWOT analyseSWOT;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalyseSWOT createEntity(EntityManager em) {
        AnalyseSWOT analyseSWOT = new AnalyseSWOT()
            .dateIdentification(DEFAULT_DATE_IDENTIFICATION)
            .description(DEFAULT_DESCRIPTION)
            .pilote(DEFAULT_PILOTE)
            .type(DEFAULT_TYPE)
            .bu(DEFAULT_BU)
            .commentaire(DEFAULT_COMMENTAIRE)
            .afficher(DEFAULT_AFFICHER);
        return analyseSWOT;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalyseSWOT createUpdatedEntity(EntityManager em) {
        AnalyseSWOT analyseSWOT = new AnalyseSWOT()
            .dateIdentification(UPDATED_DATE_IDENTIFICATION)
            .description(UPDATED_DESCRIPTION)
            .pilote(UPDATED_PILOTE)
            .type(UPDATED_TYPE)
            .bu(UPDATED_BU)
            .commentaire(UPDATED_COMMENTAIRE)
            .afficher(UPDATED_AFFICHER);
        return analyseSWOT;
    }

    @BeforeEach
    public void initTest() {
        analyseSWOT = createEntity(em);
    }

    @Test
    @Transactional
    void createAnalyseSWOT() throws Exception {
        int databaseSizeBeforeCreate = analyseSWOTRepository.findAll().size();
        // Create the AnalyseSWOT
        restAnalyseSWOTMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(analyseSWOT)))
            .andExpect(status().isCreated());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeCreate + 1);
        AnalyseSWOT testAnalyseSWOT = analyseSWOTList.get(analyseSWOTList.size() - 1);
        assertThat(testAnalyseSWOT.getDateIdentification()).isEqualTo(DEFAULT_DATE_IDENTIFICATION);
        assertThat(testAnalyseSWOT.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnalyseSWOT.getPilote()).isEqualTo(DEFAULT_PILOTE);
        assertThat(testAnalyseSWOT.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAnalyseSWOT.getBu()).isEqualTo(DEFAULT_BU);
        assertThat(testAnalyseSWOT.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testAnalyseSWOT.getAfficher()).isEqualTo(DEFAULT_AFFICHER);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(1)).save(testAnalyseSWOT);
    }

    @Test
    @Transactional
    void createAnalyseSWOTWithExistingId() throws Exception {
        // Create the AnalyseSWOT with an existing ID
        analyseSWOT.setId(1L);

        int databaseSizeBeforeCreate = analyseSWOTRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalyseSWOTMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(analyseSWOT)))
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(0)).save(analyseSWOT);
    }

    @Test
    @Transactional
    void getAllAnalyseSWOTS() throws Exception {
        // Initialize the database
        analyseSWOTRepository.saveAndFlush(analyseSWOT);

        // Get all the analyseSWOTList
        restAnalyseSWOTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyseSWOT.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIdentification").value(hasItem(DEFAULT_DATE_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pilote").value(hasItem(DEFAULT_PILOTE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].bu").value(hasItem(DEFAULT_BU)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].afficher").value(hasItem(DEFAULT_AFFICHER.booleanValue())));
    }

    @Test
    @Transactional
    void getAnalyseSWOT() throws Exception {
        // Initialize the database
        analyseSWOTRepository.saveAndFlush(analyseSWOT);

        // Get the analyseSWOT
        restAnalyseSWOTMockMvc
            .perform(get(ENTITY_API_URL_ID, analyseSWOT.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(analyseSWOT.getId().intValue()))
            .andExpect(jsonPath("$.dateIdentification").value(DEFAULT_DATE_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.pilote").value(DEFAULT_PILOTE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.bu").value(DEFAULT_BU))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.afficher").value(DEFAULT_AFFICHER.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAnalyseSWOT() throws Exception {
        // Get the analyseSWOT
        restAnalyseSWOTMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnalyseSWOT() throws Exception {
        // Initialize the database
        analyseSWOTRepository.saveAndFlush(analyseSWOT);

        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();

        // Update the analyseSWOT
        AnalyseSWOT updatedAnalyseSWOT = analyseSWOTRepository.findById(analyseSWOT.getId()).get();
        // Disconnect from session so that the updates on updatedAnalyseSWOT are not directly saved in db
        em.detach(updatedAnalyseSWOT);
        updatedAnalyseSWOT
            .dateIdentification(UPDATED_DATE_IDENTIFICATION)
            .description(UPDATED_DESCRIPTION)
            .pilote(UPDATED_PILOTE)
            .type(UPDATED_TYPE)
            .bu(UPDATED_BU)
            .commentaire(UPDATED_COMMENTAIRE)
            .afficher(UPDATED_AFFICHER);

        restAnalyseSWOTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnalyseSWOT.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnalyseSWOT))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);
        AnalyseSWOT testAnalyseSWOT = analyseSWOTList.get(analyseSWOTList.size() - 1);
        assertThat(testAnalyseSWOT.getDateIdentification()).isEqualTo(UPDATED_DATE_IDENTIFICATION);
        assertThat(testAnalyseSWOT.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnalyseSWOT.getPilote()).isEqualTo(UPDATED_PILOTE);
        assertThat(testAnalyseSWOT.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAnalyseSWOT.getBu()).isEqualTo(UPDATED_BU);
        assertThat(testAnalyseSWOT.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testAnalyseSWOT.getAfficher()).isEqualTo(UPDATED_AFFICHER);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository).save(testAnalyseSWOT);
    }

    @Test
    @Transactional
    void putNonExistingAnalyseSWOT() throws Exception {
        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();
        analyseSWOT.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyseSWOTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, analyseSWOT.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseSWOT))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(0)).save(analyseSWOT);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnalyseSWOT() throws Exception {
        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();
        analyseSWOT.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseSWOTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseSWOT))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(0)).save(analyseSWOT);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnalyseSWOT() throws Exception {
        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();
        analyseSWOT.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseSWOTMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(analyseSWOT)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(0)).save(analyseSWOT);
    }

    @Test
    @Transactional
    void partialUpdateAnalyseSWOTWithPatch() throws Exception {
        // Initialize the database
        analyseSWOTRepository.saveAndFlush(analyseSWOT);

        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();

        // Update the analyseSWOT using partial update
        AnalyseSWOT partialUpdatedAnalyseSWOT = new AnalyseSWOT();
        partialUpdatedAnalyseSWOT.setId(analyseSWOT.getId());

        partialUpdatedAnalyseSWOT.type(UPDATED_TYPE).bu(UPDATED_BU);

        restAnalyseSWOTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyseSWOT.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnalyseSWOT))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);
        AnalyseSWOT testAnalyseSWOT = analyseSWOTList.get(analyseSWOTList.size() - 1);
        assertThat(testAnalyseSWOT.getDateIdentification()).isEqualTo(DEFAULT_DATE_IDENTIFICATION);
        assertThat(testAnalyseSWOT.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnalyseSWOT.getPilote()).isEqualTo(DEFAULT_PILOTE);
        assertThat(testAnalyseSWOT.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAnalyseSWOT.getBu()).isEqualTo(UPDATED_BU);
        assertThat(testAnalyseSWOT.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testAnalyseSWOT.getAfficher()).isEqualTo(DEFAULT_AFFICHER);
    }

    @Test
    @Transactional
    void fullUpdateAnalyseSWOTWithPatch() throws Exception {
        // Initialize the database
        analyseSWOTRepository.saveAndFlush(analyseSWOT);

        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();

        // Update the analyseSWOT using partial update
        AnalyseSWOT partialUpdatedAnalyseSWOT = new AnalyseSWOT();
        partialUpdatedAnalyseSWOT.setId(analyseSWOT.getId());

        partialUpdatedAnalyseSWOT
            .dateIdentification(UPDATED_DATE_IDENTIFICATION)
            .description(UPDATED_DESCRIPTION)
            .pilote(UPDATED_PILOTE)
            .type(UPDATED_TYPE)
            .bu(UPDATED_BU)
            .commentaire(UPDATED_COMMENTAIRE)
            .afficher(UPDATED_AFFICHER);

        restAnalyseSWOTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyseSWOT.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnalyseSWOT))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);
        AnalyseSWOT testAnalyseSWOT = analyseSWOTList.get(analyseSWOTList.size() - 1);
        assertThat(testAnalyseSWOT.getDateIdentification()).isEqualTo(UPDATED_DATE_IDENTIFICATION);
        assertThat(testAnalyseSWOT.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnalyseSWOT.getPilote()).isEqualTo(UPDATED_PILOTE);
        assertThat(testAnalyseSWOT.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAnalyseSWOT.getBu()).isEqualTo(UPDATED_BU);
        assertThat(testAnalyseSWOT.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testAnalyseSWOT.getAfficher()).isEqualTo(UPDATED_AFFICHER);
    }

    @Test
    @Transactional
    void patchNonExistingAnalyseSWOT() throws Exception {
        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();
        analyseSWOT.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyseSWOTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, analyseSWOT.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyseSWOT))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(0)).save(analyseSWOT);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnalyseSWOT() throws Exception {
        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();
        analyseSWOT.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseSWOTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyseSWOT))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(0)).save(analyseSWOT);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnalyseSWOT() throws Exception {
        int databaseSizeBeforeUpdate = analyseSWOTRepository.findAll().size();
        analyseSWOT.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseSWOTMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(analyseSWOT))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnalyseSWOT in the database
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(0)).save(analyseSWOT);
    }

    @Test
    @Transactional
    void deleteAnalyseSWOT() throws Exception {
        // Initialize the database
        analyseSWOTRepository.saveAndFlush(analyseSWOT);

        int databaseSizeBeforeDelete = analyseSWOTRepository.findAll().size();

        // Delete the analyseSWOT
        restAnalyseSWOTMockMvc
            .perform(delete(ENTITY_API_URL_ID, analyseSWOT.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnalyseSWOT> analyseSWOTList = analyseSWOTRepository.findAll();
        assertThat(analyseSWOTList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnalyseSWOT in Elasticsearch
        verify(mockAnalyseSWOTSearchRepository, times(1)).deleteById(analyseSWOT.getId());
    }

    @Test
    @Transactional
    void searchAnalyseSWOT() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        analyseSWOTRepository.saveAndFlush(analyseSWOT);
        when(mockAnalyseSWOTSearchRepository.search(queryStringQuery("id:" + analyseSWOT.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(analyseSWOT), PageRequest.of(0, 1), 1));

        // Search the analyseSWOT
        restAnalyseSWOTMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + analyseSWOT.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyseSWOT.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIdentification").value(hasItem(DEFAULT_DATE_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pilote").value(hasItem(DEFAULT_PILOTE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].bu").value(hasItem(DEFAULT_BU)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].afficher").value(hasItem(DEFAULT_AFFICHER.booleanValue())));
    }
}
