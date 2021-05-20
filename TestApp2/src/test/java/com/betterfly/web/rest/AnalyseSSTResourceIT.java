package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.AnalyseSST;
import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.Situation;
import com.betterfly.repository.AnalyseSSTRepository;
import com.betterfly.repository.search.AnalyseSSTSearchRepository;
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
 * Integration tests for the {@link AnalyseSSTResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AnalyseSSTResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BUISNESS_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_BUISNESS_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_UNITE_TRAVAIL = "AAAAAAAAAA";
    private static final String UPDATED_UNITE_TRAVAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DANGER = "AAAAAAAAAA";
    private static final String UPDATED_DANGER = "BBBBBBBBBB";

    private static final String DEFAULT_RISQUE = "AAAAAAAAAA";
    private static final String UPDATED_RISQUE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPETENCE = "AAAAAAAAAA";
    private static final String UPDATED_COMPETENCE = "BBBBBBBBBB";

    private static final Situation DEFAULT_SITUATION = Situation.Normale;
    private static final Situation UPDATED_SITUATION = Situation.Anormale;

    private static final EnumFive DEFAULT_FREQUENCE = EnumFive.ONE;
    private static final EnumFive UPDATED_FREQUENCE = EnumFive.TWO;

    private static final EnumFive DEFAULT_DUREE_EXPOSITION = EnumFive.ONE;
    private static final EnumFive UPDATED_DUREE_EXPOSITION = EnumFive.TWO;

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

    private static final String ENTITY_API_URL = "/api/analyse-ssts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/analyse-ssts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnalyseSSTRepository analyseSSTRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.AnalyseSSTSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnalyseSSTSearchRepository mockAnalyseSSTSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnalyseSSTMockMvc;

    private AnalyseSST analyseSST;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalyseSST createEntity(EntityManager em) {
        AnalyseSST analyseSST = new AnalyseSST()
            .date(DEFAULT_DATE)
            .buisnessUnit(DEFAULT_BUISNESS_UNIT)
            .uniteTravail(DEFAULT_UNITE_TRAVAIL)
            .danger(DEFAULT_DANGER)
            .risque(DEFAULT_RISQUE)
            .competence(DEFAULT_COMPETENCE)
            .situation(DEFAULT_SITUATION)
            .frequence(DEFAULT_FREQUENCE)
            .dureeExposition(DEFAULT_DUREE_EXPOSITION)
            .coefficientMaitrise(DEFAULT_COEFFICIENT_MAITRISE)
            .gravite(DEFAULT_GRAVITE)
            .criticite(DEFAULT_CRITICITE)
            .maitriseExistante(DEFAULT_MAITRISE_EXISTANTE)
            .origine(DEFAULT_ORIGINE);
        return analyseSST;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalyseSST createUpdatedEntity(EntityManager em) {
        AnalyseSST analyseSST = new AnalyseSST()
            .date(UPDATED_DATE)
            .buisnessUnit(UPDATED_BUISNESS_UNIT)
            .uniteTravail(UPDATED_UNITE_TRAVAIL)
            .danger(UPDATED_DANGER)
            .risque(UPDATED_RISQUE)
            .competence(UPDATED_COMPETENCE)
            .situation(UPDATED_SITUATION)
            .frequence(UPDATED_FREQUENCE)
            .dureeExposition(UPDATED_DUREE_EXPOSITION)
            .coefficientMaitrise(UPDATED_COEFFICIENT_MAITRISE)
            .gravite(UPDATED_GRAVITE)
            .criticite(UPDATED_CRITICITE)
            .maitriseExistante(UPDATED_MAITRISE_EXISTANTE)
            .origine(UPDATED_ORIGINE);
        return analyseSST;
    }

    @BeforeEach
    public void initTest() {
        analyseSST = createEntity(em);
    }

    @Test
    @Transactional
    void createAnalyseSST() throws Exception {
        int databaseSizeBeforeCreate = analyseSSTRepository.findAll().size();
        // Create the AnalyseSST
        restAnalyseSSTMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(analyseSST)))
            .andExpect(status().isCreated());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeCreate + 1);
        AnalyseSST testAnalyseSST = analyseSSTList.get(analyseSSTList.size() - 1);
        assertThat(testAnalyseSST.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAnalyseSST.getBuisnessUnit()).isEqualTo(DEFAULT_BUISNESS_UNIT);
        assertThat(testAnalyseSST.getUniteTravail()).isEqualTo(DEFAULT_UNITE_TRAVAIL);
        assertThat(testAnalyseSST.getDanger()).isEqualTo(DEFAULT_DANGER);
        assertThat(testAnalyseSST.getRisque()).isEqualTo(DEFAULT_RISQUE);
        assertThat(testAnalyseSST.getCompetence()).isEqualTo(DEFAULT_COMPETENCE);
        assertThat(testAnalyseSST.getSituation()).isEqualTo(DEFAULT_SITUATION);
        assertThat(testAnalyseSST.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
        assertThat(testAnalyseSST.getDureeExposition()).isEqualTo(DEFAULT_DUREE_EXPOSITION);
        assertThat(testAnalyseSST.getCoefficientMaitrise()).isEqualTo(DEFAULT_COEFFICIENT_MAITRISE);
        assertThat(testAnalyseSST.getGravite()).isEqualTo(DEFAULT_GRAVITE);
        assertThat(testAnalyseSST.getCriticite()).isEqualTo(DEFAULT_CRITICITE);
        assertThat(testAnalyseSST.getMaitriseExistante()).isEqualTo(DEFAULT_MAITRISE_EXISTANTE);
        assertThat(testAnalyseSST.getOrigine()).isEqualTo(DEFAULT_ORIGINE);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(1)).save(testAnalyseSST);
    }

    @Test
    @Transactional
    void createAnalyseSSTWithExistingId() throws Exception {
        // Create the AnalyseSST with an existing ID
        analyseSST.setId(1L);

        int databaseSizeBeforeCreate = analyseSSTRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalyseSSTMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(analyseSST)))
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(0)).save(analyseSST);
    }

    @Test
    @Transactional
    void getAllAnalyseSSTS() throws Exception {
        // Initialize the database
        analyseSSTRepository.saveAndFlush(analyseSST);

        // Get all the analyseSSTList
        restAnalyseSSTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyseSST.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].buisnessUnit").value(hasItem(DEFAULT_BUISNESS_UNIT)))
            .andExpect(jsonPath("$.[*].uniteTravail").value(hasItem(DEFAULT_UNITE_TRAVAIL)))
            .andExpect(jsonPath("$.[*].danger").value(hasItem(DEFAULT_DANGER)))
            .andExpect(jsonPath("$.[*].risque").value(hasItem(DEFAULT_RISQUE)))
            .andExpect(jsonPath("$.[*].competence").value(hasItem(DEFAULT_COMPETENCE)))
            .andExpect(jsonPath("$.[*].situation").value(hasItem(DEFAULT_SITUATION.toString())))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE.toString())))
            .andExpect(jsonPath("$.[*].dureeExposition").value(hasItem(DEFAULT_DUREE_EXPOSITION.toString())))
            .andExpect(jsonPath("$.[*].coefficientMaitrise").value(hasItem(DEFAULT_COEFFICIENT_MAITRISE.toString())))
            .andExpect(jsonPath("$.[*].gravite").value(hasItem(DEFAULT_GRAVITE.toString())))
            .andExpect(jsonPath("$.[*].criticite").value(hasItem(DEFAULT_CRITICITE)))
            .andExpect(jsonPath("$.[*].maitriseExistante").value(hasItem(DEFAULT_MAITRISE_EXISTANTE)))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }

    @Test
    @Transactional
    void getAnalyseSST() throws Exception {
        // Initialize the database
        analyseSSTRepository.saveAndFlush(analyseSST);

        // Get the analyseSST
        restAnalyseSSTMockMvc
            .perform(get(ENTITY_API_URL_ID, analyseSST.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(analyseSST.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.buisnessUnit").value(DEFAULT_BUISNESS_UNIT))
            .andExpect(jsonPath("$.uniteTravail").value(DEFAULT_UNITE_TRAVAIL))
            .andExpect(jsonPath("$.danger").value(DEFAULT_DANGER))
            .andExpect(jsonPath("$.risque").value(DEFAULT_RISQUE))
            .andExpect(jsonPath("$.competence").value(DEFAULT_COMPETENCE))
            .andExpect(jsonPath("$.situation").value(DEFAULT_SITUATION.toString()))
            .andExpect(jsonPath("$.frequence").value(DEFAULT_FREQUENCE.toString()))
            .andExpect(jsonPath("$.dureeExposition").value(DEFAULT_DUREE_EXPOSITION.toString()))
            .andExpect(jsonPath("$.coefficientMaitrise").value(DEFAULT_COEFFICIENT_MAITRISE.toString()))
            .andExpect(jsonPath("$.gravite").value(DEFAULT_GRAVITE.toString()))
            .andExpect(jsonPath("$.criticite").value(DEFAULT_CRITICITE))
            .andExpect(jsonPath("$.maitriseExistante").value(DEFAULT_MAITRISE_EXISTANTE))
            .andExpect(jsonPath("$.origine").value(DEFAULT_ORIGINE));
    }

    @Test
    @Transactional
    void getNonExistingAnalyseSST() throws Exception {
        // Get the analyseSST
        restAnalyseSSTMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnalyseSST() throws Exception {
        // Initialize the database
        analyseSSTRepository.saveAndFlush(analyseSST);

        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();

        // Update the analyseSST
        AnalyseSST updatedAnalyseSST = analyseSSTRepository.findById(analyseSST.getId()).get();
        // Disconnect from session so that the updates on updatedAnalyseSST are not directly saved in db
        em.detach(updatedAnalyseSST);
        updatedAnalyseSST
            .date(UPDATED_DATE)
            .buisnessUnit(UPDATED_BUISNESS_UNIT)
            .uniteTravail(UPDATED_UNITE_TRAVAIL)
            .danger(UPDATED_DANGER)
            .risque(UPDATED_RISQUE)
            .competence(UPDATED_COMPETENCE)
            .situation(UPDATED_SITUATION)
            .frequence(UPDATED_FREQUENCE)
            .dureeExposition(UPDATED_DUREE_EXPOSITION)
            .coefficientMaitrise(UPDATED_COEFFICIENT_MAITRISE)
            .gravite(UPDATED_GRAVITE)
            .criticite(UPDATED_CRITICITE)
            .maitriseExistante(UPDATED_MAITRISE_EXISTANTE)
            .origine(UPDATED_ORIGINE);

        restAnalyseSSTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnalyseSST.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnalyseSST))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);
        AnalyseSST testAnalyseSST = analyseSSTList.get(analyseSSTList.size() - 1);
        assertThat(testAnalyseSST.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnalyseSST.getBuisnessUnit()).isEqualTo(UPDATED_BUISNESS_UNIT);
        assertThat(testAnalyseSST.getUniteTravail()).isEqualTo(UPDATED_UNITE_TRAVAIL);
        assertThat(testAnalyseSST.getDanger()).isEqualTo(UPDATED_DANGER);
        assertThat(testAnalyseSST.getRisque()).isEqualTo(UPDATED_RISQUE);
        assertThat(testAnalyseSST.getCompetence()).isEqualTo(UPDATED_COMPETENCE);
        assertThat(testAnalyseSST.getSituation()).isEqualTo(UPDATED_SITUATION);
        assertThat(testAnalyseSST.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testAnalyseSST.getDureeExposition()).isEqualTo(UPDATED_DUREE_EXPOSITION);
        assertThat(testAnalyseSST.getCoefficientMaitrise()).isEqualTo(UPDATED_COEFFICIENT_MAITRISE);
        assertThat(testAnalyseSST.getGravite()).isEqualTo(UPDATED_GRAVITE);
        assertThat(testAnalyseSST.getCriticite()).isEqualTo(UPDATED_CRITICITE);
        assertThat(testAnalyseSST.getMaitriseExistante()).isEqualTo(UPDATED_MAITRISE_EXISTANTE);
        assertThat(testAnalyseSST.getOrigine()).isEqualTo(UPDATED_ORIGINE);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository).save(testAnalyseSST);
    }

    @Test
    @Transactional
    void putNonExistingAnalyseSST() throws Exception {
        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();
        analyseSST.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyseSSTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, analyseSST.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseSST))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(0)).save(analyseSST);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnalyseSST() throws Exception {
        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();
        analyseSST.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseSSTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(analyseSST))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(0)).save(analyseSST);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnalyseSST() throws Exception {
        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();
        analyseSST.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseSSTMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(analyseSST)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(0)).save(analyseSST);
    }

    @Test
    @Transactional
    void partialUpdateAnalyseSSTWithPatch() throws Exception {
        // Initialize the database
        analyseSSTRepository.saveAndFlush(analyseSST);

        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();

        // Update the analyseSST using partial update
        AnalyseSST partialUpdatedAnalyseSST = new AnalyseSST();
        partialUpdatedAnalyseSST.setId(analyseSST.getId());

        partialUpdatedAnalyseSST
            .date(UPDATED_DATE)
            .danger(UPDATED_DANGER)
            .competence(UPDATED_COMPETENCE)
            .situation(UPDATED_SITUATION)
            .gravite(UPDATED_GRAVITE)
            .origine(UPDATED_ORIGINE);

        restAnalyseSSTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyseSST.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnalyseSST))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);
        AnalyseSST testAnalyseSST = analyseSSTList.get(analyseSSTList.size() - 1);
        assertThat(testAnalyseSST.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnalyseSST.getBuisnessUnit()).isEqualTo(DEFAULT_BUISNESS_UNIT);
        assertThat(testAnalyseSST.getUniteTravail()).isEqualTo(DEFAULT_UNITE_TRAVAIL);
        assertThat(testAnalyseSST.getDanger()).isEqualTo(UPDATED_DANGER);
        assertThat(testAnalyseSST.getRisque()).isEqualTo(DEFAULT_RISQUE);
        assertThat(testAnalyseSST.getCompetence()).isEqualTo(UPDATED_COMPETENCE);
        assertThat(testAnalyseSST.getSituation()).isEqualTo(UPDATED_SITUATION);
        assertThat(testAnalyseSST.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
        assertThat(testAnalyseSST.getDureeExposition()).isEqualTo(DEFAULT_DUREE_EXPOSITION);
        assertThat(testAnalyseSST.getCoefficientMaitrise()).isEqualTo(DEFAULT_COEFFICIENT_MAITRISE);
        assertThat(testAnalyseSST.getGravite()).isEqualTo(UPDATED_GRAVITE);
        assertThat(testAnalyseSST.getCriticite()).isEqualTo(DEFAULT_CRITICITE);
        assertThat(testAnalyseSST.getMaitriseExistante()).isEqualTo(DEFAULT_MAITRISE_EXISTANTE);
        assertThat(testAnalyseSST.getOrigine()).isEqualTo(UPDATED_ORIGINE);
    }

    @Test
    @Transactional
    void fullUpdateAnalyseSSTWithPatch() throws Exception {
        // Initialize the database
        analyseSSTRepository.saveAndFlush(analyseSST);

        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();

        // Update the analyseSST using partial update
        AnalyseSST partialUpdatedAnalyseSST = new AnalyseSST();
        partialUpdatedAnalyseSST.setId(analyseSST.getId());

        partialUpdatedAnalyseSST
            .date(UPDATED_DATE)
            .buisnessUnit(UPDATED_BUISNESS_UNIT)
            .uniteTravail(UPDATED_UNITE_TRAVAIL)
            .danger(UPDATED_DANGER)
            .risque(UPDATED_RISQUE)
            .competence(UPDATED_COMPETENCE)
            .situation(UPDATED_SITUATION)
            .frequence(UPDATED_FREQUENCE)
            .dureeExposition(UPDATED_DUREE_EXPOSITION)
            .coefficientMaitrise(UPDATED_COEFFICIENT_MAITRISE)
            .gravite(UPDATED_GRAVITE)
            .criticite(UPDATED_CRITICITE)
            .maitriseExistante(UPDATED_MAITRISE_EXISTANTE)
            .origine(UPDATED_ORIGINE);

        restAnalyseSSTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyseSST.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnalyseSST))
            )
            .andExpect(status().isOk());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);
        AnalyseSST testAnalyseSST = analyseSSTList.get(analyseSSTList.size() - 1);
        assertThat(testAnalyseSST.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAnalyseSST.getBuisnessUnit()).isEqualTo(UPDATED_BUISNESS_UNIT);
        assertThat(testAnalyseSST.getUniteTravail()).isEqualTo(UPDATED_UNITE_TRAVAIL);
        assertThat(testAnalyseSST.getDanger()).isEqualTo(UPDATED_DANGER);
        assertThat(testAnalyseSST.getRisque()).isEqualTo(UPDATED_RISQUE);
        assertThat(testAnalyseSST.getCompetence()).isEqualTo(UPDATED_COMPETENCE);
        assertThat(testAnalyseSST.getSituation()).isEqualTo(UPDATED_SITUATION);
        assertThat(testAnalyseSST.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testAnalyseSST.getDureeExposition()).isEqualTo(UPDATED_DUREE_EXPOSITION);
        assertThat(testAnalyseSST.getCoefficientMaitrise()).isEqualTo(UPDATED_COEFFICIENT_MAITRISE);
        assertThat(testAnalyseSST.getGravite()).isEqualTo(UPDATED_GRAVITE);
        assertThat(testAnalyseSST.getCriticite()).isEqualTo(UPDATED_CRITICITE);
        assertThat(testAnalyseSST.getMaitriseExistante()).isEqualTo(UPDATED_MAITRISE_EXISTANTE);
        assertThat(testAnalyseSST.getOrigine()).isEqualTo(UPDATED_ORIGINE);
    }

    @Test
    @Transactional
    void patchNonExistingAnalyseSST() throws Exception {
        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();
        analyseSST.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyseSSTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, analyseSST.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyseSST))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(0)).save(analyseSST);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnalyseSST() throws Exception {
        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();
        analyseSST.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseSSTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(analyseSST))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(0)).save(analyseSST);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnalyseSST() throws Exception {
        int databaseSizeBeforeUpdate = analyseSSTRepository.findAll().size();
        analyseSST.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyseSSTMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(analyseSST))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnalyseSST in the database
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(0)).save(analyseSST);
    }

    @Test
    @Transactional
    void deleteAnalyseSST() throws Exception {
        // Initialize the database
        analyseSSTRepository.saveAndFlush(analyseSST);

        int databaseSizeBeforeDelete = analyseSSTRepository.findAll().size();

        // Delete the analyseSST
        restAnalyseSSTMockMvc
            .perform(delete(ENTITY_API_URL_ID, analyseSST.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnalyseSST> analyseSSTList = analyseSSTRepository.findAll();
        assertThat(analyseSSTList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnalyseSST in Elasticsearch
        verify(mockAnalyseSSTSearchRepository, times(1)).deleteById(analyseSST.getId());
    }

    @Test
    @Transactional
    void searchAnalyseSST() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        analyseSSTRepository.saveAndFlush(analyseSST);
        when(mockAnalyseSSTSearchRepository.search(queryStringQuery("id:" + analyseSST.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(analyseSST), PageRequest.of(0, 1), 1));

        // Search the analyseSST
        restAnalyseSSTMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + analyseSST.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyseSST.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].buisnessUnit").value(hasItem(DEFAULT_BUISNESS_UNIT)))
            .andExpect(jsonPath("$.[*].uniteTravail").value(hasItem(DEFAULT_UNITE_TRAVAIL)))
            .andExpect(jsonPath("$.[*].danger").value(hasItem(DEFAULT_DANGER)))
            .andExpect(jsonPath("$.[*].risque").value(hasItem(DEFAULT_RISQUE)))
            .andExpect(jsonPath("$.[*].competence").value(hasItem(DEFAULT_COMPETENCE)))
            .andExpect(jsonPath("$.[*].situation").value(hasItem(DEFAULT_SITUATION.toString())))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE.toString())))
            .andExpect(jsonPath("$.[*].dureeExposition").value(hasItem(DEFAULT_DUREE_EXPOSITION.toString())))
            .andExpect(jsonPath("$.[*].coefficientMaitrise").value(hasItem(DEFAULT_COEFFICIENT_MAITRISE.toString())))
            .andExpect(jsonPath("$.[*].gravite").value(hasItem(DEFAULT_GRAVITE.toString())))
            .andExpect(jsonPath("$.[*].criticite").value(hasItem(DEFAULT_CRITICITE)))
            .andExpect(jsonPath("$.[*].maitriseExistante").value(hasItem(DEFAULT_MAITRISE_EXISTANTE)))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }
}
