package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.AutreAction;
import com.betterfly.repository.AutreActionRepository;
import com.betterfly.repository.search.AutreActionSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.AutreAction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AutreActionResource {

    private final Logger log = LoggerFactory.getLogger(AutreActionResource.class);

    private static final String ENTITY_NAME = "autreAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutreActionRepository autreActionRepository;

    private final AutreActionSearchRepository autreActionSearchRepository;

    public AutreActionResource(AutreActionRepository autreActionRepository, AutreActionSearchRepository autreActionSearchRepository) {
        this.autreActionRepository = autreActionRepository;
        this.autreActionSearchRepository = autreActionSearchRepository;
    }

    /**
     * {@code POST  /autre-actions} : Create a new autreAction.
     *
     * @param autreAction the autreAction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autreAction, or with status {@code 400 (Bad Request)} if the autreAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/autre-actions")
    public ResponseEntity<AutreAction> createAutreAction(@RequestBody AutreAction autreAction) throws URISyntaxException {
        log.debug("REST request to save AutreAction : {}", autreAction);
        if (autreAction.getId() != null) {
            throw new BadRequestAlertException("A new autreAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutreAction result = autreActionRepository.save(autreAction);
        autreActionSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/autre-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /autre-actions/:id} : Updates an existing autreAction.
     *
     * @param id the id of the autreAction to save.
     * @param autreAction the autreAction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autreAction,
     * or with status {@code 400 (Bad Request)} if the autreAction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autreAction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/autre-actions/{id}")
    public ResponseEntity<AutreAction> updateAutreAction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AutreAction autreAction
    ) throws URISyntaxException {
        log.debug("REST request to update AutreAction : {}, {}", id, autreAction);
        if (autreAction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autreAction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autreActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AutreAction result = autreActionRepository.save(autreAction);
        autreActionSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autreAction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /autre-actions/:id} : Partial updates given fields of an existing autreAction, field will ignore if it is null
     *
     * @param id the id of the autreAction to save.
     * @param autreAction the autreAction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autreAction,
     * or with status {@code 400 (Bad Request)} if the autreAction is not valid,
     * or with status {@code 404 (Not Found)} if the autreAction is not found,
     * or with status {@code 500 (Internal Server Error)} if the autreAction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/autre-actions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AutreAction> partialUpdateAutreAction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AutreAction autreAction
    ) throws URISyntaxException {
        log.debug("REST request to partial update AutreAction partially : {}, {}", id, autreAction);
        if (autreAction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autreAction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autreActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AutreAction> result = autreActionRepository
            .findById(autreAction.getId())
            .map(
                existingAutreAction -> {
                    if (autreAction.getOrigineAction() != null) {
                        existingAutreAction.setOrigineAction(autreAction.getOrigineAction());
                    }
                    if (autreAction.getOrigine() != null) {
                        existingAutreAction.setOrigine(autreAction.getOrigine());
                    }

                    return existingAutreAction;
                }
            )
            .map(autreActionRepository::save)
            .map(
                savedAutreAction -> {
                    autreActionSearchRepository.save(savedAutreAction);

                    return savedAutreAction;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autreAction.getId().toString())
        );
    }

    /**
     * {@code GET  /autre-actions} : get all the autreActions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autreActions in body.
     */
    @GetMapping("/autre-actions")
    public ResponseEntity<List<AutreAction>> getAllAutreActions(Pageable pageable) {
        log.debug("REST request to get a page of AutreActions");
        Page<AutreAction> page = autreActionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /autre-actions/:id} : get the "id" autreAction.
     *
     * @param id the id of the autreAction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autreAction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/autre-actions/{id}")
    public ResponseEntity<AutreAction> getAutreAction(@PathVariable Long id) {
        log.debug("REST request to get AutreAction : {}", id);
        Optional<AutreAction> autreAction = autreActionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(autreAction);
    }

    /**
     * {@code DELETE  /autre-actions/:id} : delete the "id" autreAction.
     *
     * @param id the id of the autreAction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/autre-actions/{id}")
    public ResponseEntity<Void> deleteAutreAction(@PathVariable Long id) {
        log.debug("REST request to delete AutreAction : {}", id);
        autreActionRepository.deleteById(id);
        autreActionSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/autre-actions?query=:query} : search for the autreAction corresponding
     * to the query.
     *
     * @param query the query of the autreAction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/autre-actions")
    public ResponseEntity<List<AutreAction>> searchAutreActions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AutreActions for query {}", query);
        Page<AutreAction> page = autreActionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
