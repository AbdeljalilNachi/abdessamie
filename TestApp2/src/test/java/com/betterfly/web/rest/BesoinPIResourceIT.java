package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.BesoinPI;
import com.betterfly.repository.BesoinPIRepository;
import com.betterfly.repository.search.BesoinPISearchRepository;
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
 * Integration tests for the {@link BesoinPIResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BesoinPIResourceIT {

    private static final LocalDate DEFAULT_DATE_IDENTIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_IDENTIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PI_PERTINENTES = "AAAAAAAAAA";
    private static final String UPDATED_PI_PERTINENTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PERTINENTE = false;
    private static final Boolean UPDATED_PERTINENTE = true;

    private static final Boolean DEFAULT_PRISE_EN_CHARGE = false;
    private static final Boolean UPDATED_PRISE_EN_CHARGE = true;

    private static final Boolean DEFAULT_AFFICHER = false;
    private static final Boolean UPDATED_AFFICHER = true;

    private static final String ENTITY_API_URL = "/api/besoin-pis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/besoin-pis";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BesoinPIRepository besoinPIRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.BesoinPISearchRepositoryMockConfiguration
     */
    @Autowired
    private BesoinPISearchRepository mockBesoinPISearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBesoinPIMockMvc;

    private BesoinPI besoinPI;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BesoinPI createEntity(EntityManager em) {
        BesoinPI besoinPI = new BesoinPI()
            .dateIdentification(DEFAULT_DATE_IDENTIFICATION)
            .piPertinentes(DEFAULT_PI_PERTINENTES)
            .pertinente(DEFAULT_PERTINENTE)
            .priseEnCharge(DEFAULT_PRISE_EN_CHARGE)
            .afficher(DEFAULT_AFFICHER);
        return besoinPI;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BesoinPI createUpdatedEntity(EntityManager em) {
        BesoinPI besoinPI = new BesoinPI()
            .dateIdentification(UPDATED_DATE_IDENTIFICATION)
            .piPertinentes(UPDATED_PI_PERTINENTES)
            .pertinente(UPDATED_PERTINENTE)
            .priseEnCharge(UPDATED_PRISE_EN_CHARGE)
            .afficher(UPDATED_AFFICHER);
        return besoinPI;
    }

    @BeforeEach
    public void initTest() {
        besoinPI = createEntity(em);
    }

    @Test
    @Transactional
    void createBesoinPI() throws Exception {
        int databaseSizeBeforeCreate = besoinPIRepository.findAll().size();
        // Create the BesoinPI
        restBesoinPIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(besoinPI)))
            .andExpect(status().isCreated());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeCreate + 1);
        BesoinPI testBesoinPI = besoinPIList.get(besoinPIList.size() - 1);
        assertThat(testBesoinPI.getDateIdentification()).isEqualTo(DEFAULT_DATE_IDENTIFICATION);
        assertThat(testBesoinPI.getPiPertinentes()).isEqualTo(DEFAULT_PI_PERTINENTES);
        assertThat(testBesoinPI.getPertinente()).isEqualTo(DEFAULT_PERTINENTE);
        assertThat(testBesoinPI.getPriseEnCharge()).isEqualTo(DEFAULT_PRISE_EN_CHARGE);
        assertThat(testBesoinPI.getAfficher()).isEqualTo(DEFAULT_AFFICHER);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(1)).save(testBesoinPI);
    }

    @Test
    @Transactional
    void createBesoinPIWithExistingId() throws Exception {
        // Create the BesoinPI with an existing ID
        besoinPI.setId(1L);

        int databaseSizeBeforeCreate = besoinPIRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBesoinPIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(besoinPI)))
            .andExpect(status().isBadRequest());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeCreate);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(0)).save(besoinPI);
    }

    @Test
    @Transactional
    void getAllBesoinPIS() throws Exception {
        // Initialize the database
        besoinPIRepository.saveAndFlush(besoinPI);

        // Get all the besoinPIList
        restBesoinPIMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(besoinPI.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIdentification").value(hasItem(DEFAULT_DATE_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].piPertinentes").value(hasItem(DEFAULT_PI_PERTINENTES)))
            .andExpect(jsonPath("$.[*].pertinente").value(hasItem(DEFAULT_PERTINENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].priseEnCharge").value(hasItem(DEFAULT_PRISE_EN_CHARGE.booleanValue())))
            .andExpect(jsonPath("$.[*].afficher").value(hasItem(DEFAULT_AFFICHER.booleanValue())));
    }

    @Test
    @Transactional
    void getBesoinPI() throws Exception {
        // Initialize the database
        besoinPIRepository.saveAndFlush(besoinPI);

        // Get the besoinPI
        restBesoinPIMockMvc
            .perform(get(ENTITY_API_URL_ID, besoinPI.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(besoinPI.getId().intValue()))
            .andExpect(jsonPath("$.dateIdentification").value(DEFAULT_DATE_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.piPertinentes").value(DEFAULT_PI_PERTINENTES))
            .andExpect(jsonPath("$.pertinente").value(DEFAULT_PERTINENTE.booleanValue()))
            .andExpect(jsonPath("$.priseEnCharge").value(DEFAULT_PRISE_EN_CHARGE.booleanValue()))
            .andExpect(jsonPath("$.afficher").value(DEFAULT_AFFICHER.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingBesoinPI() throws Exception {
        // Get the besoinPI
        restBesoinPIMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBesoinPI() throws Exception {
        // Initialize the database
        besoinPIRepository.saveAndFlush(besoinPI);

        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();

        // Update the besoinPI
        BesoinPI updatedBesoinPI = besoinPIRepository.findById(besoinPI.getId()).get();
        // Disconnect from session so that the updates on updatedBesoinPI are not directly saved in db
        em.detach(updatedBesoinPI);
        updatedBesoinPI
            .dateIdentification(UPDATED_DATE_IDENTIFICATION)
            .piPertinentes(UPDATED_PI_PERTINENTES)
            .pertinente(UPDATED_PERTINENTE)
            .priseEnCharge(UPDATED_PRISE_EN_CHARGE)
            .afficher(UPDATED_AFFICHER);

        restBesoinPIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBesoinPI.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBesoinPI))
            )
            .andExpect(status().isOk());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);
        BesoinPI testBesoinPI = besoinPIList.get(besoinPIList.size() - 1);
        assertThat(testBesoinPI.getDateIdentification()).isEqualTo(UPDATED_DATE_IDENTIFICATION);
        assertThat(testBesoinPI.getPiPertinentes()).isEqualTo(UPDATED_PI_PERTINENTES);
        assertThat(testBesoinPI.getPertinente()).isEqualTo(UPDATED_PERTINENTE);
        assertThat(testBesoinPI.getPriseEnCharge()).isEqualTo(UPDATED_PRISE_EN_CHARGE);
        assertThat(testBesoinPI.getAfficher()).isEqualTo(UPDATED_AFFICHER);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository).save(testBesoinPI);
    }

    @Test
    @Transactional
    void putNonExistingBesoinPI() throws Exception {
        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();
        besoinPI.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBesoinPIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, besoinPI.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(besoinPI))
            )
            .andExpect(status().isBadRequest());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(0)).save(besoinPI);
    }

    @Test
    @Transactional
    void putWithIdMismatchBesoinPI() throws Exception {
        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();
        besoinPI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBesoinPIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(besoinPI))
            )
            .andExpect(status().isBadRequest());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(0)).save(besoinPI);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBesoinPI() throws Exception {
        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();
        besoinPI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBesoinPIMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(besoinPI)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(0)).save(besoinPI);
    }

    @Test
    @Transactional
    void partialUpdateBesoinPIWithPatch() throws Exception {
        // Initialize the database
        besoinPIRepository.saveAndFlush(besoinPI);

        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();

        // Update the besoinPI using partial update
        BesoinPI partialUpdatedBesoinPI = new BesoinPI();
        partialUpdatedBesoinPI.setId(besoinPI.getId());

        partialUpdatedBesoinPI.piPertinentes(UPDATED_PI_PERTINENTES).pertinente(UPDATED_PERTINENTE).afficher(UPDATED_AFFICHER);

        restBesoinPIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBesoinPI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBesoinPI))
            )
            .andExpect(status().isOk());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);
        BesoinPI testBesoinPI = besoinPIList.get(besoinPIList.size() - 1);
        assertThat(testBesoinPI.getDateIdentification()).isEqualTo(DEFAULT_DATE_IDENTIFICATION);
        assertThat(testBesoinPI.getPiPertinentes()).isEqualTo(UPDATED_PI_PERTINENTES);
        assertThat(testBesoinPI.getPertinente()).isEqualTo(UPDATED_PERTINENTE);
        assertThat(testBesoinPI.getPriseEnCharge()).isEqualTo(DEFAULT_PRISE_EN_CHARGE);
        assertThat(testBesoinPI.getAfficher()).isEqualTo(UPDATED_AFFICHER);
    }

    @Test
    @Transactional
    void fullUpdateBesoinPIWithPatch() throws Exception {
        // Initialize the database
        besoinPIRepository.saveAndFlush(besoinPI);

        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();

        // Update the besoinPI using partial update
        BesoinPI partialUpdatedBesoinPI = new BesoinPI();
        partialUpdatedBesoinPI.setId(besoinPI.getId());

        partialUpdatedBesoinPI
            .dateIdentification(UPDATED_DATE_IDENTIFICATION)
            .piPertinentes(UPDATED_PI_PERTINENTES)
            .pertinente(UPDATED_PERTINENTE)
            .priseEnCharge(UPDATED_PRISE_EN_CHARGE)
            .afficher(UPDATED_AFFICHER);

        restBesoinPIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBesoinPI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBesoinPI))
            )
            .andExpect(status().isOk());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);
        BesoinPI testBesoinPI = besoinPIList.get(besoinPIList.size() - 1);
        assertThat(testBesoinPI.getDateIdentification()).isEqualTo(UPDATED_DATE_IDENTIFICATION);
        assertThat(testBesoinPI.getPiPertinentes()).isEqualTo(UPDATED_PI_PERTINENTES);
        assertThat(testBesoinPI.getPertinente()).isEqualTo(UPDATED_PERTINENTE);
        assertThat(testBesoinPI.getPriseEnCharge()).isEqualTo(UPDATED_PRISE_EN_CHARGE);
        assertThat(testBesoinPI.getAfficher()).isEqualTo(UPDATED_AFFICHER);
    }

    @Test
    @Transactional
    void patchNonExistingBesoinPI() throws Exception {
        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();
        besoinPI.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBesoinPIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, besoinPI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(besoinPI))
            )
            .andExpect(status().isBadRequest());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(0)).save(besoinPI);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBesoinPI() throws Exception {
        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();
        besoinPI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBesoinPIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(besoinPI))
            )
            .andExpect(status().isBadRequest());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(0)).save(besoinPI);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBesoinPI() throws Exception {
        int databaseSizeBeforeUpdate = besoinPIRepository.findAll().size();
        besoinPI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBesoinPIMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(besoinPI)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BesoinPI in the database
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(0)).save(besoinPI);
    }

    @Test
    @Transactional
    void deleteBesoinPI() throws Exception {
        // Initialize the database
        besoinPIRepository.saveAndFlush(besoinPI);

        int databaseSizeBeforeDelete = besoinPIRepository.findAll().size();

        // Delete the besoinPI
        restBesoinPIMockMvc
            .perform(delete(ENTITY_API_URL_ID, besoinPI.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BesoinPI> besoinPIList = besoinPIRepository.findAll();
        assertThat(besoinPIList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BesoinPI in Elasticsearch
        verify(mockBesoinPISearchRepository, times(1)).deleteById(besoinPI.getId());
    }

    @Test
    @Transactional
    void searchBesoinPI() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        besoinPIRepository.saveAndFlush(besoinPI);
        when(mockBesoinPISearchRepository.search(queryStringQuery("id:" + besoinPI.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(besoinPI), PageRequest.of(0, 1), 1));

        // Search the besoinPI
        restBesoinPIMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + besoinPI.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(besoinPI.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIdentification").value(hasItem(DEFAULT_DATE_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].piPertinentes").value(hasItem(DEFAULT_PI_PERTINENTES)))
            .andExpect(jsonPath("$.[*].pertinente").value(hasItem(DEFAULT_PERTINENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].priseEnCharge").value(hasItem(DEFAULT_PRISE_EN_CHARGE.booleanValue())))
            .andExpect(jsonPath("$.[*].afficher").value(hasItem(DEFAULT_AFFICHER.booleanValue())));
    }
}
