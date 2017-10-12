package com.worknest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.worknest.domain.Cajeros;

import com.worknest.repository.CajerosRepository;
import com.worknest.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cajeros.
 */
@RestController
@RequestMapping("/api")
public class CajerosResource {

    private final Logger log = LoggerFactory.getLogger(CajerosResource.class);

    private static final String ENTITY_NAME = "cajeros";

    private final CajerosRepository cajerosRepository;

    public CajerosResource(CajerosRepository cajerosRepository) {
        this.cajerosRepository = cajerosRepository;
    }

    /**
     * POST  /cajeros : Create a new cajeros.
     *
     * @param cajeros the cajeros to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cajeros, or with status 400 (Bad Request) if the cajeros has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cajeros")
    @Timed
    public ResponseEntity<Cajeros> createCajeros(@Valid @RequestBody Cajeros cajeros) throws URISyntaxException {
        log.debug("REST request to save Cajeros : {}", cajeros);
        if (cajeros.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cajeros cannot already have an ID")).body(null);
        }
        Cajeros result = cajerosRepository.save(cajeros);
        return ResponseEntity.created(new URI("/api/cajeros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cajeros : Updates an existing cajeros.
     *
     * @param cajeros the cajeros to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cajeros,
     * or with status 400 (Bad Request) if the cajeros is not valid,
     * or with status 500 (Internal Server Error) if the cajeros couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cajeros")
    @Timed
    public ResponseEntity<Cajeros> updateCajeros(@Valid @RequestBody Cajeros cajeros) throws URISyntaxException {
        log.debug("REST request to update Cajeros : {}", cajeros);
        if (cajeros.getId() == null) {
            return createCajeros(cajeros);
        }
        Cajeros result = cajerosRepository.save(cajeros);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cajeros.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cajeros : get all the cajeros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cajeros in body
     */
    @GetMapping("/cajeros")
    @Timed
    public List<Cajeros> getAllCajeros() {
        log.debug("REST request to get all Cajeros");
        return cajerosRepository.findAll();
        }

    /**
     * GET  /cajeros/:id : get the "id" cajeros.
     *
     * @param id the id of the cajeros to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cajeros, or with status 404 (Not Found)
     */
    @GetMapping("/cajeros/{id}")
    @Timed
    public ResponseEntity<Cajeros> getCajeros(@PathVariable Long id) {
        log.debug("REST request to get Cajeros : {}", id);
        Cajeros cajeros = cajerosRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cajeros));
    }

    /**
     * DELETE  /cajeros/:id : delete the "id" cajeros.
     *
     * @param id the id of the cajeros to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cajeros/{id}")
    @Timed
    public ResponseEntity<Void> deleteCajeros(@PathVariable Long id) {
        log.debug("REST request to delete Cajeros : {}", id);
        cajerosRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
