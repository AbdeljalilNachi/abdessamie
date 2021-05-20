package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.PlanificationRDD;
import com.betterfly.domain.enumeration.Standard;
import com.betterfly.repository.PlanificationRDDRepository;
import com.betterfly.repository.search.PlanificationRDDSearchRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PlanificationRDDResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlanificationRDDResourceIT {

    private static final Integer DEFAULT_N_RDD = 1;
    private static final Integer UPDATED_N_RDD = 2;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_REALISEE = false;
    private static final Boolean UPDATED_REALISEE = true;

    private static final byte[] DEFAULT_PRESENTATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRESENTATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRESENTATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRESENTATION_CONTENT_TYPE = "image/png";

    private static final Standard DEFAULT_STANDARD = Standard.ISO9001;
    private static final Standard UPDATED_STANDARD = Standard.ISO14001;

    private static final String ENTITY_API_URL = "/api/planification-rdds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/planification-rdds";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanificationRDDRepository planificationRDDRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.PlanificationRDDSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanificationRDDSearchRepository mockPlanificationRDDSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanificationRDDMockMvc;

    private PlanificationRDD planificationRDD;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanificationRDD createEntity(EntityManager em) {
        PlanificationRDD planificationRDD = new PlanificationRDD()
            .nRdd(DEFAULT_N_RDD)
            .date(DEFAULT_DATE)
            .realisee(DEFAULT_REALISEE)
            .presentation(DEFAULT_PRESENTATION)
            .presentationContentType(DEFAULT_PRESENTATION_CONTENT_TYPE)
            .standard(DEFAULT_STANDARD);
        return planificationRDD;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanificationRDD createUpdatedEntity(EntityManager em) {
        PlanificationRDD planificationRDD = new PlanificationRDD()
            .nRdd(UPDATED_N_RDD)
            .date(UPDATED_DATE)
            .realisee(UPDATED_REALISEE)
            .presentation(UPDATED_PRESENTATION)
            .presentationContentType(UPDATED_PRESENTATION_CONTENT_TYPE)
            .standard(UPDATED_STANDARD);
        return planificationRDD;
    }

    @BeforeEach
    public void initTest() {
        planificationRDD = createEntity(em);
    }

    @Test
    @Transactional
    void createPlanificationRDD() throws Exception {
        int databaseSizeBeforeCreate = planificationRDDRepository.findAll().size();
        // Create the PlanificationRDD
        restPlanificationRDDMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planificationRDD))
            )
            .andExpect(status().isCreated());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeCreate + 1);
        PlanificationRDD testPlanificationRDD = planificationRDDList.get(planificationRDDList.size() - 1);
        assertThat(testPlanificationRDD.getnRdd()).isEqualTo(DEFAULT_N_RDD);
        assertThat(testPlanificationRDD.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPlanificationRDD.getRealisee()).isEqualTo(DEFAULT_REALISEE);
        assertThat(testPlanificationRDD.getPresentation()).isEqualTo(DEFAULT_PRESENTATION);
        assertThat(testPlanificationRDD.getPresentationContentType()).isEqualTo(DEFAULT_PRESENTATION_CONTENT_TYPE);
        assertThat(testPlanificationRDD.getStandard()).isEqualTo(DEFAULT_STANDARD);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(1)).save(testPlanificationRDD);
    }

    @Test
    @Transactional
    void createPlanificationRDDWithExistingId() throws Exception {
        // Create the PlanificationRDD with an existing ID
        planificationRDD.setId(1L);

        int databaseSizeBeforeCreate = planificationRDDRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanificationRDDMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planificationRDD))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(0)).save(planificationRDD);
    }

    @Test
    @Transactional
    void getAllPlanificationRDDS() throws Exception {
        // Initialize the database
        planificationRDDRepository.saveAndFlush(planificationRDD);

        // Get all the planificationRDDList
        restPlanificationRDDMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planificationRDD.getId().intValue())))
            .andExpect(jsonPath("$.[*].nRdd").value(hasItem(DEFAULT_N_RDD)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].realisee").value(hasItem(DEFAULT_REALISEE.booleanValue())))
            .andExpect(jsonPath("$.[*].presentationContentType").value(hasItem(DEFAULT_PRESENTATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].presentation").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRESENTATION))))
            .andExpect(jsonPath("$.[*].standard").value(hasItem(DEFAULT_STANDARD.toString())));
    }

    @Test
    @Transactional
    void getPlanificationRDD() throws Exception {
        // Initialize the database
        planificationRDDRepository.saveAndFlush(planificationRDD);

        // Get the planificationRDD
        restPlanificationRDDMockMvc
            .perform(get(ENTITY_API_URL_ID, planificationRDD.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planificationRDD.getId().intValue()))
            .andExpect(jsonPath("$.nRdd").value(DEFAULT_N_RDD))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.realisee").value(DEFAULT_REALISEE.booleanValue()))
            .andExpect(jsonPath("$.presentationContentType").value(DEFAULT_PRESENTATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.presentation").value(Base64Utils.encodeToString(DEFAULT_PRESENTATION)))
            .andExpect(jsonPath("$.standard").value(DEFAULT_STANDARD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlanificationRDD() throws Exception {
        // Get the planificationRDD
        restPlanificationRDDMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlanificationRDD() throws Exception {
        // Initialize the database
        planificationRDDRepository.saveAndFlush(planificationRDD);

        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();

        // Update the planificationRDD
        PlanificationRDD updatedPlanificationRDD = planificationRDDRepository.findById(planificationRDD.getId()).get();
        // Disconnect from session so that the updates on updatedPlanificationRDD are not directly saved in db
        em.detach(updatedPlanificationRDD);
        updatedPlanificationRDD
            .nRdd(UPDATED_N_RDD)
            .date(UPDATED_DATE)
            .realisee(UPDATED_REALISEE)
            .presentation(UPDATED_PRESENTATION)
            .presentationContentType(UPDATED_PRESENTATION_CONTENT_TYPE)
            .standard(UPDATED_STANDARD);

        restPlanificationRDDMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlanificationRDD.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlanificationRDD))
            )
            .andExpect(status().isOk());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);
        PlanificationRDD testPlanificationRDD = planificationRDDList.get(planificationRDDList.size() - 1);
        assertThat(testPlanificationRDD.getnRdd()).isEqualTo(UPDATED_N_RDD);
        assertThat(testPlanificationRDD.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPlanificationRDD.getRealisee()).isEqualTo(UPDATED_REALISEE);
        assertThat(testPlanificationRDD.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
        assertThat(testPlanificationRDD.getPresentationContentType()).isEqualTo(UPDATED_PRESENTATION_CONTENT_TYPE);
        assertThat(testPlanificationRDD.getStandard()).isEqualTo(UPDATED_STANDARD);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository).save(testPlanificationRDD);
    }

    @Test
    @Transactional
    void putNonExistingPlanificationRDD() throws Exception {
        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();
        planificationRDD.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanificationRDDMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planificationRDD.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planificationRDD))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(0)).save(planificationRDD);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanificationRDD() throws Exception {
        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();
        planificationRDD.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanificationRDDMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planificationRDD))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(0)).save(planificationRDD);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanificationRDD() throws Exception {
        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();
        planificationRDD.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanificationRDDMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planificationRDD))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(0)).save(planificationRDD);
    }

    @Test
    @Transactional
    void partialUpdatePlanificationRDDWithPatch() throws Exception {
        // Initialize the database
        planificationRDDRepository.saveAndFlush(planificationRDD);

        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();

        // Update the planificationRDD using partial update
        PlanificationRDD partialUpdatedPlanificationRDD = new PlanificationRDD();
        partialUpdatedPlanificationRDD.setId(planificationRDD.getId());

        partialUpdatedPlanificationRDD.presentation(UPDATED_PRESENTATION).presentationContentType(UPDATED_PRESENTATION_CONTENT_TYPE);

        restPlanificationRDDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanificationRDD.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanificationRDD))
            )
            .andExpect(status().isOk());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);
        PlanificationRDD testPlanificationRDD = planificationRDDList.get(planificationRDDList.size() - 1);
        assertThat(testPlanificationRDD.getnRdd()).isEqualTo(DEFAULT_N_RDD);
        assertThat(testPlanificationRDD.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPlanificationRDD.getRealisee()).isEqualTo(DEFAULT_REALISEE);
        assertThat(testPlanificationRDD.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
        assertThat(testPlanificationRDD.getPresentationContentType()).isEqualTo(UPDATED_PRESENTATION_CONTENT_TYPE);
        assertThat(testPlanificationRDD.getStandard()).isEqualTo(DEFAULT_STANDARD);
    }

    @Test
    @Transactional
    void fullUpdatePlanificationRDDWithPatch() throws Exception {
        // Initialize the database
        planificationRDDRepository.saveAndFlush(planificationRDD);

        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();

        // Update the planificationRDD using partial update
        PlanificationRDD partialUpdatedPlanificationRDD = new PlanificationRDD();
        partialUpdatedPlanificationRDD.setId(planificationRDD.getId());

        partialUpdatedPlanificationRDD
            .nRdd(UPDATED_N_RDD)
            .date(UPDATED_DATE)
            .realisee(UPDATED_REALISEE)
            .presentation(UPDATED_PRESENTATION)
            .presentationContentType(UPDATED_PRESENTATION_CONTENT_TYPE)
            .standard(UPDATED_STANDARD);

        restPlanificationRDDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanificationRDD.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanificationRDD))
            )
            .andExpect(status().isOk());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);
        PlanificationRDD testPlanificationRDD = planificationRDDList.get(planificationRDDList.size() - 1);
        assertThat(testPlanificationRDD.getnRdd()).isEqualTo(UPDATED_N_RDD);
        assertThat(testPlanificationRDD.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPlanificationRDD.getRealisee()).isEqualTo(UPDATED_REALISEE);
        assertThat(testPlanificationRDD.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
        assertThat(testPlanificationRDD.getPresentationContentType()).isEqualTo(UPDATED_PRESENTATION_CONTENT_TYPE);
        assertThat(testPlanificationRDD.getStandard()).isEqualTo(UPDATED_STANDARD);
    }

    @Test
    @Transactional
    void patchNonExistingPlanificationRDD() throws Exception {
        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();
        planificationRDD.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanificationRDDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planificationRDD.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planificationRDD))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(0)).save(planificationRDD);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanificationRDD() throws Exception {
        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();
        planificationRDD.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanificationRDDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planificationRDD))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(0)).save(planificationRDD);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanificationRDD() throws Exception {
        int databaseSizeBeforeUpdate = planificationRDDRepository.findAll().size();
        planificationRDD.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanificationRDDMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planificationRDD))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanificationRDD in the database
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(0)).save(planificationRDD);
    }

    @Test
    @Transactional
    void deletePlanificationRDD() throws Exception {
        // Initialize the database
        planificationRDDRepository.saveAndFlush(planificationRDD);

        int databaseSizeBeforeDelete = planificationRDDRepository.findAll().size();

        // Delete the planificationRDD
        restPlanificationRDDMockMvc
            .perform(delete(ENTITY_API_URL_ID, planificationRDD.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanificationRDD> planificationRDDList = planificationRDDRepository.findAll();
        assertThat(planificationRDDList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanificationRDD in Elasticsearch
        verify(mockPlanificationRDDSearchRepository, times(1)).deleteById(planificationRDD.getId());
    }

    @Test
    @Transactional
    void searchPlanificationRDD() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        planificationRDDRepository.saveAndFlush(planificationRDD);
        when(mockPlanificationRDDSearchRepository.search(queryStringQuery("id:" + planificationRDD.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planificationRDD), PageRequest.of(0, 1), 1));

        // Search the planificationRDD
        restPlanificationRDDMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + planificationRDD.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planificationRDD.getId().intValue())))
            .andExpect(jsonPath("$.[*].nRdd").value(hasItem(DEFAULT_N_RDD)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].realisee").value(hasItem(DEFAULT_REALISEE.booleanValue())))
            .andExpect(jsonPath("$.[*].presentationContentType").value(hasItem(DEFAULT_PRESENTATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].presentation").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRESENTATION))))
            .andExpect(jsonPath("$.[*].standard").value(hasItem(DEFAULT_STANDARD.toString())));
    }
}
