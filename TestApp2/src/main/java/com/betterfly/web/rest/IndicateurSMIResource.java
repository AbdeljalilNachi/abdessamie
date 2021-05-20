package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.IndicateurSMI;
import com.betterfly.repository.IndicateurSMIRepository;
import com.betterfly.repository.search.IndicateurSMISearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.IndicateurSMI}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IndicateurSMIResource {

    private final Logger log = LoggerFactory.getLogger(IndicateurSMIResource.class);

    private static final String ENTITY_NAME = "indicateurSMI";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndicateurSMIRepository indicateurSMIRepository;

    private final IndicateurSMISearchRepository indicateurSMISearchRepository;

    public IndicateurSMIResource(
        IndicateurSMIRepository indicateurSMIRepository,
        IndicateurSMISearchRepository indicateurSMISearchRepository
    ) {
        this.indicateurSMIRepository = indicateurSMIRepository;
        this.indicateurSMISearchRepository = indicateurSMISearchRepository;
    }

    /**
     * {@code POST  /indicateur-smis} : Create a new indicateurSMI.
     *
     * @param indicateurSMI the indicateurSMI to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indicateurSMI, or with status {@code 400 (Bad Request)} if the indicateurSMI has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/indicateur-smis")
    public ResponseEntity<IndicateurSMI> createIndicateurSMI(@RequestBody IndicateurSMI indicateurSMI) throws URISyntaxException {
        log.debug("REST request to save IndicateurSMI : {}", indicateurSMI);
        if (indicateurSMI.getId() != null) {
            throw new BadRequestAlertException("A new indicateurSMI cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndicateurSMI result = indicateurSMIRepository.save(indicateurSMI);
        indicateurSMISearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/indicateur-smis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /indicateur-smis/:id} : Updates an existing indicateurSMI.
     *
     * @param id the id of the indicateurSMI to save.
     * @param indicateurSMI the indicateurSMI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indicateurSMI,
     * or with status {@code 400 (Bad Request)} if the indicateurSMI is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indicateurSMI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/indicateur-smis/{id}")
    public ResponseEntity<IndicateurSMI> updateIndicateurSMI(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndicateurSMI indicateurSMI
    ) throws URISyntaxException {
        log.debug("REST request to update IndicateurSMI : {}, {}", id, indicateurSMI);
        if (indicateurSMI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indicateurSMI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indicateurSMIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IndicateurSMI result = indicateurSMIRepository.save(indicateurSMI);
        indicateurSMISearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indicateurSMI.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /indicateur-smis/:id} : Partial updates given fields of an existing indicateurSMI, field will ignore if it is null
     *
     * @param id the id of the indicateurSMI to save.
     * @param indicateurSMI the indicateurSMI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indicateurSMI,
     * or with status {@code 400 (Bad Request)} if the indicateurSMI is not valid,
     * or with status {@code 404 (Not Found)} if the indicateurSMI is not found,
     * or with status {@code 500 (Internal Server Error)} if the indicateurSMI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/indicateur-smis/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<IndicateurSMI> partialUpdateIndicateurSMI(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndicateurSMI indicateurSMI
    ) throws URISyntaxException {
        log.debug("REST request to partial update IndicateurSMI partially : {}, {}", id, indicateurSMI);
        if (indicateurSMI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indicateurSMI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indicateurSMIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndicateurSMI> result = indicateurSMIRepository
            .findById(indicateurSMI.getId())
            .map(
                existingIndicateurSMI -> {
                    if (indicateurSMI.getDateIdentification() != null) {
                        existingIndicateurSMI.setDateIdentification(indicateurSMI.getDateIdentification());
                    }
                    if (indicateurSMI.getIndicateur() != null) {
                        existingIndicateurSMI.setIndicateur(indicateurSMI.getIndicateur());
                    }
                    if (indicateurSMI.getFormuleCalcul() != null) {
                        existingIndicateurSMI.setFormuleCalcul(indicateurSMI.getFormuleCalcul());
                    }
                    if (indicateurSMI.getCible() != null) {
                        existingIndicateurSMI.setCible(indicateurSMI.getCible());
                    }
                    if (indicateurSMI.getSeuilTolerance() != null) {
                        existingIndicateurSMI.setSeuilTolerance(indicateurSMI.getSeuilTolerance());
                    }
                    if (indicateurSMI.getUnite() != null) {
                        existingIndicateurSMI.setUnite(indicateurSMI.getUnite());
                    }
                    if (indicateurSMI.getPeriodicite() != null) {
                        existingIndicateurSMI.setPeriodicite(indicateurSMI.getPeriodicite());
                    }
                    if (indicateurSMI.getObservations() != null) {
                        existingIndicateurSMI.setObservations(indicateurSMI.getObservations());
                    }
                    if (indicateurSMI.getVigueur() != null) {
                        existingIndicateurSMI.setVigueur(indicateurSMI.getVigueur());
                    }

                    return existingIndicateurSMI;
                }
            )
            .map(indicateurSMIRepository::save)
            .map(
                savedIndicateurSMI -> {
                    indicateurSMISearchRepository.save(savedIndicateurSMI);

                    return savedIndicateurSMI;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indicateurSMI.getId().toString())
        );
    }

    /**
     * {@code GET  /indicateur-smis} : get all the indicateurSMIS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indicateurSMIS in body.
     */
    @GetMapping("/indicateur-smis")
    public ResponseEntity<List<IndicateurSMI>> getAllIndicateurSMIS(Pageable pageable) {
        log.debug("REST request to get a page of IndicateurSMIS");
        Page<IndicateurSMI> page = indicateurSMIRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /indicateur-smis/:id} : get the "id" indicateurSMI.
     *
     * @param id the id of the indicateurSMI to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indicateurSMI, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/indicateur-smis/{id}")
    public ResponseEntity<IndicateurSMI> getIndicateurSMI(@PathVariable Long id) {
        log.debug("REST request to get IndicateurSMI : {}", id);
        Optional<IndicateurSMI> indicateurSMI = indicateurSMIRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(indicateurSMI);
    }

    /**
     * {@code DELETE  /indicateur-smis/:id} : delete the "id" indicateurSMI.
     *
     * @param id the id of the indicateurSMI to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/indicateur-smis/{id}")
    public ResponseEntity<Void> deleteIndicateurSMI(@PathVariable Long id) {
        log.debug("REST request to delete IndicateurSMI : {}", id);
        indicateurSMIRepository.deleteById(id);
        indicateurSMISearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/indicateur-smis?query=:query} : search for the indicateurSMI corresponding
     * to the query.
     *
     * @param query the query of the indicateurSMI search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/indicateur-smis")
    public ResponseEntity<List<IndicateurSMI>> searchIndicateurSMIS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IndicateurSMIS for query {}", query);
        Page<IndicateurSMI> page = indicateurSMISearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
