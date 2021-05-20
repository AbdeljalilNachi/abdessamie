package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.AnalyseEnvirommentale;
import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.Situation;
import com.betterfly.repository.AnalyseEnvirommentaleRepository;
import com.betterfly.repository.search.AnalyseEnvirommentaleSearchRepository;
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
 * Integration tests for the {@link AnalyseEnvirommentaleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AnalyseEnvirommentaleResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BUSINESS_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITE = "BBBBBBBBBB";

    private static final String DEFAULT_ASPECT_ENVIRONNEMENTAL = "AAAAAAAAAA";
    private static final String UPDATED_ASPECT_ENVIRONNEMENTAL = "BBBBBBBBBB";

    private static final String DEFAULT_IMPACT_ENVIRONNEMENTAL = "AAAAAAAAAA";
    private static final String UPDATED_IMPACT_ENVIRONNEMENTAL = "BBBBBBBBBB";

    private static final String DEFAULT_COMPETENCES_REQUISES = "AAAAAAAAAA";
    private static final String UPDATED_COMPETENCES_REQUISES = "BBBBBBBBBB";

    private static final Situation DEFAULT_SITUATION = Situation.Normale;
    private static final Situation UPDATED_SITUATION = Situation.Anormale;

    private static final EnumFive DEFAULT_FREQUENCE = EnumFive.ONE;
    private static final EnumFive UPDATED_FREQUENCE = EnumFive.TWO;

    private static final EnumFive DEFAULT_SENSIBILITE_MILIEU = EnumFive.ONE;
    private static final EnumFive UPDATED_SENSIBILITE_MILIEU = EnumFive.TWO;

    private static final EnumFive DEFAULT_COEFFICIENT_MAITRISE = EnumFive.ONE;
    private static final EnumFive UPDATED_COEFFICIENT_MAITRISE = EnumFive.TWO;

    private static final EnumFive DEFAULT_GRAVITE = EnumFive.ONE;
    private static final EnumFive UPDATED_GRAVITE = EnumFive.TWO;

    private static final Integer DEFAULT_CRITICITE = 1;
    private static final Integer UPDATED_CRITICITE = 2;

    private static final String DEFAULT_MAITRISE_EXISTANTE = "AAAAAAAAAA";
    private static final String UPDATED_MAITRISE_EXISTANTE = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/analyse-envirommentales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/analyse-envirommentales";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnalyseEnvirommentaleRepository analyseEnvirommentaleRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.AnalyseEnvirommentaleSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnalyseEnvirommentaleSearchRepository mockAnalyseEnvirommentaleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnalyseEnvirommentaleMockMvc;

    private AnalyseEnvirommentale analyseEnvirommentale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalyseEnvirommentale createEntity(EntityManager em) {
        AnalyseEnvirommentale analyseEnvirommentale = new AnalyseEnvirommentale()
            .date(DEFAULT_DATE)
            .businessUnit(DEFAULT_BUSINESS_UNIT)
            .activite(DEFAULT_ACTIVITE)
            .aspectEnvironnemental(DEFAULT_ASPECT_ENVIRONNEMENTAL)
            .impactEnvironnemental(DEFAULT_IMPACT_ENVIRONNEMENTAL)
            .competencesRequises(DEFAULT_COMPETENCES_REQUISES)
            .situation(DEFAULT_SITUATION)
            .frequence(DEFAULT_FREQUENCE)
            .sensibiliteMilieu(DEFAULT_SENSIBILITE_MILIEU)
            .coefficientMaitrise(DEFAULT_COEFFICIENT_MAITRISE)
            .gravite(DEFAULT_GRAVITE)
            .criticite(DEFAULT_CRITICITE)
            .maitriseExistante(DEFAULT_MAITRISE_EXISTANTE)
            .origine(DEFAULT_ORIGINE);
        return analyseEnvirommentale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalyseEnvirommentale createUpdatedEntity(EntityManager em) {
        AnalyseEnvirommentale analyseEnvirommentale = new AnalyseEnvirommentale()
            .date(UPDATED_DATE)
            .businessUnit(UPDATED_BUSINESS_UNIT)
            .activite(UPDATED_ACTIVITE)
            .aspectEnvironnemental(UPDATED_ASPECT_ENVIRONNEMENTAL)
            .impactEnvironnemental(UPDATED_IMPACT_ENVIRONNEMENTAL)
            .competencesRequises(UPDATED_COMPETENCES_REQUISES)
            .situation(UPDATED_SITUATION)
            .frequence(UPDATED_FREQUENCE)
            .sensibiliteMilieu(UPDATED_SENSIBILITE_MILIEU)
            .coefficientMaitrise(UPDATED_COEFFICIENT_MAITRISE)
            .gravite(UPDATED_GRAVITE)
            .criticite(UPDATED_CRITICITE)
            .maitriseExistante(UPDATED_MAITRISE_EXISTANTE)
            .origine(UPDATED_ORIGINE);
        return analyseEnvirommentale;
    }

    @BeforeEach
    public void initTest() {
        analyseEnvirommentale = createEntity(em);
    }

    @Test
    @Transactional
    void createAnalyseEnvirommentale() throws Exception {
        int databaseSizeBeforeCreate = analyseEnvirommentaleRepository.findAll().size();
        // Create the AnalyseEnvirommentale
        restAnalyseEnvirommentaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseEnvirommentale))
            )
            .andExpect(status().isCreated());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeCreate + 1);
        AnalyseEnvirommentale testAnalyseEnvirommentale = analyseEnvirommentaleList.get(analyseEnvirommentaleList.size() - 1);
        assertThat(testAnalyseEnvirommentale.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAnalyseEnvirommentale.getBusinessUnit()).isEqualTo(DEFAULT_BUSINESS_UNIT);
        assertThat(testAnalyseEnvirommentale.getActivite()).isEqualTo(DEFAULT_ACTIVITE);
        assertThat(testAnalyseEnvirommentale.getAspectEnvironnemental()).isEqualTo(DEFAULT_ASPECT_ENVIRONNEMENTAL);
        assertThat(testAnalyseEnvirommentale.getImpactEnvironnemental()).isEqualTo(DEFAULT_IMPACT_ENVIRONNEMENTAL);
        assertThat(testAnalyseEnvirommentale.getCompetencesRequises()).isEqualTo(DEFAULT_COMPETENCES_REQUISES);
        assertThat(testAnalyseEnvirommentale.getSituation()).isEqualTo(DEFAULT_SITUATION);
        assertThat(testAnalyseEnvirommentale.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
        assertThat(testAnalyseEnvirommentale.getSensibiliteMilieu()).isEqualTo(DEFAULT_SENSIBILITE_MILIEU);
        assertThat(testAnalyseEnvirommentale.getCoefficientMaitrise()).isEqualTo(DEFAULT_COEFFICIENT_MAITRISE);
        assertThat(testAnalyseEnvirommentale.getGravite()).isEqualTo(DEFAULT_GRAVITE);
        assertThat(testAnalyseEnvirommentale.getCriticite()).isEqualTo(DEFAULT_CRITICITE);
        assertThat(testAnalyseEnvirommentale.getMaitriseExistante()).isEqualTo(DEFAULT_MAITRISE_EXISTANTE);
        assertThat(testAnalyseEnvirommentale.getOrigine()).isEqualTo(DEFAULT_ORIGINE);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(1)).save(testAnalyseEnvirommentale);
    }

    @Test
    @Transactional
    void createAnalyseEnvirommentaleWithExistingId() throws Exception {
        // Create the AnalyseEnvirommentale with an existing ID
        analyseEnvirommentale.setId(1L);

        int databaseSizeBeforeCreate = analyseEnvirommentaleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalyseEnvirommentaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseEnvirommentale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(0)).save(analyseEnvirommentale);
    }

    @Test
    @Transactional
    void getAllAnalyseEnvirommentales() throws Exception {
        // Initialize the database
        analyseEnvirommentaleRepository.saveAndFlush(analyseEnvirommentale);

        // Get all the analyseEnvirommentaleList
        restAnalyseEnvirommentaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyseEnvirommentale.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].businessUnit").value(hasItem(DEFAULT_BUSINESS_UNIT)))
            .andExpect(jsonPath("$.[*].activite").value(hasItem(DEFAULT_ACTIVITE)))
            .andExpect(jsonPath("$.[*].aspectEnvironnemental").value(hasItem(DEFAULT_ASPECT_ENVIRONNEMENTAL)))
            .andExpect(jsonPath("$.[*].impactEnvironnemental").value(hasItem(DEFAULT_IMPACT_ENVIRONNEMENTAL)))
            .andExpect(jsonPath("$.[*].competencesRequises").value(hasItem(DEFAULT_COMPETENCES_REQUISES)))
            .andExpect(jsonPath("$.[*].situation").value(hasItem(DEFAULT_SITUATION.toString())))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE.toString())))
            .andExpect(jsonPath("$.[*].sensibiliteMilieu").value(hasItem(DEFAULT_SENSIBILITE_MILIEU.toString())))
            .andExpect(jsonPath("$.[*].coefficientMaitrise").value(hasItem(DEFAULT_COEFFICIENT_MAITRISE.toString())))
            .andExpect(jsonPath("$.[*].gravite").value(hasItem(DEFAULT_GRAVITE.toString())))
            .andExpect(jsonPath("$.[*].criticite").value(hasItem(DEFAULT_CRITICITE)))
            .andExpect(jsonPath("$.[*].maitriseExistante").value(hasItem(DEFAULT_MAITRISE_EXISTANTE)))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }

    @Test
    @Transactional
    void getAnalyseEnvirommentale() throws Exception {
        // Initialize the database
        analyseEnvirommentaleRepository.saveAndFlush(analyseEnvirommentale);

        // Get the analyseEnvirommentale
        restAnalyseEnvirommentaleMockMvc
            .perform(get(ENTITY_API_URL_ID, analyseEnvirommentale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(analyseEnvirommentale.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.businessUnit").value(DEFAULT_BUSINESS_UNIT))
            .andExpect(jsonPath("$.activite").value(DEFAULT_ACTIVITE))
            .andExpect(jsonPath("$.aspectEnvironnemental").value(DEFAULT_ASPECT_ENVIRONNEMENTAL))
            .andExpect(jsonPath("$.impactEnvironnemental").value(DEFAULT_IMPACT_ENVIRONNEMENTAL))
            .andExpect(jsonPath("$.competencesRequises").value(DEFAULT_COMPETENCES_REQUISES))
            .andExpect(jsonPath("$.situation").value(DEFAULT_SITUATION.toString()))
            .andExpect(jsonPath("$.frequence").value(DEFAULT_FREQUENCE.toString()))
            .andExpect(jsonPath("$.sensibiliteMilieu").value(DEFAULT_SENSIBILITE_MILIEU.toString()))
            .andExpect(jsonPath("$.coefficientMaitrise").value(DEFAULT_COEFFICIENT_MAITRISE.toString()))
            .andExpect(jsonPath("$.gravite").value(DEFAULT_GRAVITE.toString()))
            .andExpect(jsonPath("$.criticite").value(DEFAULT_CRITICITE))
            .andExpect(jsonPath("$.maitriseExistante").value(DEFAULT_MAITRISE_EXISTANTE))
            .andExpect(jsonPath("$.origine").value(DEFAULT_ORIGINE));
    }

    @Test
    @Transactional
    void getNonExistingAnalyseEnvirommentale() throws Exception {
        // Get the analyseEnvirommentale
        restAnalyseEnvirommentaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnalyseEnvirommentale() throws Exception {
        // Initialize the database
        analyseEnvirommentaleRepository.saveAndFlush(analyseEnvirommentale);

        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();

        // Update the analyseEnvirommentale
        AnalyseEnvirommentale updatedAnalyseEnvirommentale = analyseEnvirommentaleRepository.findById(analyseEnvirommentale.getId()).get();
        // Disconnect from session so that the updates on updatedAnalyseEnvirommentale are not directly saved in db
        em.detach(updatedAnalyseEnvirommentale);
        updatedAnalyseEnvirommentale
            .date(UPDATED_DATE)
            .businessUnit(UPDATED_BUSINESS_UNIT)
            .activite(UPDATED_ACTIVITE)
            .aspectEnvironnemental(UPDATED_ASPECT_ENVIRONNEMENTAL)
            .impactEnvironnemental(UPDATED_IMPACT_ENVIRONNEMENTAL)
            .competencesRequises(UPDATED_COMPETENCES_REQUISES)
            .situation(UPDATED_SITUATION)
            .frequence(UPDATED_FREQUENCE)
            .sensibiliteMilieu(UPDATED_SENSIBILITE_MILIEU)
            .coefficientMaitrise(UPDATED_COEFFICIENT_MAITRISE)
            .gravite(UPDATED_GRAVITE)
            .criticite(UPDATED_CRITICITE)
            .maitriseExistante(UPDATED_MAITRISE_EXISTANTE)
            .origine(UPDATED_ORIGINE);

        restAnalyseEnvirommentaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnalyseEnvirommentale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnalyseEnvirommentale))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);
        AnalyseEnvirommentale testAnalyseEnvirommentale = analyseEnvirommentaleList.get(analyseEnvirommentaleList.size() - 1);
        assertThat(testAnalyseEnvirommentale.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnalyseEnvirommentale.getBusinessUnit()).isEqualTo(UPDATED_BUSINESS_UNIT);
        assertThat(testAnalyseEnvirommentale.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testAnalyseEnvirommentale.getAspectEnvironnemental()).isEqualTo(UPDATED_ASPECT_ENVIRONNEMENTAL);
        assertThat(testAnalyseEnvirommentale.getImpactEnvironnemental()).isEqualTo(UPDATED_IMPACT_ENVIRONNEMENTAL);
        assertThat(testAnalyseEnvirommentale.getCompetencesRequises()).isEqualTo(UPDATED_COMPETENCES_REQUISES);
        assertThat(testAnalyseEnvirommentale.getSituation()).isEqualTo(UPDATED_SITUATION);
        assertThat(testAnalyseEnvirommentale.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testAnalyseEnvirommentale.getSensibiliteMilieu()).isEqualTo(UPDATED_SENSIBILITE_MILIEU);
        assertThat(testAnalyseEnvirommentale.getCoefficientMaitrise()).isEqualTo(UPDATED_COEFFICIENT_MAITRISE);
        assertThat(testAnalyseEnvirommentale.getGravite()).isEqualTo(UPDATED_GRAVITE);
        assertThat(testAnalyseEnvirommentale.getCriticite()).isEqualTo(UPDATED_CRITICITE);
        assertThat(testAnalyseEnvirommentale.getMaitriseExistante()).isEqualTo(UPDATED_MAITRISE_EXISTANTE);
        assertThat(testAnalyseEnvirommentale.getOrigine()).isEqualTo(UPDATED_ORIGINE);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository).save(testAnalyseEnvirommentale);
    }

    @Test
    @Transactional
    void putNonExistingAnalyseEnvirommentale() throws Exception {
        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();
        analyseEnvirommentale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyseEnvirommentaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, analyseEnvirommentale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseEnvirommentale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(0)).save(analyseEnvirommentale);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnalyseEnvirommentale() throws Exception {
        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();
        analyseEnvirommentale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseEnvirommentaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseEnvirommentale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(0)).save(analyseEnvirommentale);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnalyseEnvirommentale() throws Exception {
        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();
        analyseEnvirommentale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseEnvirommentaleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseEnvirommentale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(0)).save(analyseEnvirommentale);
    }

    @Test
    @Transactional
    void partialUpdateAnalyseEnvirommentaleWithPatch() throws Exception {
        // Initialize the database
        analyseEnvirommentaleRepository.saveAndFlush(analyseEnvirommentale);

        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();

        // Update the analyseEnvirommentale using partial update
        AnalyseEnvirommentale partialUpdatedAnalyseEnvirommentale = new AnalyseEnvirommentale();
        partialUpdatedAnalyseEnvirommentale.setId(analyseEnvirommentale.getId());

        partialUpdatedAnalyseEnvirommentale
            .date(UPDATED_DATE)
            .businessUnit(UPDATED_BUSINESS_UNIT)
            .sensibiliteMilieu(UPDATED_SENSIBILITE_MILIEU)
            .maitriseExistante(UPDATED_MAITRISE_EXISTANTE);

        restAnalyseEnvirommentaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyseEnvirommentale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnalyseEnvirommentale))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);
        AnalyseEnvirommentale testAnalyseEnvirommentale = analyseEnvirommentaleList.get(analyseEnvirommentaleList.size() - 1);
        assertThat(testAnalyseEnvirommentale.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnalyseEnvirommentale.getBusinessUnit()).isEqualTo(UPDATED_BUSINESS_UNIT);
        assertThat(testAnalyseEnvirommentale.getActivite()).isEqualTo(DEFAULT_ACTIVITE);
        assertThat(testAnalyseEnvirommentale.getAspectEnvironnemental()).isEqualTo(DEFAULT_ASPECT_ENVIRONNEMENTAL);
        assertThat(testAnalyseEnvirommentale.getImpactEnvironnemental()).isEqualTo(DEFAULT_IMPACT_ENVIRONNEMENTAL);
        assertThat(testAnalyseEnvirommentale.getCompetencesRequises()).isEqualTo(DEFAULT_COMPETENCES_REQUISES);
        assertThat(testAnalyseEnvirommentale.getSituation()).isEqualTo(DEFAULT_SITUATION);
        assertThat(testAnalyseEnvirommentale.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
        assertThat(testAnalyseEnvirommentale.getSensibiliteMilieu()).isEqualTo(UPDATED_SENSIBILITE_MILIEU);
        assertThat(testAnalyseEnvirommentale.getCoefficientMaitrise()).isEqualTo(DEFAULT_COEFFICIENT_MAITRISE);
        assertThat(testAnalyseEnvirommentale.getGravite()).isEqualTo(DEFAULT_GRAVITE);
        assertThat(testAnalyseEnvirommentale.getCriticite()).isEqualTo(DEFAULT_CRITICITE);
        assertThat(testAnalyseEnvirommentale.getMaitriseExistante()).isEqualTo(UPDATED_MAITRISE_EXISTANTE);
        assertThat(testAnalyseEnvirommentale.getOrigine()).isEqualTo(DEFAULT_ORIGINE);
    }

    @Test
    @Transactional
    void fullUpdateAnalyseEnvirommentaleWithPatch() throws Exception {
        // Initialize the database
        analyseEnvirommentaleRepository.saveAndFlush(analyseEnvirommentale);

        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();

        // Update the analyseEnvirommentale using partial update
        AnalyseEnvirommentale partialUpdatedAnalyseEnvirommentale = new AnalyseEnvirommentale();
        partialUpdatedAnalyseEnvirommentale.setId(analyseEnvirommentale.getId());

        partialUpdatedAnalyseEnvirommentale
            .date(UPDATED_DATE)
            .businessUnit(UPDATED_BUSINESS_UNIT)
            .activite(UPDATED_ACTIVITE)
            .aspectEnvironnemental(UPDATED_ASPECT_ENVIRONNEMENTAL)
            .impactEnvironnemental(UPDATED_IMPACT_ENVIRONNEMENTAL)
            .competencesRequises(UPDATED_COMPETENCES_REQUISES)
            .situation(UPDATED_SITUATION)
            .frequence(UPDATED_FREQUENCE)
            .sensibiliteMilieu(UPDATED_SENSIBILITE_MILIEU)
            .coefficientMaitrise(UPDATED_COEFFICIENT_MAITRISE)
            .gravite(UPDATED_GRAVITE)
            .criticite(UPDATED_CRITICITE)
            .maitriseExistante(UPDATED_MAITRISE_EXISTANTE)
            .origine(UPDATED_ORIGINE);

        restAnalyseEnvirommentaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyseEnvirommentale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnalyseEnvirommentale))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);
        AnalyseEnvirommentale testAnalyseEnvirommentale = analyseEnvirommentaleList.get(analyseEnvirommentaleList.size() - 1);
        assertThat(testAnalyseEnvirommentale.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnalyseEnvirommentale.getBusinessUnit()).isEqualTo(UPDATED_BUSINESS_UNIT);
        assertThat(testAnalyseEnvirommentale.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testAnalyseEnvirommentale.getAspectEnvironnemental()).isEqualTo(UPDATED_ASPECT_ENVIRONNEMENTAL);
        assertThat(testAnalyseEnvirommentale.getImpactEnvironnemental()).isEqualTo(UPDATED_IMPACT_ENVIRONNEMENTAL);
        assertThat(testAnalyseEnvirommentale.getCompetencesRequises()).isEqualTo(UPDATED_COMPETENCES_REQUISES);
        assertThat(testAnalyseEnvirommentale.getSituation()).isEqualTo(UPDATED_SITUATION);
        assertThat(testAnalyseEnvirommentale.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testAnalyseEnvirommentale.getSensibiliteMilieu()).isEqualTo(UPDATED_SENSIBILITE_MILIEU);
        assertThat(testAnalyseEnvirommentale.getCoefficientMaitrise()).isEqualTo(UPDATED_COEFFICIENT_MAITRISE);
        assertThat(testAnalyseEnvirommentale.getGravite()).isEqualTo(UPDATED_GRAVITE);
        assertThat(testAnalyseEnvirommentale.getCriticite()).isEqualTo(UPDATED_CRITICITE);
        assertThat(testAnalyseEnvirommentale.getMaitriseExistante()).isEqualTo(UPDATED_MAITRISE_EXISTANTE);
        assertThat(testAnalyseEnvirommentale.getOrigine()).isEqualTo(UPDATED_ORIGINE);
    }

    @Test
    @Transactional
    void patchNonExistingAnalyseEnvirommentale() throws Exception {
        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();
        analyseEnvirommentale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyseEnvirommentaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, analyseEnvirommentale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyseEnvirommentale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(0)).save(analyseEnvirommentale);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnalyseEnvirommentale() throws Exception {
        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();
        analyseEnvirommentale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseEnvirommentaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyseEnvirommentale))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(0)).save(analyseEnvirommentale);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnalyseEnvirommentale() throws Exception {
        int databaseSizeBeforeUpdate = analyseEnvirommentaleRepository.findAll().size();
        analyseEnvirommentale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseEnvirommentaleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyseEnvirommentale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnalyseEnvirommentale in the database
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(0)).save(analyseEnvirommentale);
    }

    @Test
    @Transactional
    void deleteAnalyseEnvirommentale() throws Exception {
        // Initialize the database
        analyseEnvirommentaleRepository.saveAndFlush(analyseEnvirommentale);

        int databaseSizeBeforeDelete = analyseEnvirommentaleRepository.findAll().size();

        // Delete the analyseEnvirommentale
        restAnalyseEnvirommentaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, analyseEnvirommentale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnalyseEnvirommentale> analyseEnvirommentaleList = analyseEnvirommentaleRepository.findAll();
        assertThat(analyseEnvirommentaleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnalyseEnvirommentale in Elasticsearch
        verify(mockAnalyseEnvirommentaleSearchRepository, times(1)).deleteById(analyseEnvirommentale.getId());
    }

    @Test
    @Transactional
    void searchAnalyseEnvirommentale() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        analyseEnvirommentaleRepository.saveAndFlush(analyseEnvirommentale);
        when(
            mockAnalyseEnvirommentaleSearchRepository.search(queryStringQuery("id:" + analyseEnvirommentale.getId()), PageRequest.of(0, 20))
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(analyseEnvirommentale), PageRequest.of(0, 1), 1));

        // Search the analyseEnvirommentale
        restAnalyseEnvirommentaleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + analyseEnvirommentale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyseEnvirommentale.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].businessUnit").value(hasItem(DEFAULT_BUSINESS_UNIT)))
            .andExpect(jsonPath("$.[*].activite").value(hasItem(DEFAULT_ACTIVITE)))
            .andExpect(jsonPath("$.[*].aspectEnvironnemental").value(hasItem(DEFAULT_ASPECT_ENVIRONNEMENTAL)))
            .andExpect(jsonPath("$.[*].impactEnvironnemental").value(hasItem(DEFAULT_IMPACT_ENVIRONNEMENTAL)))
            .andExpect(jsonPath("$.[*].competencesRequises").value(hasItem(DEFAULT_COMPETENCES_REQUISES)))
            .andExpect(jsonPath("$.[*].situation").value(hasItem(DEFAULT_SITUATION.toString())))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE.toString())))
            .andExpect(jsonPath("$.[*].sensibiliteMilieu").value(hasItem(DEFAULT_SENSIBILITE_MILIEU.toString())))
            .andExpect(jsonPath("$.[*].coefficientMaitrise").value(hasItem(DEFAULT_COEFFICIENT_MAITRISE.toString())))
            .andExpect(jsonPath("$.[*].gravite").value(hasItem(DEFAULT_GRAVITE.toString())))
            .andExpect(jsonPath("$.[*].criticite").value(hasItem(DEFAULT_CRITICITE)))
            .andExpect(jsonPath("$.[*].maitriseExistante").value(hasItem(DEFAULT_MAITRISE_EXISTANTE)))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }
}
