package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.ResultIndicateurs;
import com.betterfly.repository.ResultIndicateursRepository;
import com.betterfly.repository.search.ResultIndicateursSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.ResultIndicateurs}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResultIndicateursResource {

    private final Logger log = LoggerFactory.getLogger(ResultIndicateursResource.class);

    private static final String ENTITY_NAME = "resultIndicateurs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResultIndicateursRepository resultIndicateursRepository;

    private final ResultIndicateursSearchRepository resultIndicateursSearchRepository;

    public ResultIndicateursResource(
        ResultIndicateursRepository resultIndicateursRepository,
        ResultIndicateursSearchRepository resultIndicateursSearchRepository
    ) {
        this.resultIndicateursRepository = resultIndicateursRepository;
        this.resultIndicateursSearchRepository = resultIndicateursSearchRepository;
    }

    /**
     * {@code POST  /result-indicateurs} : Create a new resultIndicateurs.
     *
     * @param resultIndicateurs the resultIndicateurs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resultIndicateurs, or with status {@code 400 (Bad Request)} if the resultIndicateurs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/result-indicateurs")
    public ResponseEntity<ResultIndicateurs> createResultIndicateurs(@RequestBody ResultIndicateurs resultIndicateurs)
        throws URISyntaxException {
        log.debug("REST request to save ResultIndicateurs : {}", resultIndicateurs);
        if (resultIndicateurs.getId() != null) {
            throw new BadRequestAlertException("A new resultIndicateurs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResultIndicateurs result = resultIndicateursRepository.save(resultIndicateurs);
        resultIndicateursSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/result-indicateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /result-indicateurs/:id} : Updates an existing resultIndicateurs.
     *
     * @param id the id of the resultIndicateurs to save.
     * @param resultIndicateurs the resultIndicateurs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultIndicateurs,
     * or with status {@code 400 (Bad Request)} if the resultIndicateurs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resultIndicateurs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/result-indicateurs/{id}")
    public ResponseEntity<ResultIndicateurs> updateResultIndicateurs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResultIndicateurs resultIndicateurs
    ) throws URISyntaxException {
        log.debug("REST request to update ResultIndicateurs : {}, {}", id, resultIndicateurs);
        if (resultIndicateurs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resultIndicateurs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resultIndicateursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResultIndicateurs result = resultIndicateursRepository.save(resultIndicateurs);
        resultIndicateursSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resultIndicateurs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /result-indicateurs/:id} : Partial updates given fields of an existing resultIndicateurs, field will ignore if it is null
     *
     * @param id the id of the resultIndicateurs to save.
     * @param resultIndicateurs the resultIndicateurs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultIndicateurs,
     * or with status {@code 400 (Bad Request)} if the resultIndicateurs is not valid,
     * or with status {@code 404 (Not Found)} if the resultIndicateurs is not found,
     * or with status {@code 500 (Internal Server Error)} if the resultIndicateurs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/result-indicateurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ResultIndicateurs> partialUpdateResultIndicateurs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResultIndicateurs resultIndicateurs
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResultIndicateurs partially : {}, {}", id, resultIndicateurs);
        if (resultIndicateurs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resultIndicateurs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resultIndicateursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResultIndicateurs> result = resultIndicateursRepository
            .findById(resultIndicateurs.getId())
            .map(
                existingResultIndicateurs -> {
                    if (resultIndicateurs.getAnnee() != null) {
                        existingResultIndicateurs.setAnnee(resultIndicateurs.getAnnee());
                    }
                    if (resultIndicateurs.getObservation() != null) {
                        existingResultIndicateurs.setObservation(resultIndicateurs.getObservation());
                    }

                    return existingResultIndicateurs;
                }
            )
            .map(resultIndicateursRepository::save)
            .map(
                savedResultIndicateurs -> {
                    resultIndicateursSearchRepository.save(savedResultIndicateurs);

                    return savedResultIndicateurs;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resultIndicateurs.getId().toString())
        );
    }

    /**
     * {@code GET  /result-indicateurs} : get all the resultIndicateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resultIndicateurs in body.
     */
    @GetMapping("/result-indicateurs")
    public ResponseEntity<List<ResultIndicateurs>> getAllResultIndicateurs(Pageable pageable) {
        log.debug("REST request to get a page of ResultIndicateurs");
        Page<ResultIndicateurs> page = resultIndicateursRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /result-indicateurs/:id} : get the "id" resultIndicateurs.
     *
     * @param id the id of the resultIndicateurs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resultIndicateurs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/result-indicateurs/{id}")
    public ResponseEntity<ResultIndicateurs> getResultIndicateurs(@PathVariable Long id) {
        log.debug("REST request to get ResultIndicateurs : {}", id);
        Optional<ResultIndicateurs> resultIndicateurs = resultIndicateursRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resultIndicateurs);
    }

    /**
     * {@code DELETE  /result-indicateurs/:id} : delete the "id" resultIndicateurs.
     *
     * @param id the id of the resultIndicateurs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/result-indicateurs/{id}")
    public ResponseEntity<Void> deleteResultIndicateurs(@PathVariable Long id) {
        log.debug("REST request to delete ResultIndicateurs : {}", id);
        resultIndicateursRepository.deleteById(id);
        resultIndicateursSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/result-indicateurs?query=:query} : search for the resultIndicateurs corresponding
     * to the query.
     *
     * @param query the query of the resultIndicateurs search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/result-indicateurs")
    public ResponseEntity<List<ResultIndicateurs>> searchResultIndicateurs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ResultIndicateurs for query {}", query);
        Page<ResultIndicateurs> page = resultIndicateursSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
