package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.Reclamation;
import com.betterfly.repository.ReclamationRepository;
import com.betterfly.repository.search.ReclamationSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.Reclamation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReclamationResource {

    private final Logger log = LoggerFactory.getLogger(ReclamationResource.class);

    private static final String ENTITY_NAME = "reclamation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReclamationRepository reclamationRepository;

    private final ReclamationSearchRepository reclamationSearchRepository;

    public ReclamationResource(ReclamationRepository reclamationRepository, ReclamationSearchRepository reclamationSearchRepository) {
        this.reclamationRepository = reclamationRepository;
        this.reclamationSearchRepository = reclamationSearchRepository;
    }

    /**
     * {@code POST  /reclamations} : Create a new reclamation.
     *
     * @param reclamation the reclamation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reclamation, or with status {@code 400 (Bad Request)} if the reclamation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reclamations")
    public ResponseEntity<Reclamation> createReclamation(@RequestBody Reclamation reclamation) throws URISyntaxException {
        log.debug("REST request to save Reclamation : {}", reclamation);
        if (reclamation.getId() != null) {
            throw new BadRequestAlertException("A new reclamation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reclamation result = reclamationRepository.save(reclamation);
        reclamationSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/reclamations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reclamations/:id} : Updates an existing reclamation.
     *
     * @param id the id of the reclamation to save.
     * @param reclamation the reclamation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reclamation,
     * or with status {@code 400 (Bad Request)} if the reclamation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reclamation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reclamations/{id}")
    public ResponseEntity<Reclamation> updateReclamation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reclamation reclamation
    ) throws URISyntaxException {
        log.debug("REST request to update Reclamation : {}, {}", id, reclamation);
        if (reclamation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reclamation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reclamationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Reclamation result = reclamationRepository.save(reclamation);
        reclamationSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reclamation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reclamations/:id} : Partial updates given fields of an existing reclamation, field will ignore if it is null
     *
     * @param id the id of the reclamation to save.
     * @param reclamation the reclamation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reclamation,
     * or with status {@code 400 (Bad Request)} if the reclamation is not valid,
     * or with status {@code 404 (Not Found)} if the reclamation is not found,
     * or with status {@code 500 (Internal Server Error)} if the reclamation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reclamations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Reclamation> partialUpdateReclamation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reclamation reclamation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reclamation partially : {}, {}", id, reclamation);
        if (reclamation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reclamation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reclamationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reclamation> result = reclamationRepository
            .findById(reclamation.getId())
            .map(
                existingReclamation -> {
                    if (reclamation.getDate() != null) {
                        existingReclamation.setDate(reclamation.getDate());
                    }
                    if (reclamation.getDescription() != null) {
                        existingReclamation.setDescription(reclamation.getDescription());
                    }
                    if (reclamation.getJustifiee() != null) {
                        existingReclamation.setJustifiee(reclamation.getJustifiee());
                    }
                    if (reclamation.getClient() != null) {
                        existingReclamation.setClient(reclamation.getClient());
                    }
                    if (reclamation.getPiecejointe() != null) {
                        existingReclamation.setPiecejointe(reclamation.getPiecejointe());
                    }
                    if (reclamation.getPiecejointeContentType() != null) {
                        existingReclamation.setPiecejointeContentType(reclamation.getPiecejointeContentType());
                    }
                    if (reclamation.getOrigine() != null) {
                        existingReclamation.setOrigine(reclamation.getOrigine());
                    }

                    return existingReclamation;
                }
            )
            .map(reclamationRepository::save)
            .map(
                savedReclamation -> {
                    reclamationSearchRepository.save(savedReclamation);

                    return savedReclamation;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reclamation.getId().toString())
        );
    }

    /**
     * {@code GET  /reclamations} : get all the reclamations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reclamations in body.
     */
    @GetMapping("/reclamations")
    public ResponseEntity<List<Reclamation>> getAllReclamations(Pageable pageable) {
        log.debug("REST request to get a page of Reclamations");
        Page<Reclamation> page = reclamationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reclamations/:id} : get the "id" reclamation.
     *
     * @param id the id of the reclamation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reclamation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reclamations/{id}")
    public ResponseEntity<Reclamation> getReclamation(@PathVariable Long id) {
        log.debug("REST request to get Reclamation : {}", id);
        Optional<Reclamation> reclamation = reclamationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reclamation);
    }

    /**
     * {@code DELETE  /reclamations/:id} : delete the "id" reclamation.
     *
     * @param id the id of the reclamation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reclamations/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable Long id) {
        log.debug("REST request to delete Reclamation : {}", id);
        reclamationRepository.deleteById(id);
        reclamationSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/reclamations?query=:query} : search for the reclamation corresponding
     * to the query.
     *
     * @param query the query of the reclamation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/reclamations")
    public ResponseEntity<List<Reclamation>> searchReclamations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Reclamations for query {}", query);
        Page<Reclamation> page = reclamationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
