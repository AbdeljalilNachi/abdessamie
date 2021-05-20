package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.ProcessusSMI;
import com.betterfly.repository.ProcessusSMIRepository;
import com.betterfly.repository.search.ProcessusSMISearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.ProcessusSMI}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProcessusSMIResource {

    private final Logger log = LoggerFactory.getLogger(ProcessusSMIResource.class);

    private static final String ENTITY_NAME = "processusSMI";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessusSMIRepository processusSMIRepository;

    private final ProcessusSMISearchRepository processusSMISearchRepository;

    public ProcessusSMIResource(ProcessusSMIRepository processusSMIRepository, ProcessusSMISearchRepository processusSMISearchRepository) {
        this.processusSMIRepository = processusSMIRepository;
        this.processusSMISearchRepository = processusSMISearchRepository;
    }

    /**
     * {@code POST  /processus-smis} : Create a new processusSMI.
     *
     * @param processusSMI the processusSMI to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processusSMI, or with status {@code 400 (Bad Request)} if the processusSMI has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/processus-smis")
    public ResponseEntity<ProcessusSMI> createProcessusSMI(@RequestBody ProcessusSMI processusSMI) throws URISyntaxException {
        log.debug("REST request to save ProcessusSMI : {}", processusSMI);
        if (processusSMI.getId() != null) {
            throw new BadRequestAlertException("A new processusSMI cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessusSMI result = processusSMIRepository.save(processusSMI);
        processusSMISearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/processus-smis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /processus-smis/:id} : Updates an existing processusSMI.
     *
     * @param id the id of the processusSMI to save.
     * @param processusSMI the processusSMI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processusSMI,
     * or with status {@code 400 (Bad Request)} if the processusSMI is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processusSMI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/processus-smis/{id}")
    public ResponseEntity<ProcessusSMI> updateProcessusSMI(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessusSMI processusSMI
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessusSMI : {}, {}", id, processusSMI);
        if (processusSMI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processusSMI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processusSMIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessusSMI result = processusSMIRepository.save(processusSMI);
        processusSMISearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processusSMI.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /processus-smis/:id} : Partial updates given fields of an existing processusSMI, field will ignore if it is null
     *
     * @param id the id of the processusSMI to save.
     * @param processusSMI the processusSMI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processusSMI,
     * or with status {@code 400 (Bad Request)} if the processusSMI is not valid,
     * or with status {@code 404 (Not Found)} if the processusSMI is not found,
     * or with status {@code 500 (Internal Server Error)} if the processusSMI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/processus-smis/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProcessusSMI> partialUpdateProcessusSMI(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessusSMI processusSMI
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessusSMI partially : {}, {}", id, processusSMI);
        if (processusSMI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processusSMI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processusSMIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessusSMI> result = processusSMIRepository
            .findById(processusSMI.getId())
            .map(
                existingProcessusSMI -> {
                    if (processusSMI.getProcessus() != null) {
                        existingProcessusSMI.setProcessus(processusSMI.getProcessus());
                    }
                    if (processusSMI.getDate() != null) {
                        existingProcessusSMI.setDate(processusSMI.getDate());
                    }
                    if (processusSMI.getVersion() != null) {
                        existingProcessusSMI.setVersion(processusSMI.getVersion());
                    }
                    if (processusSMI.getFinalite() != null) {
                        existingProcessusSMI.setFinalite(processusSMI.getFinalite());
                    }
                    if (processusSMI.getFicheProcessus() != null) {
                        existingProcessusSMI.setFicheProcessus(processusSMI.getFicheProcessus());
                    }
                    if (processusSMI.getFicheProcessusContentType() != null) {
                        existingProcessusSMI.setFicheProcessusContentType(processusSMI.getFicheProcessusContentType());
                    }
                    if (processusSMI.getType() != null) {
                        existingProcessusSMI.setType(processusSMI.getType());
                    }
                    if (processusSMI.getVigueur() != null) {
                        existingProcessusSMI.setVigueur(processusSMI.getVigueur());
                    }

                    return existingProcessusSMI;
                }
            )
            .map(processusSMIRepository::save)
            .map(
                savedProcessusSMI -> {
                    processusSMISearchRepository.save(savedProcessusSMI);

                    return savedProcessusSMI;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processusSMI.getId().toString())
        );
    }

    /**
     * {@code GET  /processus-smis} : get all the processusSMIS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processusSMIS in body.
     */
    @GetMapping("/processus-smis")
    public ResponseEntity<List<ProcessusSMI>> getAllProcessusSMIS(Pageable pageable) {
        log.debug("REST request to get a page of ProcessusSMIS");
        Page<ProcessusSMI> page = processusSMIRepository.findAll(pageable);
        // for(ProcessusSMI processussmi:page)
        //	processusSMISearchRepository.save(processussmi);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /processus-smis/:id} : get the "id" processusSMI.
     *
     * @param id the id of the processusSMI to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processusSMI, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/processus-smis/{id}")
    public ResponseEntity<ProcessusSMI> getProcessusSMI(@PathVariable Long id) {
        log.debug("REST request to get ProcessusSMI : {}", id);
        Optional<ProcessusSMI> processusSMI = processusSMIRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processusSMI);
    }

    /**
     * {@code DELETE  /processus-smis/:id} : delete the "id" processusSMI.
     *
     * @param id the id of the processusSMI to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/processus-smis/{id}")
    public ResponseEntity<Void> deleteProcessusSMI(@PathVariable Long id) {
        log.debug("REST request to delete ProcessusSMI : {}", id);
        processusSMIRepository.deleteById(id);
        processusSMISearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/processus-smis?query=:query} : search for the processusSMI corresponding
     * to the query.
     *
     * @param query the query of the processusSMI search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/processus-smis")
    public ResponseEntity<List<ProcessusSMI>> searchProcessusSMIS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProcessusSMIS for query {}", query);
        Page<ProcessusSMI> page = processusSMISearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
