package com.worknest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.worknest.domain.Cajas;

import com.worknest.repository.CajasRepository;
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
 * REST controller for managing Cajas.
 */
@RestController
@RequestMapping("/api")
public class CajasResource {

    private final Logger log = LoggerFactory.getLogger(CajasResource.class);

    private static final String ENTITY_NAME = "cajas";

    private final CajasRepository cajasRepository;

    public CajasResource(CajasRepository cajasRepository) {
        this.cajasRepository = cajasRepository;
    }

    /**
     * POST  /cajas : Create a new cajas.
     *
     * @param cajas the cajas to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cajas, or with status 400 (Bad Request) if the cajas has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cajas")
    @Timed
    public ResponseEntity<Cajas> createCajas(@Valid @RequestBody Cajas cajas) throws URISyntaxException {
        log.debug("REST request to save Cajas : {}", cajas);
        if (cajas.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cajas cannot already have an ID")).body(null);
        }
        Cajas result = cajasRepository.save(cajas);
        return ResponseEntity.created(new URI("/api/cajas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cajas : Updates an existing cajas.
     *
     * @param cajas the cajas to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cajas,
     * or with status 400 (Bad Request) if the cajas is not valid,
     * or with status 500 (Internal Server Error) if the cajas couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cajas")
    @Timed
    public ResponseEntity<Cajas> updateCajas(@Valid @RequestBody Cajas cajas) throws URISyntaxException {
        log.debug("REST request to update Cajas : {}", cajas);
        if (cajas.getId() == null) {
            return createCajas(cajas);
        }
        Cajas result = cajasRepository.save(cajas);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cajas.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cajas : get all the cajas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cajas in body
     */
    @GetMapping("/cajas")
    @Timed
    public List<Cajas> getAllCajas() {
        log.debug("REST request to get all Cajas");
        return cajasRepository.findAll();
        }

    /**
     * GET  /cajas/:id : get the "id" cajas.
     *
     * @param id the id of the cajas to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cajas, or with status 404 (Not Found)
     */
    @GetMapping("/cajas/{id}")
    @Timed
    public ResponseEntity<Cajas> getCajas(@PathVariable Long id) {
        log.debug("REST request to get Cajas : {}", id);
        Cajas cajas = cajasRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cajas));
    }

    /**
     * DELETE  /cajas/:id : delete the "id" cajas.
     *
     * @param id the id of the cajas to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cajas/{id}")
    @Timed
    public ResponseEntity<Void> deleteCajas(@PathVariable Long id) {
        log.debug("REST request to delete Cajas : {}", id);
        cajasRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
