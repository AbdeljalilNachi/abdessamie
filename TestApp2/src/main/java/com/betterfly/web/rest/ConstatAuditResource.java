package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.ConstatAudit;
import com.betterfly.repository.ConstatAuditRepository;
import com.betterfly.repository.search.ConstatAuditSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.ConstatAudit}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConstatAuditResource {

    private final Logger log = LoggerFactory.getLogger(ConstatAuditResource.class);

    private static final String ENTITY_NAME = "constatAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConstatAuditRepository constatAuditRepository;

    private final ConstatAuditSearchRepository constatAuditSearchRepository;

    public ConstatAuditResource(ConstatAuditRepository constatAuditRepository, ConstatAuditSearchRepository constatAuditSearchRepository) {
        this.constatAuditRepository = constatAuditRepository;
        this.constatAuditSearchRepository = constatAuditSearchRepository;
    }

    /**
     * {@code POST  /constat-audits} : Create a new constatAudit.
     *
     * @param constatAudit the constatAudit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new constatAudit, or with status {@code 400 (Bad Request)} if the constatAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/constat-audits")
    public ResponseEntity<ConstatAudit> createConstatAudit(@RequestBody ConstatAudit constatAudit) throws URISyntaxException {
        log.debug("REST request to save ConstatAudit : {}", constatAudit);
        if (constatAudit.getId() != null) {
            throw new BadRequestAlertException("A new constatAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConstatAudit result = constatAuditRepository.save(constatAudit);
        constatAuditSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/constat-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /constat-audits/:id} : Updates an existing constatAudit.
     *
     * @param id the id of the constatAudit to save.
     * @param constatAudit the constatAudit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated constatAudit,
     * or with status {@code 400 (Bad Request)} if the constatAudit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the constatAudit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/constat-audits/{id}")
    public ResponseEntity<ConstatAudit> updateConstatAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConstatAudit constatAudit
    ) throws URISyntaxException {
        log.debug("REST request to update ConstatAudit : {}, {}", id, constatAudit);
        if (constatAudit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, constatAudit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!constatAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConstatAudit result = constatAuditRepository.save(constatAudit);
        constatAuditSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, constatAudit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /constat-audits/:id} : Partial updates given fields of an existing constatAudit, field will ignore if it is null
     *
     * @param id the id of the constatAudit to save.
     * @param constatAudit the constatAudit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated constatAudit,
     * or with status {@code 400 (Bad Request)} if the constatAudit is not valid,
     * or with status {@code 404 (Not Found)} if the constatAudit is not found,
     * or with status {@code 500 (Internal Server Error)} if the constatAudit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/constat-audits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ConstatAudit> partialUpdateConstatAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConstatAudit constatAudit
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConstatAudit partially : {}, {}", id, constatAudit);
        if (constatAudit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, constatAudit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!constatAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConstatAudit> result = constatAuditRepository
            .findById(constatAudit.getId())
            .map(
                existingConstatAudit -> {
                    if (constatAudit.getType() != null) {
                        existingConstatAudit.setType(constatAudit.getType());
                    }
                    if (constatAudit.getConstat() != null) {
                        existingConstatAudit.setConstat(constatAudit.getConstat());
                    }
                    if (constatAudit.getOrigine() != null) {
                        existingConstatAudit.setOrigine(constatAudit.getOrigine());
                    }

                    return existingConstatAudit;
                }
            )
            .map(constatAuditRepository::save)
            .map(
                savedConstatAudit -> {
                    constatAuditSearchRepository.save(savedConstatAudit);

                    return savedConstatAudit;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, constatAudit.getId().toString())
        );
    }

    /**
     * {@code GET  /constat-audits} : get all the constatAudits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of constatAudits in body.
     */
    @GetMapping("/constat-audits")
    public ResponseEntity<List<ConstatAudit>> getAllConstatAudits(Pageable pageable) {
        log.debug("REST request to get a page of ConstatAudits");
        Page<ConstatAudit> page = constatAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /constat-audits/:id} : get the "id" constatAudit.
     *
     * @param id the id of the constatAudit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the constatAudit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/constat-audits/{id}")
    public ResponseEntity<ConstatAudit> getConstatAudit(@PathVariable Long id) {
        log.debug("REST request to get ConstatAudit : {}", id);
        Optional<ConstatAudit> constatAudit = constatAuditRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(constatAudit);
    }

    /**
     * {@code DELETE  /constat-audits/:id} : delete the "id" constatAudit.
     *
     * @param id the id of the constatAudit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/constat-audits/{id}")
    public ResponseEntity<Void> deleteConstatAudit(@PathVariable Long id) {
        log.debug("REST request to delete ConstatAudit : {}", id);
        constatAuditRepository.deleteById(id);
        constatAuditSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/constat-audits?query=:query} : search for the constatAudit corresponding
     * to the query.
     *
     * @param query the query of the constatAudit search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/constat-audits")
    public ResponseEntity<List<ConstatAudit>> searchConstatAudits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ConstatAudits for query {}", query);
        Page<ConstatAudit> page = constatAuditSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
