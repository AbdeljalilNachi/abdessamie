package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.AnalyseSST;
import com.betterfly.repository.AnalyseSSTRepository;
import com.betterfly.repository.search.AnalyseSSTSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.AnalyseSST}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AnalyseSSTResource {

    private final Logger log = LoggerFactory.getLogger(AnalyseSSTResource.class);

    private static final String ENTITY_NAME = "analyseSST";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalyseSSTRepository analyseSSTRepository;

    private final AnalyseSSTSearchRepository analyseSSTSearchRepository;

    public AnalyseSSTResource(AnalyseSSTRepository analyseSSTRepository, AnalyseSSTSearchRepository analyseSSTSearchRepository) {
        this.analyseSSTRepository = analyseSSTRepository;
        this.analyseSSTSearchRepository = analyseSSTSearchRepository;
    }

    /**
     * {@code POST  /analyse-ssts} : Create a new analyseSST.
     *
     * @param analyseSST the analyseSST to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analyseSST, or with status {@code 400 (Bad Request)} if the analyseSST has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/analyse-ssts")
    public ResponseEntity<AnalyseSST> createAnalyseSST(@RequestBody AnalyseSST analyseSST) throws URISyntaxException {
        log.debug("REST request to save AnalyseSST : {}", analyseSST);
        if (analyseSST.getId() != null) {
            throw new BadRequestAlertException("A new analyseSST cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalyseSST result = analyseSSTRepository.save(analyseSST);
        analyseSSTSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/analyse-ssts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /analyse-ssts/:id} : Updates an existing analyseSST.
     *
     * @param id the id of the analyseSST to save.
     * @param analyseSST the analyseSST to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyseSST,
     * or with status {@code 400 (Bad Request)} if the analyseSST is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analyseSST couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/analyse-ssts/{id}")
    public ResponseEntity<AnalyseSST> updateAnalyseSST(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnalyseSST analyseSST
    ) throws URISyntaxException {
        log.debug("REST request to update AnalyseSST : {}, {}", id, analyseSST);
        if (analyseSST.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyseSST.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyseSSTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnalyseSST result = analyseSSTRepository.save(analyseSST);
        analyseSSTSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analyseSST.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /analyse-ssts/:id} : Partial updates given fields of an existing analyseSST, field will ignore if it is null
     *
     * @param id the id of the analyseSST to save.
     * @param analyseSST the analyseSST to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyseSST,
     * or with status {@code 400 (Bad Request)} if the analyseSST is not valid,
     * or with status {@code 404 (Not Found)} if the analyseSST is not found,
     * or with status {@code 500 (Internal Server Error)} if the analyseSST couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/analyse-ssts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnalyseSST> partialUpdateAnalyseSST(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnalyseSST analyseSST
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnalyseSST partially : {}, {}", id, analyseSST);
        if (analyseSST.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyseSST.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyseSSTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnalyseSST> result = analyseSSTRepository
            .findById(analyseSST.getId())
            .map(
                existingAnalyseSST -> {
                    if (analyseSST.getDate() != null) {
                        existingAnalyseSST.setDate(analyseSST.getDate());
                    }
                    if (analyseSST.getBuisnessUnit() != null) {
                        existingAnalyseSST.setBuisnessUnit(analyseSST.getBuisnessUnit());
                    }
                    if (analyseSST.getUniteTravail() != null) {
                        existingAnalyseSST.setUniteTravail(analyseSST.getUniteTravail());
                    }
                    if (analyseSST.getDanger() != null) {
                        existingAnalyseSST.setDanger(analyseSST.getDanger());
                    }
                    if (analyseSST.getRisque() != null) {
                        existingAnalyseSST.setRisque(analyseSST.getRisque());
                    }
                    if (analyseSST.getCompetence() != null) {
                        existingAnalyseSST.setCompetence(analyseSST.getCompetence());
                    }
                    if (analyseSST.getSituation() != null) {
                        existingAnalyseSST.setSituation(analyseSST.getSituation());
                    }
                    if (analyseSST.getFrequence() != null) {
                        existingAnalyseSST.setFrequence(analyseSST.getFrequence());
                    }
                    if (analyseSST.getDureeExposition() != null) {
                        existingAnalyseSST.setDureeExposition(analyseSST.getDureeExposition());
                    }
                    if (analyseSST.getCoefficientMaitrise() != null) {
                        existingAnalyseSST.setCoefficientMaitrise(analyseSST.getCoefficientMaitrise());
                    }
                    if (analyseSST.getGravite() != null) {
                        existingAnalyseSST.setGravite(analyseSST.getGravite());
                    }
                    if (analyseSST.getCriticite() != null) {
                        existingAnalyseSST.setCriticite(analyseSST.getCriticite());
                    }
                    if (analyseSST.getMaitriseExistante() != null) {
                        existingAnalyseSST.setMaitriseExistante(analyseSST.getMaitriseExistante());
                    }
                    if (analyseSST.getOrigine() != null) {
                        existingAnalyseSST.setOrigine(analyseSST.getOrigine());
                    }

                    return existingAnalyseSST;
                }
            )
            .map(analyseSSTRepository::save)
            .map(
                savedAnalyseSST -> {
                    analyseSSTSearchRepository.save(savedAnalyseSST);

                    return savedAnalyseSST;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analyseSST.getId().toString())
        );
    }

    /**
     * {@code GET  /analyse-ssts} : get all the analyseSSTS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analyseSSTS in body.
     */
    @GetMapping("/analyse-ssts")
    public ResponseEntity<List<AnalyseSST>> getAllAnalyseSSTS(Pageable pageable) {
        log.debug("REST request to get a page of AnalyseSSTS");
        Page<AnalyseSST> page = analyseSSTRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /analyse-ssts/:id} : get the "id" analyseSST.
     *
     * @param id the id of the analyseSST to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analyseSST, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/analyse-ssts/{id}")
    public ResponseEntity<AnalyseSST> getAnalyseSST(@PathVariable Long id) {
        log.debug("REST request to get AnalyseSST : {}", id);
        Optional<AnalyseSST> analyseSST = analyseSSTRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(analyseSST);
    }

    /**
     * {@code DELETE  /analyse-ssts/:id} : delete the "id" analyseSST.
     *
     * @param id the id of the analyseSST to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/analyse-ssts/{id}")
    public ResponseEntity<Void> deleteAnalyseSST(@PathVariable Long id) {
        log.debug("REST request to delete AnalyseSST : {}", id);
        analyseSSTRepository.deleteById(id);
        analyseSSTSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/analyse-ssts?query=:query} : search for the analyseSST corresponding
     * to the query.
     *
     * @param query the query of the analyseSST search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/analyse-ssts")
    public ResponseEntity<List<AnalyseSST>> searchAnalyseSSTS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnalyseSSTS for query {}", query);
        Page<AnalyseSST> page = analyseSSTSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
