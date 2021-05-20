package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.Audit;
import com.betterfly.repository.AuditRepository;
import com.betterfly.repository.search.AuditSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.Audit}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AuditResource {

    private final Logger log = LoggerFactory.getLogger(AuditResource.class);

    private static final String ENTITY_NAME = "audit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditRepository auditRepository;

    private final AuditSearchRepository auditSearchRepository;

    public AuditResource(AuditRepository auditRepository, AuditSearchRepository auditSearchRepository) {
        this.auditRepository = auditRepository;
        this.auditSearchRepository = auditSearchRepository;
    }

    /**
     * {@code POST  /audits} : Create a new audit.
     *
     * @param audit the audit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new audit, or with status {@code 400 (Bad Request)} if the audit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audits")
    public ResponseEntity<Audit> createAudit(@RequestBody Audit audit) throws URISyntaxException {
        log.debug("REST request to save Audit : {}", audit);
        if (audit.getId() != null) {
            throw new BadRequestAlertException("A new audit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Audit result = auditRepository.save(audit);
        auditSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audits/:id} : Updates an existing audit.
     *
     * @param id the id of the audit to save.
     * @param audit the audit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated audit,
     * or with status {@code 400 (Bad Request)} if the audit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the audit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audits/{id}")
    public ResponseEntity<Audit> updateAudit(@PathVariable(value = "id", required = false) final Long id, @RequestBody Audit audit)
        throws URISyntaxException {
        log.debug("REST request to update Audit : {}, {}", id, audit);
        if (audit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, audit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Audit result = auditRepository.save(audit);
        auditSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, audit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /audits/:id} : Partial updates given fields of an existing audit, field will ignore if it is null
     *
     * @param id the id of the audit to save.
     * @param audit the audit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated audit,
     * or with status {@code 400 (Bad Request)} if the audit is not valid,
     * or with status {@code 404 (Not Found)} if the audit is not found,
     * or with status {@code 500 (Internal Server Error)} if the audit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/audits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Audit> partialUpdateAudit(@PathVariable(value = "id", required = false) final Long id, @RequestBody Audit audit)
        throws URISyntaxException {
        log.debug("REST request to partial update Audit partially : {}, {}", id, audit);
        if (audit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, audit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Audit> result = auditRepository
            .findById(audit.getId())
            .map(
                existingAudit -> {
                    if (audit.getDateAudit() != null) {
                        existingAudit.setDateAudit(audit.getDateAudit());
                    }
                    if (audit.getTypeAudit() != null) {
                        existingAudit.setTypeAudit(audit.getTypeAudit());
                    }
                    if (audit.getStandard() != null) {
                        existingAudit.setStandard(audit.getStandard());
                    }
                    if (audit.getiD() != null) {
                        existingAudit.setiD(audit.getiD());
                    }
                    if (audit.getStatut() != null) {
                        existingAudit.setStatut(audit.getStatut());
                    }
                    if (audit.getConclusion() != null) {
                        existingAudit.setConclusion(audit.getConclusion());
                    }

                    return existingAudit;
                }
            )
            .map(auditRepository::save)
            .map(
                savedAudit -> {
                    auditSearchRepository.save(savedAudit);

                    return savedAudit;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, audit.getId().toString())
        );
    }

    /**
     * {@code GET  /audits} : get all the audits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of audits in body.
     */
    @GetMapping("/audits")
    public ResponseEntity<List<Audit>> getAllAudits(Pageable pageable) {
        log.debug("REST request to get a page of Audits");
        Page<Audit> page = auditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audits/:id} : get the "id" audit.
     *
     * @param id the id of the audit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the audit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audits/{id}")
    public ResponseEntity<Audit> getAudit(@PathVariable Long id) {
        log.debug("REST request to get Audit : {}", id);
        Optional<Audit> audit = auditRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(audit);
    }

    /**
     * {@code DELETE  /audits/:id} : delete the "id" audit.
     *
     * @param id the id of the audit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audits/{id}")
    public ResponseEntity<Void> deleteAudit(@PathVariable Long id) {
        log.debug("REST request to delete Audit : {}", id);
        auditRepository.deleteById(id);
        auditSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/audits?query=:query} : search for the audit corresponding
     * to the query.
     *
     * @param query the query of the audit search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/audits")
    public ResponseEntity<List<Audit>> searchAudits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Audits for query {}", query);
        Page<Audit> page = auditSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
