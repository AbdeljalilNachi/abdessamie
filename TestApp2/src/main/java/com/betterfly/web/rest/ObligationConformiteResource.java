package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.ObligationConformite;
import com.betterfly.repository.ObligationConformiteRepository;
import com.betterfly.repository.search.ObligationConformiteSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.ObligationConformite}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ObligationConformiteResource {

    private final Logger log = LoggerFactory.getLogger(ObligationConformiteResource.class);

    private static final String ENTITY_NAME = "obligationConformite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObligationConformiteRepository obligationConformiteRepository;

    private final ObligationConformiteSearchRepository obligationConformiteSearchRepository;

    public ObligationConformiteResource(
        ObligationConformiteRepository obligationConformiteRepository,
        ObligationConformiteSearchRepository obligationConformiteSearchRepository
    ) {
        this.obligationConformiteRepository = obligationConformiteRepository;
        this.obligationConformiteSearchRepository = obligationConformiteSearchRepository;
    }

    /**
     * {@code POST  /obligation-conformites} : Create a new obligationConformite.
     *
     * @param obligationConformite the obligationConformite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new obligationConformite, or with status {@code 400 (Bad Request)} if the obligationConformite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/obligation-conformites")
    public ResponseEntity<ObligationConformite> createObligationConformite(@RequestBody ObligationConformite obligationConformite)
        throws URISyntaxException {
        log.debug("REST request to save ObligationConformite : {}", obligationConformite);
        if (obligationConformite.getId() != null) {
            throw new BadRequestAlertException("A new obligationConformite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ObligationConformite result = obligationConformiteRepository.save(obligationConformite);
        obligationConformiteSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/obligation-conformites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /obligation-conformites/:id} : Updates an existing obligationConformite.
     *
     * @param id the id of the obligationConformite to save.
     * @param obligationConformite the obligationConformite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated obligationConformite,
     * or with status {@code 400 (Bad Request)} if the obligationConformite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the obligationConformite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/obligation-conformites/{id}")
    public ResponseEntity<ObligationConformite> updateObligationConformite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ObligationConformite obligationConformite
    ) throws URISyntaxException {
        log.debug("REST request to update ObligationConformite : {}, {}", id, obligationConformite);
        if (obligationConformite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, obligationConformite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!obligationConformiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ObligationConformite result = obligationConformiteRepository.save(obligationConformite);
        obligationConformiteSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, obligationConformite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /obligation-conformites/:id} : Partial updates given fields of an existing obligationConformite, field will ignore if it is null
     *
     * @param id the id of the obligationConformite to save.
     * @param obligationConformite the obligationConformite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated obligationConformite,
     * or with status {@code 400 (Bad Request)} if the obligationConformite is not valid,
     * or with status {@code 404 (Not Found)} if the obligationConformite is not found,
     * or with status {@code 500 (Internal Server Error)} if the obligationConformite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/obligation-conformites/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ObligationConformite> partialUpdateObligationConformite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ObligationConformite obligationConformite
    ) throws URISyntaxException {
        log.debug("REST request to partial update ObligationConformite partially : {}, {}", id, obligationConformite);
        if (obligationConformite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, obligationConformite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!obligationConformiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ObligationConformite> result = obligationConformiteRepository
            .findById(obligationConformite.getId())
            .map(
                existingObligationConformite -> {
                    if (obligationConformite.getDate() != null) {
                        existingObligationConformite.setDate(obligationConformite.getDate());
                    }
                    if (obligationConformite.getRubrique() != null) {
                        existingObligationConformite.setRubrique(obligationConformite.getRubrique());
                    }
                    if (obligationConformite.getReference() != null) {
                        existingObligationConformite.setReference(obligationConformite.getReference());
                    }
                    if (obligationConformite.getNum() != null) {
                        existingObligationConformite.setNum(obligationConformite.getNum());
                    }
                    if (obligationConformite.getExigence() != null) {
                        existingObligationConformite.setExigence(obligationConformite.getExigence());
                    }
                    if (obligationConformite.getApplicable() != null) {
                        existingObligationConformite.setApplicable(obligationConformite.getApplicable());
                    }
                    if (obligationConformite.getConforme() != null) {
                        existingObligationConformite.setConforme(obligationConformite.getConforme());
                    }
                    if (obligationConformite.getStatut() != null) {
                        existingObligationConformite.setStatut(obligationConformite.getStatut());
                    }
                    if (obligationConformite.getObservation() != null) {
                        existingObligationConformite.setObservation(obligationConformite.getObservation());
                    }
                    if (obligationConformite.getOrigine() != null) {
                        existingObligationConformite.setOrigine(obligationConformite.getOrigine());
                    }

                    return existingObligationConformite;
                }
            )
            .map(obligationConformiteRepository::save)
            .map(
                savedObligationConformite -> {
                    obligationConformiteSearchRepository.save(savedObligationConformite);

                    return savedObligationConformite;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, obligationConformite.getId().toString())
        );
    }

    /**
     * {@code GET  /obligation-conformites} : get all the obligationConformites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of obligationConformites in body.
     */
    @GetMapping("/obligation-conformites")
    public ResponseEntity<List<ObligationConformite>> getAllObligationConformites(Pageable pageable) {
        log.debug("REST request to get a page of ObligationConformites");
        Page<ObligationConformite> page = obligationConformiteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /obligation-conformites/:id} : get the "id" obligationConformite.
     *
     * @param id the id of the obligationConformite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the obligationConformite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/obligation-conformites/{id}")
    public ResponseEntity<ObligationConformite> getObligationConformite(@PathVariable Long id) {
        log.debug("REST request to get ObligationConformite : {}", id);
        Optional<ObligationConformite> obligationConformite = obligationConformiteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(obligationConformite);
    }

    /**
     * {@code DELETE  /obligation-conformites/:id} : delete the "id" obligationConformite.
     *
     * @param id the id of the obligationConformite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/obligation-conformites/{id}")
    public ResponseEntity<Void> deleteObligationConformite(@PathVariable Long id) {
        log.debug("REST request to delete ObligationConformite : {}", id);
        obligationConformiteRepository.deleteById(id);
        obligationConformiteSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/obligation-conformites?query=:query} : search for the obligationConformite corresponding
     * to the query.
     *
     * @param query the query of the obligationConformite search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/obligation-conformites")
    public ResponseEntity<List<ObligationConformite>> searchObligationConformites(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ObligationConformites for query {}", query);
        Page<ObligationConformite> page = obligationConformiteSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
