package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.Reclamation;
import com.betterfly.repository.ReclamationRepository;
import com.betterfly.repository.search.ReclamationSearchRepository;
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
 * Integration tests for the {@link ReclamationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReclamationResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_JUSTIFIEE = false;
    private static final Boolean UPDATED_JUSTIFIEE = true;

    private static final String DEFAULT_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PIECEJOINTE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PIECEJOINTE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PIECEJOINTE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PIECEJOINTE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ORIGINE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reclamations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/reclamations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReclamationRepository reclamationRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.ReclamationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReclamationSearchRepository mockReclamationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReclamationMockMvc;

    private Reclamation reclamation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .justifiee(DEFAULT_JUSTIFIEE)
            .client(DEFAULT_CLIENT)
            .piecejointe(DEFAULT_PIECEJOINTE)
            .piecejointeContentType(DEFAULT_PIECEJOINTE_CONTENT_TYPE)
            .origine(DEFAULT_ORIGINE);
        return reclamation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createUpdatedEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .justifiee(UPDATED_JUSTIFIEE)
            .client(UPDATED_CLIENT)
            .piecejointe(UPDATED_PIECEJOINTE)
            .piecejointeContentType(UPDATED_PIECEJOINTE_CONTENT_TYPE)
            .origine(UPDATED_ORIGINE);
        return reclamation;
    }

    @BeforeEach
    public void initTest() {
        reclamation = createEntity(em);
    }

    @Test
    @Transactional
    void createReclamation() throws Exception {
        int databaseSizeBeforeCreate = reclamationRepository.findAll().size();
        // Create the Reclamation
        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamation)))
            .andExpect(status().isCreated());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeCreate + 1);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testReclamation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReclamation.getJustifiee()).isEqualTo(DEFAULT_JUSTIFIEE);
        assertThat(testReclamation.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testReclamation.getPiecejointe()).isEqualTo(DEFAULT_PIECEJOINTE);
        assertThat(testReclamation.getPiecejointeContentType()).isEqualTo(DEFAULT_PIECEJOINTE_CONTENT_TYPE);
        assertThat(testReclamation.getOrigine()).isEqualTo(DEFAULT_ORIGINE);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(1)).save(testReclamation);
    }

    @Test
    @Transactional
    void createReclamationWithExistingId() throws Exception {
        // Create the Reclamation with an existing ID
        reclamation.setId(1L);

        int databaseSizeBeforeCreate = reclamationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamation)))
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(0)).save(reclamation);
    }

    @Test
    @Transactional
    void getAllReclamations() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamation.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].justifiee").value(hasItem(DEFAULT_JUSTIFIEE.booleanValue())))
            .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT)))
            .andExpect(jsonPath("$.[*].piecejointeContentType").value(hasItem(DEFAULT_PIECEJOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piecejointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECEJOINTE))))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }

    @Test
    @Transactional
    void getReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        // Get the reclamation
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL_ID, reclamation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reclamation.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.justifiee").value(DEFAULT_JUSTIFIEE.booleanValue()))
            .andExpect(jsonPath("$.client").value(DEFAULT_CLIENT))
            .andExpect(jsonPath("$.piecejointeContentType").value(DEFAULT_PIECEJOINTE_CONTENT_TYPE))
            .andExpect(jsonPath("$.piecejointe").value(Base64Utils.encodeToString(DEFAULT_PIECEJOINTE)))
            .andExpect(jsonPath("$.origine").value(DEFAULT_ORIGINE));
    }

    @Test
    @Transactional
    void getNonExistingReclamation() throws Exception {
        // Get the reclamation
        restReclamationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation
        Reclamation updatedReclamation = reclamationRepository.findById(reclamation.getId()).get();
        // Disconnect from session so that the updates on updatedReclamation are not directly saved in db
        em.detach(updatedReclamation);
        updatedReclamation
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .justifiee(UPDATED_JUSTIFIEE)
            .client(UPDATED_CLIENT)
            .piecejointe(UPDATED_PIECEJOINTE)
            .piecejointeContentType(UPDATED_PIECEJOINTE_CONTENT_TYPE)
            .origine(UPDATED_ORIGINE);

        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReclamation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReclamation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReclamation.getJustifiee()).isEqualTo(UPDATED_JUSTIFIEE);
        assertThat(testReclamation.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testReclamation.getPiecejointe()).isEqualTo(UPDATED_PIECEJOINTE);
        assertThat(testReclamation.getPiecejointeContentType()).isEqualTo(UPDATED_PIECEJOINTE_CONTENT_TYPE);
        assertThat(testReclamation.getOrigine()).isEqualTo(UPDATED_ORIGINE);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository).save(testReclamation);
    }

    @Test
    @Transactional
    void putNonExistingReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reclamation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(0)).save(reclamation);
    }

    @Test
    @Transactional
    void putWithIdMismatchReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(0)).save(reclamation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reclamation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(0)).save(reclamation);
    }

    @Test
    @Transactional
    void partialUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation.date(UPDATED_DATE);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReclamation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReclamation.getJustifiee()).isEqualTo(DEFAULT_JUSTIFIEE);
        assertThat(testReclamation.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testReclamation.getPiecejointe()).isEqualTo(DEFAULT_PIECEJOINTE);
        assertThat(testReclamation.getPiecejointeContentType()).isEqualTo(DEFAULT_PIECEJOINTE_CONTENT_TYPE);
        assertThat(testReclamation.getOrigine()).isEqualTo(DEFAULT_ORIGINE);
    }

    @Test
    @Transactional
    void fullUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .justifiee(UPDATED_JUSTIFIEE)
            .client(UPDATED_CLIENT)
            .piecejointe(UPDATED_PIECEJOINTE)
            .piecejointeContentType(UPDATED_PIECEJOINTE_CONTENT_TYPE)
            .origine(UPDATED_ORIGINE);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);
        Reclamation testReclamation = reclamationList.get(reclamationList.size() - 1);
        assertThat(testReclamation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReclamation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReclamation.getJustifiee()).isEqualTo(UPDATED_JUSTIFIEE);
        assertThat(testReclamation.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testReclamation.getPiecejointe()).isEqualTo(UPDATED_PIECEJOINTE);
        assertThat(testReclamation.getPiecejointeContentType()).isEqualTo(UPDATED_PIECEJOINTE_CONTENT_TYPE);
        assertThat(testReclamation.getOrigine()).isEqualTo(UPDATED_ORIGINE);
    }

    @Test
    @Transactional
    void patchNonExistingReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(0)).save(reclamation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(0)).save(reclamation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReclamation() throws Exception {
        int databaseSizeBeforeUpdate = reclamationRepository.findAll().size();
        reclamation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reclamation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(0)).save(reclamation);
    }

    @Test
    @Transactional
    void deleteReclamation() throws Exception {
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);

        int databaseSizeBeforeDelete = reclamationRepository.findAll().size();

        // Delete the reclamation
        restReclamationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reclamation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        assertThat(reclamationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Reclamation in Elasticsearch
        verify(mockReclamationSearchRepository, times(1)).deleteById(reclamation.getId());
    }

    @Test
    @Transactional
    void searchReclamation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reclamationRepository.saveAndFlush(reclamation);
        when(mockReclamationSearchRepository.search(queryStringQuery("id:" + reclamation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reclamation), PageRequest.of(0, 1), 1));

        // Search the reclamation
        restReclamationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reclamation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamation.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].justifiee").value(hasItem(DEFAULT_JUSTIFIEE.booleanValue())))
            .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT)))
            .andExpect(jsonPath("$.[*].piecejointeContentType").value(hasItem(DEFAULT_PIECEJOINTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].piecejointe").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIECEJOINTE))))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }
}
