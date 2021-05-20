package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.AnalyseSWOT;
import com.betterfly.repository.AnalyseSWOTRepository;
import com.betterfly.repository.search.AnalyseSWOTSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.AnalyseSWOT}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AnalyseSWOTResource {

    private final Logger log = LoggerFactory.getLogger(AnalyseSWOTResource.class);

    private static final String ENTITY_NAME = "analyseSWOT";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalyseSWOTRepository analyseSWOTRepository;

    private final AnalyseSWOTSearchRepository analyseSWOTSearchRepository;

    public AnalyseSWOTResource(AnalyseSWOTRepository analyseSWOTRepository, AnalyseSWOTSearchRepository analyseSWOTSearchRepository) {
        this.analyseSWOTRepository = analyseSWOTRepository;
        this.analyseSWOTSearchRepository = analyseSWOTSearchRepository;
    }

    /**
     * {@code POST  /analyse-swots} : Create a new analyseSWOT.
     *
     * @param analyseSWOT the analyseSWOT to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analyseSWOT, or with status {@code 400 (Bad Request)} if the analyseSWOT has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/analyse-swots")
    public ResponseEntity<AnalyseSWOT> createAnalyseSWOT(@RequestBody AnalyseSWOT analyseSWOT) throws URISyntaxException {
        log.debug("REST request to save AnalyseSWOT : {}", analyseSWOT);
        if (analyseSWOT.getId() != null) {
            throw new BadRequestAlertException("A new analyseSWOT cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalyseSWOT result = analyseSWOTRepository.save(analyseSWOT);
        analyseSWOTSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/analyse-swots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /analyse-swots/:id} : Updates an existing analyseSWOT.
     *
     * @param id the id of the analyseSWOT to save.
     * @param analyseSWOT the analyseSWOT to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyseSWOT,
     * or with status {@code 400 (Bad Request)} if the analyseSWOT is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analyseSWOT couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/analyse-swots/{id}")
    public ResponseEntity<AnalyseSWOT> updateAnalyseSWOT(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnalyseSWOT analyseSWOT
    ) throws URISyntaxException {
        log.debug("REST request to update AnalyseSWOT : {}, {}", id, analyseSWOT);
        if (analyseSWOT.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyseSWOT.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyseSWOTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnalyseSWOT result = analyseSWOTRepository.save(analyseSWOT);
        analyseSWOTSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analyseSWOT.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /analyse-swots/:id} : Partial updates given fields of an existing analyseSWOT, field will ignore if it is null
     *
     * @param id the id of the analyseSWOT to save.
     * @param analyseSWOT the analyseSWOT to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyseSWOT,
     * or with status {@code 400 (Bad Request)} if the analyseSWOT is not valid,
     * or with status {@code 404 (Not Found)} if the analyseSWOT is not found,
     * or with status {@code 500 (Internal Server Error)} if the analyseSWOT couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/analyse-swots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnalyseSWOT> partialUpdateAnalyseSWOT(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnalyseSWOT analyseSWOT
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnalyseSWOT partially : {}, {}", id, analyseSWOT);
        if (analyseSWOT.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyseSWOT.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyseSWOTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnalyseSWOT> result = analyseSWOTRepository
            .findById(analyseSWOT.getId())
            .map(
                existingAnalyseSWOT -> {
                    if (analyseSWOT.getDateIdentification() != null) {
                        existingAnalyseSWOT.setDateIdentification(analyseSWOT.getDateIdentification());
                    }
                    if (analyseSWOT.getDescription() != null) {
                        existingAnalyseSWOT.setDescription(analyseSWOT.getDescription());
                    }
                    if (analyseSWOT.getPilote() != null) {
                        existingAnalyseSWOT.setPilote(analyseSWOT.getPilote());
                    }
                    if (analyseSWOT.getType() != null) {
                        existingAnalyseSWOT.setType(analyseSWOT.getType());
                    }
                    if (analyseSWOT.getBu() != null) {
                        existingAnalyseSWOT.setBu(analyseSWOT.getBu());
                    }
                    if (analyseSWOT.getCommentaire() != null) {
                        existingAnalyseSWOT.setCommentaire(analyseSWOT.getCommentaire());
                    }
                    if (analyseSWOT.getAfficher() != null) {
                        existingAnalyseSWOT.setAfficher(analyseSWOT.getAfficher());
                    }

                    return existingAnalyseSWOT;
                }
            )
            .map(analyseSWOTRepository::save)
            .map(
                savedAnalyseSWOT -> {
                    analyseSWOTSearchRepository.save(savedAnalyseSWOT);

                    return savedAnalyseSWOT;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analyseSWOT.getId().toString())
        );
    }

    /**
     * {@code GET  /analyse-swots} : get all the analyseSWOTS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analyseSWOTS in body.
     */
    @GetMapping("/analyse-swots")
    public ResponseEntity<List<AnalyseSWOT>> getAllAnalyseSWOTS(Pageable pageable) {
        log.debug("REST request to get a page of AnalyseSWOTS");
        Page<AnalyseSWOT> page = analyseSWOTRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /analyse-swots/:id} : get the "id" analyseSWOT.
     *
     * @param id the id of the analyseSWOT to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analyseSWOT, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/analyse-swots/{id}")
    public ResponseEntity<AnalyseSWOT> getAnalyseSWOT(@PathVariable Long id) {
        log.debug("REST request to get AnalyseSWOT : {}", id);
        Optional<AnalyseSWOT> analyseSWOT = analyseSWOTRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(analyseSWOT);
    }

    /**
     * {@code DELETE  /analyse-swots/:id} : delete the "id" analyseSWOT.
     *
     * @param id the id of the analyseSWOT to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/analyse-swots/{id}")
    public ResponseEntity<Void> deleteAnalyseSWOT(@PathVariable Long id) {
        log.debug("REST request to delete AnalyseSWOT : {}", id);
        analyseSWOTRepository.deleteById(id);
        analyseSWOTSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/analyse-swots?query=:query} : search for the analyseSWOT corresponding
     * to the query.
     *
     * @param query the query of the analyseSWOT search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/analyse-swots")
    public ResponseEntity<List<AnalyseSWOT>> searchAnalyseSWOTS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnalyseSWOTS for query {}", query);
        Page<AnalyseSWOT> page = analyseSWOTSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
