package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.AnalyseEnvirommentale;
import com.betterfly.repository.AnalyseEnvirommentaleRepository;
import com.betterfly.repository.search.AnalyseEnvirommentaleSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.AnalyseEnvirommentale}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AnalyseEnvirommentaleResource {

    private final Logger log = LoggerFactory.getLogger(AnalyseEnvirommentaleResource.class);

    private static final String ENTITY_NAME = "analyseEnvirommentale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalyseEnvirommentaleRepository analyseEnvirommentaleRepository;

    private final AnalyseEnvirommentaleSearchRepository analyseEnvirommentaleSearchRepository;

    public AnalyseEnvirommentaleResource(
        AnalyseEnvirommentaleRepository analyseEnvirommentaleRepository,
        AnalyseEnvirommentaleSearchRepository analyseEnvirommentaleSearchRepository
    ) {
        this.analyseEnvirommentaleRepository = analyseEnvirommentaleRepository;
        this.analyseEnvirommentaleSearchRepository = analyseEnvirommentaleSearchRepository;
    }

    /**
     * {@code POST  /analyse-envirommentales} : Create a new analyseEnvirommentale.
     *
     * @param analyseEnvirommentale the analyseEnvirommentale to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analyseEnvirommentale, or with status {@code 400 (Bad Request)} if the analyseEnvirommentale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/analyse-envirommentales")
    public ResponseEntity<AnalyseEnvirommentale> createAnalyseEnvirommentale(@RequestBody AnalyseEnvirommentale analyseEnvirommentale)
        throws URISyntaxException {
        log.debug("REST request to save AnalyseEnvirommentale : {}", analyseEnvirommentale);
        if (analyseEnvirommentale.getId() != null) {
            throw new BadRequestAlertException("A new analyseEnvirommentale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalyseEnvirommentale result = analyseEnvirommentaleRepository.save(analyseEnvirommentale);
        analyseEnvirommentaleSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/analyse-envirommentales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /analyse-envirommentales/:id} : Updates an existing analyseEnvirommentale.
     *
     * @param id the id of the analyseEnvirommentale to save.
     * @param analyseEnvirommentale the analyseEnvirommentale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyseEnvirommentale,
     * or with status {@code 400 (Bad Request)} if the analyseEnvirommentale is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analyseEnvirommentale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/analyse-envirommentales/{id}")
    public ResponseEntity<AnalyseEnvirommentale> updateAnalyseEnvirommentale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnalyseEnvirommentale analyseEnvirommentale
    ) throws URISyntaxException {
        log.debug("REST request to update AnalyseEnvirommentale : {}, {}", id, analyseEnvirommentale);
        if (analyseEnvirommentale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyseEnvirommentale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyseEnvirommentaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnalyseEnvirommentale result = analyseEnvirommentaleRepository.save(analyseEnvirommentale);
        analyseEnvirommentaleSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analyseEnvirommentale.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /analyse-envirommentales/:id} : Partial updates given fields of an existing analyseEnvirommentale, field will ignore if it is null
     *
     * @param id the id of the analyseEnvirommentale to save.
     * @param analyseEnvirommentale the analyseEnvirommentale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyseEnvirommentale,
     * or with status {@code 400 (Bad Request)} if the analyseEnvirommentale is not valid,
     * or with status {@code 404 (Not Found)} if the analyseEnvirommentale is not found,
     * or with status {@code 500 (Internal Server Error)} if the analyseEnvirommentale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/analyse-envirommentales/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnalyseEnvirommentale> partialUpdateAnalyseEnvirommentale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnalyseEnvirommentale analyseEnvirommentale
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnalyseEnvirommentale partially : {}, {}", id, analyseEnvirommentale);
        if (analyseEnvirommentale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyseEnvirommentale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyseEnvirommentaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnalyseEnvirommentale> result = analyseEnvirommentaleRepository
            .findById(analyseEnvirommentale.getId())
            .map(
                existingAnalyseEnvirommentale -> {
                    if (analyseEnvirommentale.getDate() != null) {
                        existingAnalyseEnvirommentale.setDate(analyseEnvirommentale.getDate());
                    }
                    if (analyseEnvirommentale.getBusinessUnit() != null) {
                        existingAnalyseEnvirommentale.setBusinessUnit(analyseEnvirommentale.getBusinessUnit());
                    }
                    if (analyseEnvirommentale.getActivite() != null) {
                        existingAnalyseEnvirommentale.setActivite(analyseEnvirommentale.getActivite());
                    }
                    if (analyseEnvirommentale.getAspectEnvironnemental() != null) {
                        existingAnalyseEnvirommentale.setAspectEnvironnemental(analyseEnvirommentale.getAspectEnvironnemental());
                    }
                    if (analyseEnvirommentale.getImpactEnvironnemental() != null) {
                        existingAnalyseEnvirommentale.setImpactEnvironnemental(analyseEnvirommentale.getImpactEnvironnemental());
                    }
                    if (analyseEnvirommentale.getCompetencesRequises() != null) {
                        existingAnalyseEnvirommentale.setCompetencesRequises(analyseEnvirommentale.getCompetencesRequises());
                    }
                    if (analyseEnvirommentale.getSituation() != null) {
                        existingAnalyseEnvirommentale.setSituation(analyseEnvirommentale.getSituation());
                    }
                    if (analyseEnvirommentale.getFrequence() != null) {
                        existingAnalyseEnvirommentale.setFrequence(analyseEnvirommentale.getFrequence());
                    }
                    if (analyseEnvirommentale.getSensibiliteMilieu() != null) {
                        existingAnalyseEnvirommentale.setSensibiliteMilieu(analyseEnvirommentale.getSensibiliteMilieu());
                    }
                    if (analyseEnvirommentale.getCoefficientMaitrise() != null) {
                        existingAnalyseEnvirommentale.setCoefficientMaitrise(analyseEnvirommentale.getCoefficientMaitrise());
                    }
                    if (analyseEnvirommentale.getGravite() != null) {
                        existingAnalyseEnvirommentale.setGravite(analyseEnvirommentale.getGravite());
                    }
                    if (analyseEnvirommentale.getCriticite() != null) {
                        existingAnalyseEnvirommentale.setCriticite(analyseEnvirommentale.getCriticite());
                    }
                    if (analyseEnvirommentale.getMaitriseExistante() != null) {
                        existingAnalyseEnvirommentale.setMaitriseExistante(analyseEnvirommentale.getMaitriseExistante());
                    }
                    if (analyseEnvirommentale.getOrigine() != null) {
                        existingAnalyseEnvirommentale.setOrigine(analyseEnvirommentale.getOrigine());
                    }

                    return existingAnalyseEnvirommentale;
                }
            )
            .map(analyseEnvirommentaleRepository::save)
            .map(
                savedAnalyseEnvirommentale -> {
                    analyseEnvirommentaleSearchRepository.save(savedAnalyseEnvirommentale);

                    return savedAnalyseEnvirommentale;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analyseEnvirommentale.getId().toString())
        );
    }

    /**
     * {@code GET  /analyse-envirommentales} : get all the analyseEnvirommentales.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analyseEnvirommentales in body.
     */
    @GetMapping("/analyse-envirommentales")
    public ResponseEntity<List<AnalyseEnvirommentale>> getAllAnalyseEnvirommentales(Pageable pageable) {
        log.debug("REST request to get a page of AnalyseEnvirommentales");
        Page<AnalyseEnvirommentale> page = analyseEnvirommentaleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /analyse-envirommentales/:id} : get the "id" analyseEnvirommentale.
     *
     * @param id the id of the analyseEnvirommentale to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analyseEnvirommentale, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/analyse-envirommentales/{id}")
    public ResponseEntity<AnalyseEnvirommentale> getAnalyseEnvirommentale(@PathVariable Long id) {
        log.debug("REST request to get AnalyseEnvirommentale : {}", id);
        Optional<AnalyseEnvirommentale> analyseEnvirommentale = analyseEnvirommentaleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(analyseEnvirommentale);
    }

    /**
     * {@code DELETE  /analyse-envirommentales/:id} : delete the "id" analyseEnvirommentale.
     *
     * @param id the id of the analyseEnvirommentale to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/analyse-envirommentales/{id}")
    public ResponseEntity<Void> deleteAnalyseEnvirommentale(@PathVariable Long id) {
        log.debug("REST request to delete AnalyseEnvirommentale : {}", id);
        analyseEnvirommentaleRepository.deleteById(id);
        analyseEnvirommentaleSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/analyse-envirommentales?query=:query} : search for the analyseEnvirommentale corresponding
     * to the query.
     *
     * @param query the query of the analyseEnvirommentale search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/analyse-envirommentales")
    public ResponseEntity<List<AnalyseEnvirommentale>> searchAnalyseEnvirommentales(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnalyseEnvirommentales for query {}", query);
        Page<AnalyseEnvirommentale> page = analyseEnvirommentaleSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
