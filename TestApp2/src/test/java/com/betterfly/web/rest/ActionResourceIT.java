package com.betterfly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterfly.IntegrationTest;
import com.betterfly.domain.Action;
import com.betterfly.domain.enumeration.Efficace;
import com.betterfly.domain.enumeration.Statut;
import com.betterfly.domain.enumeration.TypeAction;
import com.betterfly.repository.ActionRepository;
import com.betterfly.repository.search.ActionSearchRepository;
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
 * Integration tests for the {@link ActionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ActionResourceIT {

    private static final LocalDate DEFAULT_DATE_PLANIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PLANIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final TypeAction DEFAULT_TYPE = TypeAction.ACTION_FACE_AU_RISQUE;
    private static final TypeAction UPDATED_TYPE = TypeAction.ACTION_CORRECTIVE;

    private static final LocalDate DEFAULT_DELAI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELAI = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_AVANCEMENT = "AAAAAAAAAA";
    private static final String UPDATED_AVANCEMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REALISEE = false;
    private static final Boolean UPDATED_REALISEE = true;

    private static final String DEFAULT_CRITERE_RESULTAT = "AAAAAAAAAA";
    private static final String UPDATED_CRITERE_RESULTAT = "BBBBBBBBBB";

    private static final String DEFAULT_RESSOURCES_NECESSAIRES = "AAAAAAAAAA";
    private static final String UPDATED_RESSOURCES_NECESSAIRES = "BBBBBBBBBB";

    private static final Statut DEFAULT_STATUT = Statut.EN_COURS;
    private static final Statut UPDATED_STATUT = Statut.PLANIFIEE;

    private static final Efficace DEFAULT_EFFICACE = Efficace.EFFICACE;
    private static final Efficace UPDATED_EFFICACE = Efficace.NON_EFFICACE;

    private static final String ENTITY_API_URL = "/api/actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/actions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActionRepository actionRepository;

    /**
     * This repository is mocked in the com.betterfly.repository.search test package.
     *
     * @see com.betterfly.repository.search.ActionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ActionSearchRepository mockActionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActionMockMvc;

    private Action action;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Action createEntity(EntityManager em) {
        Action action = new Action()
            .datePlanification(DEFAULT_DATE_PLANIFICATION)
            .action(DEFAULT_ACTION)
            .type(DEFAULT_TYPE)
            .delai(DEFAULT_DELAI)
            .avancement(DEFAULT_AVANCEMENT)
            .realisee(DEFAULT_REALISEE)
            .critereResultat(DEFAULT_CRITERE_RESULTAT)
            .ressourcesNecessaires(DEFAULT_RESSOURCES_NECESSAIRES)
            .statut(DEFAULT_STATUT)
            .efficace(DEFAULT_EFFICACE);
        return action;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Action createUpdatedEntity(EntityManager em) {
        Action action = new Action()
            .datePlanification(UPDATED_DATE_PLANIFICATION)
            .action(UPDATED_ACTION)
            .type(UPDATED_TYPE)
            .delai(UPDATED_DELAI)
            .avancement(UPDATED_AVANCEMENT)
            .realisee(UPDATED_REALISEE)
            .critereResultat(UPDATED_CRITERE_RESULTAT)
            .ressourcesNecessaires(UPDATED_RESSOURCES_NECESSAIRES)
            .statut(UPDATED_STATUT)
            .efficace(UPDATED_EFFICACE);
        return action;
    }

    @BeforeEach
    public void initTest() {
        action = createEntity(em);
    }

    @Test
    @Transactional
    void createAction() throws Exception {
        int databaseSizeBeforeCreate = actionRepository.findAll().size();
        // Create the Action
        restActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(action)))
            .andExpect(status().isCreated());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeCreate + 1);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getDatePlanification()).isEqualTo(DEFAULT_DATE_PLANIFICATION);
        assertThat(testAction.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAction.getDelai()).isEqualTo(DEFAULT_DELAI);
        assertThat(testAction.getAvancement()).isEqualTo(DEFAULT_AVANCEMENT);
        assertThat(testAction.getRealisee()).isEqualTo(DEFAULT_REALISEE);
        assertThat(testAction.getCritereResultat()).isEqualTo(DEFAULT_CRITERE_RESULTAT);
        assertThat(testAction.getRessourcesNecessaires()).isEqualTo(DEFAULT_RESSOURCES_NECESSAIRES);
        assertThat(testAction.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testAction.getEfficace()).isEqualTo(DEFAULT_EFFICACE);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(1)).save(testAction);
    }

    @Test
    @Transactional
    void createActionWithExistingId() throws Exception {
        // Create the Action with an existing ID
        action.setId(1L);

        int databaseSizeBeforeCreate = actionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(action)))
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(0)).save(action);
    }

    @Test
    @Transactional
    void getAllActions() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList
        restActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(action.getId().intValue())))
            .andExpect(jsonPath("$.[*].datePlanification").value(hasItem(DEFAULT_DATE_PLANIFICATION.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].delai").value(hasItem(DEFAULT_DELAI.toString())))
            .andExpect(jsonPath("$.[*].avancement").value(hasItem(DEFAULT_AVANCEMENT)))
            .andExpect(jsonPath("$.[*].realisee").value(hasItem(DEFAULT_REALISEE.booleanValue())))
            .andExpect(jsonPath("$.[*].critereResultat").value(hasItem(DEFAULT_CRITERE_RESULTAT)))
            .andExpect(jsonPath("$.[*].ressourcesNecessaires").value(hasItem(DEFAULT_RESSOURCES_NECESSAIRES)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].efficace").value(hasItem(DEFAULT_EFFICACE.toString())));
    }

    @Test
    @Transactional
    void getAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get the action
        restActionMockMvc
            .perform(get(ENTITY_API_URL_ID, action.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(action.getId().intValue()))
            .andExpect(jsonPath("$.datePlanification").value(DEFAULT_DATE_PLANIFICATION.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.delai").value(DEFAULT_DELAI.toString()))
            .andExpect(jsonPath("$.avancement").value(DEFAULT_AVANCEMENT))
            .andExpect(jsonPath("$.realisee").value(DEFAULT_REALISEE.booleanValue()))
            .andExpect(jsonPath("$.critereResultat").value(DEFAULT_CRITERE_RESULTAT))
            .andExpect(jsonPath("$.ressourcesNecessaires").value(DEFAULT_RESSOURCES_NECESSAIRES))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.efficace").value(DEFAULT_EFFICACE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAction() throws Exception {
        // Get the action
        restActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action
        Action updatedAction = actionRepository.findById(action.getId()).get();
        // Disconnect from session so that the updates on updatedAction are not directly saved in db
        em.detach(updatedAction);
        updatedAction
            .datePlanification(UPDATED_DATE_PLANIFICATION)
            .action(UPDATED_ACTION)
            .type(UPDATED_TYPE)
            .delai(UPDATED_DELAI)
            .avancement(UPDATED_AVANCEMENT)
            .realisee(UPDATED_REALISEE)
            .critereResultat(UPDATED_CRITERE_RESULTAT)
            .ressourcesNecessaires(UPDATED_RESSOURCES_NECESSAIRES)
            .statut(UPDATED_STATUT)
            .efficace(UPDATED_EFFICACE);

        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAction))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getDatePlanification()).isEqualTo(UPDATED_DATE_PLANIFICATION);
        assertThat(testAction.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAction.getDelai()).isEqualTo(UPDATED_DELAI);
        assertThat(testAction.getAvancement()).isEqualTo(UPDATED_AVANCEMENT);
        assertThat(testAction.getRealisee()).isEqualTo(UPDATED_REALISEE);
        assertThat(testAction.getCritereResultat()).isEqualTo(UPDATED_CRITERE_RESULTAT);
        assertThat(testAction.getRessourcesNecessaires()).isEqualTo(UPDATED_RESSOURCES_NECESSAIRES);
        assertThat(testAction.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAction.getEfficace()).isEqualTo(UPDATED_EFFICACE);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository).save(testAction);
    }

    @Test
    @Transactional
    void putNonExistingAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, action.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(action))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(0)).save(action);
    }

    @Test
    @Transactional
    void putWithIdMismatchAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(action))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(0)).save(action);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(action)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(0)).save(action);
    }

    @Test
    @Transactional
    void partialUpdateActionWithPatch() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action using partial update
        Action partialUpdatedAction = new Action();
        partialUpdatedAction.setId(action.getId());

        partialUpdatedAction
            .datePlanification(UPDATED_DATE_PLANIFICATION)
            .type(UPDATED_TYPE)
            .delai(UPDATED_DELAI)
            .realisee(UPDATED_REALISEE)
            .ressourcesNecessaires(UPDATED_RESSOURCES_NECESSAIRES)
            .efficace(UPDATED_EFFICACE);

        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAction))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getDatePlanification()).isEqualTo(UPDATED_DATE_PLANIFICATION);
        assertThat(testAction.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAction.getDelai()).isEqualTo(UPDATED_DELAI);
        assertThat(testAction.getAvancement()).isEqualTo(DEFAULT_AVANCEMENT);
        assertThat(testAction.getRealisee()).isEqualTo(UPDATED_REALISEE);
        assertThat(testAction.getCritereResultat()).isEqualTo(DEFAULT_CRITERE_RESULTAT);
        assertThat(testAction.getRessourcesNecessaires()).isEqualTo(UPDATED_RESSOURCES_NECESSAIRES);
        assertThat(testAction.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testAction.getEfficace()).isEqualTo(UPDATED_EFFICACE);
    }

    @Test
    @Transactional
    void fullUpdateActionWithPatch() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action using partial update
        Action partialUpdatedAction = new Action();
        partialUpdatedAction.setId(action.getId());

        partialUpdatedAction
            .datePlanification(UPDATED_DATE_PLANIFICATION)
            .action(UPDATED_ACTION)
            .type(UPDATED_TYPE)
            .delai(UPDATED_DELAI)
            .avancement(UPDATED_AVANCEMENT)
            .realisee(UPDATED_REALISEE)
            .critereResultat(UPDATED_CRITERE_RESULTAT)
            .ressourcesNecessaires(UPDATED_RESSOURCES_NECESSAIRES)
            .statut(UPDATED_STATUT)
            .efficace(UPDATED_EFFICACE);

        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAction))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getDatePlanification()).isEqualTo(UPDATED_DATE_PLANIFICATION);
        assertThat(testAction.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAction.getDelai()).isEqualTo(UPDATED_DELAI);
        assertThat(testAction.getAvancement()).isEqualTo(UPDATED_AVANCEMENT);
        assertThat(testAction.getRealisee()).isEqualTo(UPDATED_REALISEE);
        assertThat(testAction.getCritereResultat()).isEqualTo(UPDATED_CRITERE_RESULTAT);
        assertThat(testAction.getRessourcesNecessaires()).isEqualTo(UPDATED_RESSOURCES_NECESSAIRES);
        assertThat(testAction.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAction.getEfficace()).isEqualTo(UPDATED_EFFICACE);
    }

    @Test
    @Transactional
    void patchNonExistingAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, action.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(action))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(0)).save(action);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(action))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(0)).save(action);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(action)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(0)).save(action);
    }

    @Test
    @Transactional
    void deleteAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeDelete = actionRepository.findAll().size();

        // Delete the action
        restActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, action.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Action in Elasticsearch
        verify(mockActionSearchRepository, times(1)).deleteById(action.getId());
    }

    @Test
    @Transactional
    void searchAction() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        actionRepository.saveAndFlush(action);
        when(mockActionSearchRepository.search(queryStringQuery("id:" + action.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(action), PageRequest.of(0, 1), 1));

        // Search the action
        restActionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + action.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(action.getId().intValue())))
            .andExpect(jsonPath("$.[*].datePlanification").value(hasItem(DEFAULT_DATE_PLANIFICATION.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].delai").value(hasItem(DEFAULT_DELAI.toString())))
            .andExpect(jsonPath("$.[*].avancement").value(hasItem(DEFAULT_AVANCEMENT)))
            .andExpect(jsonPath("$.[*].realisee").value(hasItem(DEFAULT_REALISEE.booleanValue())))
            .andExpect(jsonPath("$.[*].critereResultat").value(hasItem(DEFAULT_CRITERE_RESULTAT)))
            .andExpect(jsonPath("$.[*].ressourcesNecessaires").value(hasItem(DEFAULT_RESSOURCES_NECESSAIRES)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].efficace").value(hasItem(DEFAULT_EFFICACE.toString())));
    }
}
