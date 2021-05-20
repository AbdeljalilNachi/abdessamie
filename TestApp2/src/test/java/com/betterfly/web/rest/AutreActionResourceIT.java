package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.AutreAction;
import com.betterfly.repository.AutreActionRepository;
import com.betterfly.repository.search.AutreActionSearchRepository;
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
 * Integration tests for the {@link AutreActionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AutreActionResourceIT {

    private static final String DEFAULT_ORIGINE_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINE_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/autre-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/autre-actions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AutreActionRepository autreActionRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.AutreActionSearchRepositoryMockConfiguration
     */
    @Autowired
    private AutreActionSearchRepository mockAutreActionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutreActionMockMvc;

    private AutreAction autreAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutreAction createEntity(EntityManager em) {
        AutreAction autreAction = new AutreAction().origineAction(DEFAULT_ORIGINE_ACTION).origine(DEFAULT_ORIGINE);
        return autreAction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutreAction createUpdatedEntity(EntityManager em) {
        AutreAction autreAction = new AutreAction().origineAction(UPDATED_ORIGINE_ACTION).origine(UPDATED_ORIGINE);
        return autreAction;
    }

    @BeforeEach
    public void initTest() {
        autreAction = createEntity(em);
    }

    @Test
    @Transactional
    void createAutreAction() throws Exception {
        int databaseSizeBeforeCreate = autreActionRepository.findAll().size();
        // Create the AutreAction
        restAutreActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autreAction)))
            .andExpect(status().isCreated());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeCreate + 1);
        AutreAction testAutreAction = autreActionList.get(autreActionList.size() - 1);
        assertThat(testAutreAction.getOrigineAction()).isEqualTo(DEFAULT_ORIGINE_ACTION);
        assertThat(testAutreAction.getOrigine()).isEqualTo(DEFAULT_ORIGINE);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(1)).save(testAutreAction);
    }

    @Test
    @Transactional
    void createAutreActionWithExistingId() throws Exception {
        // Create the AutreAction with an existing ID
        autreAction.setId(1L);

        int databaseSizeBeforeCreate = autreActionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutreActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autreAction)))
            .andExpect(status().isBadRequest());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeCreate);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(0)).save(autreAction);
    }

    @Test
    @Transactional
    void getAllAutreActions() throws Exception {
        // Initialize the database
        autreActionRepository.saveAndFlush(autreAction);

        // Get all the autreActionList
        restAutreActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autreAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].origineAction").value(hasItem(DEFAULT_ORIGINE_ACTION)))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }

    @Test
    @Transactional
    void getAutreAction() throws Exception {
        // Initialize the database
        autreActionRepository.saveAndFlush(autreAction);

        // Get the autreAction
        restAutreActionMockMvc
            .perform(get(ENTITY_API_URL_ID, autreAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autreAction.getId().intValue()))
            .andExpect(jsonPath("$.origineAction").value(DEFAULT_ORIGINE_ACTION))
            .andExpect(jsonPath("$.origine").value(DEFAULT_ORIGINE));
    }

    @Test
    @Transactional
    void getNonExistingAutreAction() throws Exception {
        // Get the autreAction
        restAutreActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAutreAction() throws Exception {
        // Initialize the database
        autreActionRepository.saveAndFlush(autreAction);

        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();

        // Update the autreAction
        AutreAction updatedAutreAction = autreActionRepository.findById(autreAction.getId()).get();
        // Disconnect from session so that the updates on updatedAutreAction are not directly saved in db
        em.detach(updatedAutreAction);
        updatedAutreAction.origineAction(UPDATED_ORIGINE_ACTION).origine(UPDATED_ORIGINE);

        restAutreActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAutreAction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAutreAction))
            )
            .andExpect(status().isOk());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);
        AutreAction testAutreAction = autreActionList.get(autreActionList.size() - 1);
        assertThat(testAutreAction.getOrigineAction()).isEqualTo(UPDATED_ORIGINE_ACTION);
        assertThat(testAutreAction.getOrigine()).isEqualTo(UPDATED_ORIGINE);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository).save(testAutreAction);
    }

    @Test
    @Transactional
    void putNonExistingAutreAction() throws Exception {
        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();
        autreAction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutreActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autreAction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(autreAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(0)).save(autreAction);
    }

    @Test
    @Transactional
    void putWithIdMismatchAutreAction() throws Exception {
        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();
        autreAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutreActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(autreAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(0)).save(autreAction);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAutreAction() throws Exception {
        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();
        autreAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutreActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autreAction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(0)).save(autreAction);
    }

    @Test
    @Transactional
    void partialUpdateAutreActionWithPatch() throws Exception {
        // Initialize the database
        autreActionRepository.saveAndFlush(autreAction);

        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();

        // Update the autreAction using partial update
        AutreAction partialUpdatedAutreAction = new AutreAction();
        partialUpdatedAutreAction.setId(autreAction.getId());

        partialUpdatedAutreAction.origineAction(UPDATED_ORIGINE_ACTION).origine(UPDATED_ORIGINE);

        restAutreActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutreAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAutreAction))
            )
            .andExpect(status().isOk());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);
        AutreAction testAutreAction = autreActionList.get(autreActionList.size() - 1);
        assertThat(testAutreAction.getOrigineAction()).isEqualTo(UPDATED_ORIGINE_ACTION);
        assertThat(testAutreAction.getOrigine()).isEqualTo(UPDATED_ORIGINE);
    }

    @Test
    @Transactional
    void fullUpdateAutreActionWithPatch() throws Exception {
        // Initialize the database
        autreActionRepository.saveAndFlush(autreAction);

        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();

        // Update the autreAction using partial update
        AutreAction partialUpdatedAutreAction = new AutreAction();
        partialUpdatedAutreAction.setId(autreAction.getId());

        partialUpdatedAutreAction.origineAction(UPDATED_ORIGINE_ACTION).origine(UPDATED_ORIGINE);

        restAutreActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutreAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAutreAction))
            )
            .andExpect(status().isOk());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);
        AutreAction testAutreAction = autreActionList.get(autreActionList.size() - 1);
        assertThat(testAutreAction.getOrigineAction()).isEqualTo(UPDATED_ORIGINE_ACTION);
        assertThat(testAutreAction.getOrigine()).isEqualTo(UPDATED_ORIGINE);
    }

    @Test
    @Transactional
    void patchNonExistingAutreAction() throws Exception {
        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();
        autreAction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutreActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, autreAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(autreAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(0)).save(autreAction);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAutreAction() throws Exception {
        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();
        autreAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutreActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(autreAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(0)).save(autreAction);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAutreAction() throws Exception {
        int databaseSizeBeforeUpdate = autreActionRepository.findAll().size();
        autreAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutreActionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(autreAction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AutreAction in the database
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(0)).save(autreAction);
    }

    @Test
    @Transactional
    void deleteAutreAction() throws Exception {
        // Initialize the database
        autreActionRepository.saveAndFlush(autreAction);

        int databaseSizeBeforeDelete = autreActionRepository.findAll().size();

        // Delete the autreAction
        restAutreActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, autreAction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AutreAction> autreActionList = autreActionRepository.findAll();
        assertThat(autreActionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AutreAction in Elasticsearch
        verify(mockAutreActionSearchRepository, times(1)).deleteById(autreAction.getId());
    }

    @Test
    @Transactional
    void searchAutreAction() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        autreActionRepository.saveAndFlush(autreAction);
        when(mockAutreActionSearchRepository.search(queryStringQuery("id:" + autreAction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(autreAction), PageRequest.of(0, 1), 1));

        // Search the autreAction
        restAutreActionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + autreAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autreAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].origineAction").value(hasItem(DEFAULT_ORIGINE_ACTION)))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE)));
    }
}
