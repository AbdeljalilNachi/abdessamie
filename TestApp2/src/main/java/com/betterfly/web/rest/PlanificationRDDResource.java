package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.PlanificationRDD;
import com.betterfly.repository.PlanificationRDDRepository;
import com.betterfly.repository.search.PlanificationRDDSearchRepository;
import com.betterfly.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.betterfly.domain.PlanificationRDD}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlanificationRDDResource {

    private final Logger log = LoggerFactory.getLogger(PlanificationRDDResource.class);

    private static final String ENTITY_NAME = "planificationRDD";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanificationRDDRepository planificationRDDRepository;

    private final PlanificationRDDSearchRepository planificationRDDSearchRepository;

    public PlanificationRDDResource(
        PlanificationRDDRepository planificationRDDRepository,
        PlanificationRDDSearchRepository planificationRDDSearchRepository
    ) {
        this.planificationRDDRepository = planificationRDDRepository;
        this.planificationRDDSearchRepository = planificationRDDSearchRepository;
    }

    /**
     * {@code POST  /planification-rdds} : Create a new planificationRDD.
     *
     * @param planificationRDD the planificationRDD to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planificationRDD, or with status {@code 400 (Bad Request)} if the planificationRDD has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/planification-rdds")
    public ResponseEntity<PlanificationRDD> createPlanificationRDD(@RequestBody PlanificationRDD planificationRDD)
        throws URISyntaxException {
        log.debug("REST request to save PlanificationRDD : {}", planificationRDD);
        if (planificationRDD.getId() != null) {
            throw new BadRequestAlertException("A new planificationRDD cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanificationRDD result = planificationRDDRepository.save(planificationRDD);
        planificationRDDSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/planification-rdds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /planification-rdds/:id} : Updates an existing planificationRDD.
     *
     * @param id the id of the planificationRDD to save.
     * @param planificationRDD the planificationRDD to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planificationRDD,
     * or with status {@code 400 (Bad Request)} if the planificationRDD is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planificationRDD couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/planification-rdds/{id}")
    public ResponseEntity<PlanificationRDD> updatePlanificationRDD(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlanificationRDD planificationRDD
    ) throws URISyntaxException {
        log.debug("REST request to update PlanificationRDD : {}, {}", id, planificationRDD);
        if (planificationRDD.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planificationRDD.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planificationRDDRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlanificationRDD result = planificationRDDRepository.save(planificationRDD);
        planificationRDDSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planificationRDD.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /planification-rdds/:id} : Partial updates given fields of an existing planificationRDD, field will ignore if it is null
     *
     * @param id the id of the planificationRDD to save.
     * @param planificationRDD the planificationRDD to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planificationRDD,
     * or with status {@code 400 (Bad Request)} if the planificationRDD is not valid,
     * or with status {@code 404 (Not Found)} if the planificationRDD is not found,
     * or with status {@code 500 (Internal Server Error)} if the planificationRDD couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/planification-rdds/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PlanificationRDD> partialUpdatePlanificationRDD(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlanificationRDD planificationRDD
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlanificationRDD partially : {}, {}", id, planificationRDD);
        if (planificationRDD.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planificationRDD.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planificationRDDRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanificationRDD> result = planificationRDDRepository
            .findById(planificationRDD.getId())
            .map(
                existingPlanificationRDD -> {
                    if (planificationRDD.getnRdd() != null) {
                        existingPlanificationRDD.setnRdd(planificationRDD.getnRdd());
                    }
                    if (planificationRDD.getDate() != null) {
                        existingPlanificationRDD.setDate(planificationRDD.getDate());
                    }
                    if (planificationRDD.getRealisee() != null) {
                        existingPlanificationRDD.setRealisee(planificationRDD.getRealisee());
                    }
                    if (planificationRDD.getPresentation() != null) {
                        existingPlanificationRDD.setPresentation(planificationRDD.getPresentation());
                    }
                    if (planificationRDD.getPresentationContentType() != null) {
                        existingPlanificationRDD.setPresentationContentType(planificationRDD.getPresentationContentType());
                    }
                    if (planificationRDD.getStandard() != null) {
                        existingPlanificationRDD.setStandard(planificationRDD.getStandard());
                    }

                    return existingPlanificationRDD;
                }
            )
            .map(planificationRDDRepository::save)
            .map(
                savedPlanificationRDD -> {
                    planificationRDDSearchRepository.save(savedPlanificationRDD);

                    return savedPlanificationRDD;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planificationRDD.getId().toString())
        );
    }

    /**
     * {@code GET  /planification-rdds} : get all the planificationRDDS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planificationRDDS in body.
     */
    @GetMapping("/planification-rdds")
    public ResponseEntity<List<PlanificationRDD>> getAllPlanificationRDDS(Pageable pageable) {
        log.debug("REST request to get a page of PlanificationRDDS");
        Page<PlanificationRDD> page = planificationRDDRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /planification-rdds/:id} : get the "id" planificationRDD.
     *
     * @param id the id of the planificationRDD to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planificationRDD, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planification-rdds/{id}")
    public ResponseEntity<PlanificationRDD> getPlanificationRDD(@PathVariable Long id) {
        log.debug("REST request to get PlanificationRDD : {}", id);
        Optional<PlanificationRDD> planificationRDD = planificationRDDRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(planificationRDD);
    }

    /**
     * {@code DELETE  /planification-rdds/:id} : delete the "id" planificationRDD.
     *
     * @param id the id of the planificationRDD to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/planification-rdds/{id}")
    public ResponseEntity<Void> deletePlanificationRDD(@PathVariable Long id) {
        log.debug("REST request to delete PlanificationRDD : {}", id);
        planificationRDDRepository.deleteById(id);
        planificationRDDSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/planification-rdds?query=:query} : search for the planificationRDD corresponding
     * to the query.
     *
     * @param query the query of the planificationRDD search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/planification-rdds")
    public ResponseEntity<List<PlanificationRDD>> searchPlanificationRDDS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PlanificationRDDS for query {}", query);
        Page<PlanificationRDD> page = planificationRDDSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
