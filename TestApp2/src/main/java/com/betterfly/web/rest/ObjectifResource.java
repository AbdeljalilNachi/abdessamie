package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.Objectif;
import com.betterfly.repository.ObjectifRepository;
import com.betterfly.repository.search.ObjectifSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.Objectif}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ObjectifResource {

    private final Logger log = LoggerFactory.getLogger(ObjectifResource.class);

    private static final String ENTITY_NAME = "objectif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjectifRepository objectifRepository;

    private final ObjectifSearchRepository objectifSearchRepository;

    public ObjectifResource(ObjectifRepository objectifRepository, ObjectifSearchRepository objectifSearchRepository) {
        this.objectifRepository = objectifRepository;
        this.objectifSearchRepository = objectifSearchRepository;
    }

    /**
     * {@code POST  /objectifs} : Create a new objectif.
     *
     * @param objectif the objectif to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objectif, or with status {@code 400 (Bad Request)} if the objectif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/objectifs")
    public ResponseEntity<Objectif> createObjectif(@RequestBody Objectif objectif) throws URISyntaxException {
        log.debug("REST request to save Objectif : {}", objectif);
        if (objectif.getId() != null) {
            throw new BadRequestAlertException("A new objectif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Objectif result = objectifRepository.save(objectif);
        objectifSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/objectifs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /objectifs/:id} : Updates an existing objectif.
     *
     * @param id the id of the objectif to save.
     * @param objectif the objectif to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objectif,
     * or with status {@code 400 (Bad Request)} if the objectif is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objectif couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/objectifs/{id}")
    public ResponseEntity<Objectif> updateObjectif(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Objectif objectif
    ) throws URISyntaxException {
        log.debug("REST request to update Objectif : {}, {}", id, objectif);
        if (objectif.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objectif.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objectifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Objectif result = objectifRepository.save(objectif);
        objectifSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, objectif.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /objectifs/:id} : Partial updates given fields of an existing objectif, field will ignore if it is null
     *
     * @param id the id of the objectif to save.
     * @param objectif the objectif to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objectif,
     * or with status {@code 400 (Bad Request)} if the objectif is not valid,
     * or with status {@code 404 (Not Found)} if the objectif is not found,
     * or with status {@code 500 (Internal Server Error)} if the objectif couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/objectifs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Objectif> partialUpdateObjectif(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Objectif objectif
    ) throws URISyntaxException {
        log.debug("REST request to partial update Objectif partially : {}, {}", id, objectif);
        if (objectif.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objectif.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objectifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Objectif> result = objectifRepository
            .findById(objectif.getId())
            .map(
                existingObjectif -> {
                    if (objectif.getAxedelapolitiqueqse() != null) {
                        existingObjectif.setAxedelapolitiqueqse(objectif.getAxedelapolitiqueqse());
                    }
                    if (objectif.getObjectifqse() != null) {
                        existingObjectif.setObjectifqse(objectif.getObjectifqse());
                    }
                    if (objectif.getOrigine() != null) {
                        existingObjectif.setOrigine(objectif.getOrigine());
                    }

                    return existingObjectif;
                }
            )
            .map(objectifRepository::save)
            .map(
                savedObjectif -> {
                    objectifSearchRepository.save(savedObjectif);

                    return savedObjectif;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, objectif.getId().toString())
        );
    }

    /**
     * {@code GET  /objectifs} : get all the objectifs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objectifs in body.
     */
    @GetMapping("/objectifs")
    public ResponseEntity<List<Objectif>> getAllObjectifs(Pageable pageable) {
        log.debug("REST request to get a page of Objectifs");
        Page<Objectif> page = objectifRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /objectifs/:id} : get the "id" objectif.
     *
     * @param id the id of the objectif to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objectif, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/objectifs/{id}")
    public ResponseEntity<Objectif> getObjectif(@PathVariable Long id) {
        log.debug("REST request to get Objectif : {}", id);
        Optional<Objectif> objectif = objectifRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(objectif);
    }

    /**
     * {@code DELETE  /objectifs/:id} : delete the "id" objectif.
     *
     * @param id the id of the objectif to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/objectifs/{id}")
    public ResponseEntity<Void> deleteObjectif(@PathVariable Long id) {
        log.debug("REST request to delete Objectif : {}", id);
        objectifRepository.deleteById(id);
        objectifSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/objectifs?query=:query} : search for the objectif corresponding
     * to the query.
     *
     * @param query the query of the objectif search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/objectifs")
    public ResponseEntity<List<Objectif>> searchObjectifs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Objectifs for query {}", query);
        Page<Objectif> page = objectifSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
