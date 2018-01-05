package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Valor;

import com.mycompany.myapp.repository.ValorRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Valor.
 */
@RestController
@RequestMapping("/api")
public class ValorResource {

    private final Logger log = LoggerFactory.getLogger(ValorResource.class);

    private static final String ENTITY_NAME = "valor";

    private final ValorRepository valorRepository;

    public ValorResource(ValorRepository valorRepository) {
        this.valorRepository = valorRepository;
    }

    /**
     * POST  /valors : Create a new valor.
     *
     * @param valor the valor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new valor, or with status 400 (Bad Request) if the valor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/valors")
    @Timed
    public ResponseEntity<Valor> createValor(@RequestBody Valor valor) throws URISyntaxException {
        log.debug("REST request to save Valor : {}", valor);
        if (valor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new valor cannot already have an ID")).body(null);
        }
        Valor result = valorRepository.save(valor);
        return ResponseEntity.created(new URI("/api/valors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /valors : Updates an existing valor.
     *
     * @param valor the valor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated valor,
     * or with status 400 (Bad Request) if the valor is not valid,
     * or with status 500 (Internal Server Error) if the valor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/valors")
    @Timed
    public ResponseEntity<Valor> updateValor(@RequestBody Valor valor) throws URISyntaxException {
        log.debug("REST request to update Valor : {}", valor);
        if (valor.getId() == null) {
            return createValor(valor);
        }
        Valor result = valorRepository.save(valor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, valor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /valors : get all the valors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of valors in body
     */
    @GetMapping("/valors")
    @Timed
    public List<Valor> getAllValors() {
        log.debug("REST request to get all Valors");
        return valorRepository.findAll();
        }

    /**
     * GET  /valors/:id : get the "id" valor.
     *
     * @param id the id of the valor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the valor, or with status 404 (Not Found)
     */
    @GetMapping("/valors/{id}")
    @Timed
    public ResponseEntity<Valor> getValor(@PathVariable Long id) {
        log.debug("REST request to get Valor : {}", id);
        Valor valor = valorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(valor));
    }

    /**
     * DELETE  /valors/:id : delete the "id" valor.
     *
     * @param id the id of the valor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/valors/{id}")
    @Timed
    public ResponseEntity<Void> deleteValor(@PathVariable Long id) {
        log.debug("REST request to delete Valor : {}", id);
        valorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
