package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.PolitiqueQSE;
import com.betterfly.repository.PolitiqueQSERepository;
import com.betterfly.repository.search.PolitiqueQSESearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.PolitiqueQSE}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PolitiqueQSEResource {

    private final Logger log = LoggerFactory.getLogger(PolitiqueQSEResource.class);

    private static final String ENTITY_NAME = "politiqueQSE";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PolitiqueQSERepository politiqueQSERepository;

    private final PolitiqueQSESearchRepository politiqueQSESearchRepository;

    public PolitiqueQSEResource(PolitiqueQSERepository politiqueQSERepository, PolitiqueQSESearchRepository politiqueQSESearchRepository) {
        this.politiqueQSERepository = politiqueQSERepository;
        this.politiqueQSESearchRepository = politiqueQSESearchRepository;
    }

    /**
     * {@code POST  /politique-qses} : Create a new politiqueQSE.
     *
     * @param politiqueQSE the politiqueQSE to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new politiqueQSE, or with status {@code 400 (Bad Request)} if the politiqueQSE has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/politique-qses")
    public ResponseEntity<PolitiqueQSE> createPolitiqueQSE(@RequestBody PolitiqueQSE politiqueQSE) throws URISyntaxException {
        log.debug("REST request to save PolitiqueQSE : {}", politiqueQSE);
        if (politiqueQSE.getId() != null) {
            throw new BadRequestAlertException("A new politiqueQSE cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PolitiqueQSE result = politiqueQSERepository.save(politiqueQSE);
        politiqueQSESearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/politique-qses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /politique-qses/:id} : Updates an existing politiqueQSE.
     *
     * @param id the id of the politiqueQSE to save.
     * @param politiqueQSE the politiqueQSE to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated politiqueQSE,
     * or with status {@code 400 (Bad Request)} if the politiqueQSE is not valid,
     * or with status {@code 500 (Internal Server Error)} if the politiqueQSE couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/politique-qses/{id}")
    public ResponseEntity<PolitiqueQSE> updatePolitiqueQSE(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PolitiqueQSE politiqueQSE
    ) throws URISyntaxException {
        log.debug("REST request to update PolitiqueQSE : {}, {}", id, politiqueQSE);
        if (politiqueQSE.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, politiqueQSE.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!politiqueQSERepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PolitiqueQSE result = politiqueQSERepository.save(politiqueQSE);
        politiqueQSESearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, politiqueQSE.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /politique-qses/:id} : Partial updates given fields of an existing politiqueQSE, field will ignore if it is null
     *
     * @param id the id of the politiqueQSE to save.
     * @param politiqueQSE the politiqueQSE to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated politiqueQSE,
     * or with status {@code 400 (Bad Request)} if the politiqueQSE is not valid,
     * or with status {@code 404 (Not Found)} if the politiqueQSE is not found,
     * or with status {@code 500 (Internal Server Error)} if the politiqueQSE couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/politique-qses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PolitiqueQSE> partialUpdatePolitiqueQSE(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PolitiqueQSE politiqueQSE
    ) throws URISyntaxException {
        log.debug("REST request to partial update PolitiqueQSE partially : {}, {}", id, politiqueQSE);
        if (politiqueQSE.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, politiqueQSE.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!politiqueQSERepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PolitiqueQSE> result = politiqueQSERepository
            .findById(politiqueQSE.getId())
            .map(
                existingPolitiqueQSE -> {
                    if (politiqueQSE.getDate() != null) {
                        existingPolitiqueQSE.setDate(politiqueQSE.getDate());
                    }
                    if (politiqueQSE.getAxePolitiqueQSE() != null) {
                        existingPolitiqueQSE.setAxePolitiqueQSE(politiqueQSE.getAxePolitiqueQSE());
                    }
                    if (politiqueQSE.getObjectifQSE() != null) {
                        existingPolitiqueQSE.setObjectifQSE(politiqueQSE.getObjectifQSE());
                    }
                    if (politiqueQSE.getVigueur() != null) {
                        existingPolitiqueQSE.setVigueur(politiqueQSE.getVigueur());
                    }

                    return existingPolitiqueQSE;
                }
            )
            .map(politiqueQSERepository::save)
            .map(
                savedPolitiqueQSE -> {
                    politiqueQSESearchRepository.save(savedPolitiqueQSE);

                    return savedPolitiqueQSE;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, politiqueQSE.getId().toString())
        );
    }

    /**
     * {@code GET  /politique-qses} : get all the politiqueQSES.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of politiqueQSES in body.
     */
    @GetMapping("/politique-qses")
    public ResponseEntity<List<PolitiqueQSE>> getAllPolitiqueQSES(Pageable pageable) {
        log.debug("REST request to get a page of PolitiqueQSES");
        Page<PolitiqueQSE> page = politiqueQSERepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /politique-qses/:id} : get the "id" politiqueQSE.
     *
     * @param id the id of the politiqueQSE to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the politiqueQSE, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/politique-qses/{id}")
    public ResponseEntity<PolitiqueQSE> getPolitiqueQSE(@PathVariable Long id) {
        log.debug("REST request to get PolitiqueQSE : {}", id);
        Optional<PolitiqueQSE> politiqueQSE = politiqueQSERepository.findById(id);
        return ResponseUtil.wrapOrNotFound(politiqueQSE);
    }

    /**
     * {@code DELETE  /politique-qses/:id} : delete the "id" politiqueQSE.
     *
     * @param id the id of the politiqueQSE to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/politique-qses/{id}")
    public ResponseEntity<Void> deletePolitiqueQSE(@PathVariable Long id) {
        log.debug("REST request to delete PolitiqueQSE : {}", id);
        politiqueQSERepository.deleteById(id);
        politiqueQSESearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/politique-qses?query=:query} : search for the politiqueQSE corresponding
     * to the query.
     *
     * @param query the query of the politiqueQSE search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/politique-qses")
    public ResponseEntity<List<PolitiqueQSE>> searchPolitiqueQSES(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PolitiqueQSES for query {}", query);
        Page<PolitiqueQSE> page = politiqueQSESearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
