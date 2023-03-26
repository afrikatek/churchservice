package com.afrikatek.churchservice.web.rest;

import com.afrikatek.churchservice.domain.Ministry;
import com.afrikatek.churchservice.repository.MinistryRepository;
import com.afrikatek.churchservice.service.MinistryService;
import com.afrikatek.churchservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.afrikatek.churchservice.domain.Ministry}.
 */
@RestController
@RequestMapping("/api")
public class MinistryResource {

    private final Logger log = LoggerFactory.getLogger(MinistryResource.class);

    private static final String ENTITY_NAME = "ministry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MinistryService ministryService;

    private final MinistryRepository ministryRepository;

    public MinistryResource(MinistryService ministryService, MinistryRepository ministryRepository) {
        this.ministryService = ministryService;
        this.ministryRepository = ministryRepository;
    }

    /**
     * {@code POST  /ministries} : Create a new ministry.
     *
     * @param ministry the ministry to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ministry, or with status {@code 400 (Bad Request)} if the ministry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ministries")
    public ResponseEntity<Ministry> createMinistry(@Valid @RequestBody Ministry ministry) throws URISyntaxException {
        log.debug("REST request to save Ministry : {}", ministry);
        if (ministry.getId() != null) {
            throw new BadRequestAlertException("A new ministry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ministry result = ministryService.save(ministry);
        return ResponseEntity
            .created(new URI("/api/ministries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ministries/:id} : Updates an existing ministry.
     *
     * @param id the id of the ministry to save.
     * @param ministry the ministry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ministry,
     * or with status {@code 400 (Bad Request)} if the ministry is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ministry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ministries/{id}")
    public ResponseEntity<Ministry> updateMinistry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ministry ministry
    ) throws URISyntaxException {
        log.debug("REST request to update Ministry : {}, {}", id, ministry);
        if (ministry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ministry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ministryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ministry result = ministryService.update(ministry);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ministry.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ministries/:id} : Partial updates given fields of an existing ministry, field will ignore if it is null
     *
     * @param id the id of the ministry to save.
     * @param ministry the ministry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ministry,
     * or with status {@code 400 (Bad Request)} if the ministry is not valid,
     * or with status {@code 404 (Not Found)} if the ministry is not found,
     * or with status {@code 500 (Internal Server Error)} if the ministry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ministries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ministry> partialUpdateMinistry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ministry ministry
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ministry partially : {}, {}", id, ministry);
        if (ministry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ministry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ministryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ministry> result = ministryService.partialUpdate(ministry);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ministry.getId().toString())
        );
    }

    /**
     * {@code GET  /ministries} : get all the ministries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ministries in body.
     */
    @GetMapping("/ministries")
    public ResponseEntity<List<Ministry>> getAllMinistries(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ministries");
        Page<Ministry> page = ministryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ministries/:id} : get the "id" ministry.
     *
     * @param id the id of the ministry to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ministry, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ministries/{id}")
    public ResponseEntity<Ministry> getMinistry(@PathVariable Long id) {
        log.debug("REST request to get Ministry : {}", id);
        Optional<Ministry> ministry = ministryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ministry);
    }

    /**
     * {@code DELETE  /ministries/:id} : delete the "id" ministry.
     *
     * @param id the id of the ministry to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ministries/{id}")
    public ResponseEntity<Void> deleteMinistry(@PathVariable Long id) {
        log.debug("REST request to delete Ministry : {}", id);
        ministryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
