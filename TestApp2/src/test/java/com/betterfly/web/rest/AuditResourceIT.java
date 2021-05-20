package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.Audit;
import com.betterfly.domain.enumeration.Standard;
import com.betterfly.domain.enumeration.StatutAudit;
import com.betterfly.domain.enumeration.TypeAudit;
import com.betterfly.repository.AuditRepository;
import com.betterfly.repository.search.AuditSearchRepository;
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
 * Integration tests for the {@link AuditResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AuditResourceIT {

    private static final LocalDate DEFAULT_DATE_AUDIT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_AUDIT = LocalDate.now(ZoneId.systemDefault());

    private static final TypeAudit DEFAULT_TYPE_AUDIT = TypeAudit.Interne;
    private static final TypeAudit UPDATED_TYPE_AUDIT = TypeAudit.Externe;

    private static final Standard DEFAULT_STANDARD = Standard.ISO9001;
    private static final Standard UPDATED_STANDARD = Standard.ISO14001;

    private static final String DEFAULT_I_D = "AAAAAAAAAA";
    private static final String UPDATED_I_D = "BBBBBBBBBB";

    private static final StatutAudit DEFAULT_STATUT = StatutAudit.Retard;
    private static final StatutAudit UPDATED_STATUT = StatutAudit.REALISE;

    private static final String DEFAULT_CONCLUSION = "AAAAAAAAAA";
    private static final String UPDATED_CONCLUSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/audits";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuditRepository auditRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.AuditSearchRepositoryMockConfiguration
     */
    @Autowired
    private AuditSearchRepository mockAuditSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditMockMvc;

    private Audit audit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audit createEntity(EntityManager em) {
        Audit audit = new Audit()
            .dateAudit(DEFAULT_DATE_AUDIT)
            .typeAudit(DEFAULT_TYPE_AUDIT)
            .standard(DEFAULT_STANDARD)
            .iD(DEFAULT_I_D)
            .statut(DEFAULT_STATUT)
            .conclusion(DEFAULT_CONCLUSION);
        return audit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audit createUpdatedEntity(EntityManager em) {
        Audit audit = new Audit()
            .dateAudit(UPDATED_DATE_AUDIT)
            .typeAudit(UPDATED_TYPE_AUDIT)
            .standard(UPDATED_STANDARD)
            .iD(UPDATED_I_D)
            .statut(UPDATED_STATUT)
            .conclusion(UPDATED_CONCLUSION);
        return audit;
    }

    @BeforeEach
    public void initTest() {
        audit = createEntity(em);
    }

    @Test
    @Transactional
    void createAudit() throws Exception {
        int databaseSizeBeforeCreate = auditRepository.findAll().size();
        // Create the Audit
        restAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isCreated());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeCreate + 1);
        Audit testAudit = auditList.get(auditList.size() - 1);
        assertThat(testAudit.getDateAudit()).isEqualTo(DEFAULT_DATE_AUDIT);
        assertThat(testAudit.getTypeAudit()).isEqualTo(DEFAULT_TYPE_AUDIT);
        assertThat(testAudit.getStandard()).isEqualTo(DEFAULT_STANDARD);
        assertThat(testAudit.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testAudit.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testAudit.getConclusion()).isEqualTo(DEFAULT_CONCLUSION);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(1)).save(testAudit);
    }

    @Test
    @Transactional
    void createAuditWithExistingId() throws Exception {
        // Create the Audit with an existing ID
        audit.setId(1L);

        int databaseSizeBeforeCreate = auditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeCreate);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(0)).save(audit);
    }

    @Test
    @Transactional
    void getAllAudits() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList
        restAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audit.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAudit").value(hasItem(DEFAULT_DATE_AUDIT.toString())))
            .andExpect(jsonPath("$.[*].typeAudit").value(hasItem(DEFAULT_TYPE_AUDIT.toString())))
            .andExpect(jsonPath("$.[*].standard").value(hasItem(DEFAULT_STANDARD.toString())))
            .andExpect(jsonPath("$.[*].iD").value(hasItem(DEFAULT_I_D)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].conclusion").value(hasItem(DEFAULT_CONCLUSION)));
    }

    @Test
    @Transactional
    void getAudit() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get the audit
        restAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, audit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(audit.getId().intValue()))
            .andExpect(jsonPath("$.dateAudit").value(DEFAULT_DATE_AUDIT.toString()))
            .andExpect(jsonPath("$.typeAudit").value(DEFAULT_TYPE_AUDIT.toString()))
            .andExpect(jsonPath("$.standard").value(DEFAULT_STANDARD.toString()))
            .andExpect(jsonPath("$.iD").value(DEFAULT_I_D))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.conclusion").value(DEFAULT_CONCLUSION));
    }

    @Test
    @Transactional
    void getNonExistingAudit() throws Exception {
        // Get the audit
        restAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAudit() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        int databaseSizeBeforeUpdate = auditRepository.findAll().size();

        // Update the audit
        Audit updatedAudit = auditRepository.findById(audit.getId()).get();
        // Disconnect from session so that the updates on updatedAudit are not directly saved in db
        em.detach(updatedAudit);
        updatedAudit
            .dateAudit(UPDATED_DATE_AUDIT)
            .typeAudit(UPDATED_TYPE_AUDIT)
            .standard(UPDATED_STANDARD)
            .iD(UPDATED_I_D)
            .statut(UPDATED_STATUT)
            .conclusion(UPDATED_CONCLUSION);

        restAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAudit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAudit))
            )
            .andExpect(status().isOk());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);
        Audit testAudit = auditList.get(auditList.size() - 1);
        assertThat(testAudit.getDateAudit()).isEqualTo(UPDATED_DATE_AUDIT);
        assertThat(testAudit.getTypeAudit()).isEqualTo(UPDATED_TYPE_AUDIT);
        assertThat(testAudit.getStandard()).isEqualTo(UPDATED_STANDARD);
        assertThat(testAudit.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testAudit.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAudit.getConclusion()).isEqualTo(UPDATED_CONCLUSION);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository).save(testAudit);
    }

    @Test
    @Transactional
    void putNonExistingAudit() throws Exception {
        int databaseSizeBeforeUpdate = auditRepository.findAll().size();
        audit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, audit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(audit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(0)).save(audit);
    }

    @Test
    @Transactional
    void putWithIdMismatchAudit() throws Exception {
        int databaseSizeBeforeUpdate = auditRepository.findAll().size();
        audit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(audit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(0)).save(audit);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAudit() throws Exception {
        int databaseSizeBeforeUpdate = auditRepository.findAll().size();
        audit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(0)).save(audit);
    }

    @Test
    @Transactional
    void partialUpdateAuditWithPatch() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        int databaseSizeBeforeUpdate = auditRepository.findAll().size();

        // Update the audit using partial update
        Audit partialUpdatedAudit = new Audit();
        partialUpdatedAudit.setId(audit.getId());

        partialUpdatedAudit.statut(UPDATED_STATUT).conclusion(UPDATED_CONCLUSION);

        restAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAudit))
            )
            .andExpect(status().isOk());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);
        Audit testAudit = auditList.get(auditList.size() - 1);
        assertThat(testAudit.getDateAudit()).isEqualTo(DEFAULT_DATE_AUDIT);
        assertThat(testAudit.getTypeAudit()).isEqualTo(DEFAULT_TYPE_AUDIT);
        assertThat(testAudit.getStandard()).isEqualTo(DEFAULT_STANDARD);
        assertThat(testAudit.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testAudit.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAudit.getConclusion()).isEqualTo(UPDATED_CONCLUSION);
    }

    @Test
    @Transactional
    void fullUpdateAuditWithPatch() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        int databaseSizeBeforeUpdate = auditRepository.findAll().size();

        // Update the audit using partial update
        Audit partialUpdatedAudit = new Audit();
        partialUpdatedAudit.setId(audit.getId());

        partialUpdatedAudit
            .dateAudit(UPDATED_DATE_AUDIT)
            .typeAudit(UPDATED_TYPE_AUDIT)
            .standard(UPDATED_STANDARD)
            .iD(UPDATED_I_D)
            .statut(UPDATED_STATUT)
            .conclusion(UPDATED_CONCLUSION);

        restAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAudit))
            )
            .andExpect(status().isOk());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);
        Audit testAudit = auditList.get(auditList.size() - 1);
        assertThat(testAudit.getDateAudit()).isEqualTo(UPDATED_DATE_AUDIT);
        assertThat(testAudit.getTypeAudit()).isEqualTo(UPDATED_TYPE_AUDIT);
        assertThat(testAudit.getStandard()).isEqualTo(UPDATED_STANDARD);
        assertThat(testAudit.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testAudit.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAudit.getConclusion()).isEqualTo(UPDATED_CONCLUSION);
    }

    @Test
    @Transactional
    void patchNonExistingAudit() throws Exception {
        int databaseSizeBeforeUpdate = auditRepository.findAll().size();
        audit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, audit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(audit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(0)).save(audit);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAudit() throws Exception {
        int databaseSizeBeforeUpdate = auditRepository.findAll().size();
        audit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(audit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(0)).save(audit);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAudit() throws Exception {
        int databaseSizeBeforeUpdate = auditRepository.findAll().size();
        audit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(0)).save(audit);
    }

    @Test
    @Transactional
    void deleteAudit() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        int databaseSizeBeforeDelete = auditRepository.findAll().size();

        // Delete the audit
        restAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, audit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Audit in Elasticsearch
        verify(mockAuditSearchRepository, times(1)).deleteById(audit.getId());
    }

    @Test
    @Transactional
    void searchAudit() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        auditRepository.saveAndFlush(audit);
        when(mockAuditSearchRepository.search(queryStringQuery("id:" + audit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(audit), PageRequest.of(0, 1), 1));

        // Search the audit
        restAuditMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + audit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audit.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAudit").value(hasItem(DEFAULT_DATE_AUDIT.toString())))
            .andExpect(jsonPath("$.[*].typeAudit").value(hasItem(DEFAULT_TYPE_AUDIT.toString())))
            .andExpect(jsonPath("$.[*].standard").value(hasItem(DEFAULT_STANDARD.toString())))
            .andExpect(jsonPath("$.[*].iD").value(hasItem(DEFAULT_I_D)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].conclusion").value(hasItem(DEFAULT_CONCLUSION)));
    }
}
