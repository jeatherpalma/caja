package com.worknest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.worknest.domain.HistorialAcciones;

import com.worknest.repository.HistorialAccionesRepository;
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
 * REST controller for managing HistorialAcciones.
 */
@RestController
@RequestMapping("/api")
public class HistorialAccionesResource {

    private final Logger log = LoggerFactory.getLogger(HistorialAccionesResource.class);

    private static final String ENTITY_NAME = "historialAcciones";

    private final HistorialAccionesRepository historialAccionesRepository;

    public HistorialAccionesResource(HistorialAccionesRepository historialAccionesRepository) {
        this.historialAccionesRepository = historialAccionesRepository;
    }

    /**
     * POST  /historial-acciones : Create a new historialAcciones.
     *
     * @param historialAcciones the historialAcciones to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historialAcciones, or with status 400 (Bad Request) if the historialAcciones has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/historial-acciones")
    @Timed
    public ResponseEntity<HistorialAcciones> createHistorialAcciones(@Valid @RequestBody HistorialAcciones historialAcciones) throws URISyntaxException {
        log.debug("REST request to save HistorialAcciones : {}", historialAcciones);
        if (historialAcciones.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new historialAcciones cannot already have an ID")).body(null);
        }
        HistorialAcciones result = historialAccionesRepository.save(historialAcciones);
        return ResponseEntity.created(new URI("/api/historial-acciones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /historial-acciones : Updates an existing historialAcciones.
     *
     * @param historialAcciones the historialAcciones to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historialAcciones,
     * or with status 400 (Bad Request) if the historialAcciones is not valid,
     * or with status 500 (Internal Server Error) if the historialAcciones couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/historial-acciones")
    @Timed
    public ResponseEntity<HistorialAcciones> updateHistorialAcciones(@Valid @RequestBody HistorialAcciones historialAcciones) throws URISyntaxException {
        log.debug("REST request to update HistorialAcciones : {}", historialAcciones);
        if (historialAcciones.getId() == null) {
            return createHistorialAcciones(historialAcciones);
        }
        HistorialAcciones result = historialAccionesRepository.save(historialAcciones);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historialAcciones.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historial-acciones : get all the historialAcciones.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of historialAcciones in body
     */
    @GetMapping("/historial-acciones")
    @Timed
    public List<HistorialAcciones> getAllHistorialAcciones() {
        log.debug("REST request to get all HistorialAcciones");
        return historialAccionesRepository.findAll();
        }

    /**
     * GET  /historial-acciones/:id : get the "id" historialAcciones.
     *
     * @param id the id of the historialAcciones to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historialAcciones, or with status 404 (Not Found)
     */
    @GetMapping("/historial-acciones/{id}")
    @Timed
    public ResponseEntity<HistorialAcciones> getHistorialAcciones(@PathVariable Long id) {
        log.debug("REST request to get HistorialAcciones : {}", id);
        HistorialAcciones historialAcciones = historialAccionesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(historialAcciones));
    }

    /**
     * DELETE  /historial-acciones/:id : delete the "id" historialAcciones.
     *
     * @param id the id of the historialAcciones to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/historial-acciones/{id}")
    @Timed
    public ResponseEntity<Void> deleteHistorialAcciones(@PathVariable Long id) {
        log.debug("REST request to delete HistorialAcciones : {}", id);
        historialAccionesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
