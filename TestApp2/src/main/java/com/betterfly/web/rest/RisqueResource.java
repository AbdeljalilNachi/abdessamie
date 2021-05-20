package com.betterfly.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.betterfly.domain.Risque;
import com.betterfly.repository.RisqueRepository;
import com.betterfly.repository.search.RisqueSearchRepository;
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
 * REST controller for managing {@link com.betterfly.domain.Risque}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RisqueResource {

    private final Logger log = LoggerFactory.getLogger(RisqueResource.class);

    private static final String ENTITY_NAME = "risque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RisqueRepository risqueRepository;

    private final RisqueSearchRepository risqueSearchRepository;

    public RisqueResource(RisqueRepository risqueRepository, RisqueSearchRepository risqueSearchRepository) {
        this.risqueRepository = risqueRepository;
        this.risqueSearchRepository = risqueSearchRepository;
    }

    /**
     * {@code POST  /risques} : Create a new risque.
     *
     * @param risque the risque to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new risque, or with status {@code 400 (Bad Request)} if the risque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/risques")
    public ResponseEntity<Risque> createRisque(@RequestBody Risque risque) throws URISyntaxException {
        log.debug("REST request to save Risque : {}", risque);
        if (risque.getId() != null) {
            throw new BadRequestAlertException("A new risque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Risque result = risqueRepository.save(risque);
        risqueSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/risques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /risques/:id} : Updates an existing risque.
     *
     * @param id the id of the risque to save.
     * @param risque the risque to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated risque,
     * or with status {@code 400 (Bad Request)} if the risque is not valid,
     * or with status {@code 500 (Internal Server Error)} if the risque couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/risques/{id}")
    public ResponseEntity<Risque> updateRisque(@PathVariable(value = "id", required = false) final Long id, @RequestBody Risque risque)
        throws URISyntaxException {
        log.debug("REST request to update Risque : {}, {}", id, risque);
        if (risque.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, risque.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!risqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Risque result = risqueRepository.save(risque);
        risqueSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, risque.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /risques/:id} : Partial updates given fields of an existing risque, field will ignore if it is null
     *
     * @param id the id of the risque to save.
     * @param risque the risque to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated risque,
     * or with status {@code 400 (Bad Request)} if the risque is not valid,
     * or with status {@code 404 (Not Found)} if the risque is not found,
     * or with status {@code 500 (Internal Server Error)} if the risque couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/risques/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Risque> partialUpdateRisque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Risque risque
    ) throws URISyntaxException {
        log.debug("REST request to partial update Risque partially : {}, {}", id, risque);
        if (risque.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, risque.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!risqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Risque> result = risqueRepository
            .findById(risque.getId())
            .map(
                existingRisque -> {
                    if (risque.getDateIdentification() != null) {
                        existingRisque.setDateIdentification(risque.getDateIdentification());
                    }
                    if (risque.getDescription() != null) {
                        existingRisque.setDescription(risque.getDescription());
                    }
                    if (risque.getCausePotentielle() != null) {
                        existingRisque.setCausePotentielle(risque.getCausePotentielle());
                    }
                    if (risque.getEffetPotentiel() != null) {
                        existingRisque.setEffetPotentiel(risque.getEffetPotentiel());
                    }
                    if (risque.getType() != null) {
                        existingRisque.setType(risque.getType());
                    }
                    if (risque.getGravite() != null) {
                        existingRisque.setGravite(risque.getGravite());
                    }
                    if (risque.getProbabilite() != null) {
                        existingRisque.setProbabilite(risque.getProbabilite());
                    }
                    if (risque.getCriticite() != null) {
                        existingRisque.setCriticite(risque.getCriticite());
                    }
                    if (risque.getTraitement() != null) {
                        existingRisque.setTraitement(risque.getTraitement());
                    }
                    if (risque.getCommentaire() != null) {
                        existingRisque.setCommentaire(risque.getCommentaire());
                    }
                    if (risque.getOrigine() != null) {
                        existingRisque.setOrigine(risque.getOrigine());
                    }

                    return existingRisque;
                }
            )
            .map(risqueRepository::save)
            .map(
                savedRisque -> {
                    risqueSearchRepository.save(savedRisque);

                    return savedRisque;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, risque.getId().toString())
        );
    }

    /**
     * {@code GET  /risques} : get all the risques.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of risques in body.
     */
    @GetMapping("/risques")
    public ResponseEntity<List<Risque>> getAllRisques(Pageable pageable) {
        log.debug("REST request to get a page of Risques");
        Page<Risque> page = risqueRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /risques/:id} : get the "id" risque.
     *
     * @param id the id of the risque to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the risque, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/risques/{id}")
    public ResponseEntity<Risque> getRisque(@PathVariable Long id) {
        log.debug("REST request to get Risque : {}", id);
        Optional<Risque> risque = risqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(risque);
    }

    /**
     * {@code DELETE  /risques/:id} : delete the "id" risque.
     *
     * @param id the id of the risque to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/risques/{id}")
    public ResponseEntity<Void> deleteRisque(@PathVariable Long id) {
        log.debug("REST request to delete Risque : {}", id);
        risqueRepository.deleteById(id);
        risqueSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/risques?query=:query} : search for the risque corresponding
     * to the query.
     *
     * @param query the query of the risque search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/risques")
    public ResponseEntity<List<Risque>> searchRisques(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Risques for query {}", query);
        Page<Risque> page = risqueSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
