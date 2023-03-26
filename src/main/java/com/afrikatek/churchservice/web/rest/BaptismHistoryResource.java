package com.afrikatek.churchservice.web.rest;

import com.afrikatek.churchservice.domain.BaptismHistory;
import com.afrikatek.churchservice.repository.BaptismHistoryRepository;
import com.afrikatek.churchservice.service.BaptismHistoryService;
import com.afrikatek.churchservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.afrikatek.churchservice.domain.BaptismHistory}.
 */
@RestController
@RequestMapping("/api")
public class BaptismHistoryResource {

    private final Logger log = LoggerFactory.getLogger(BaptismHistoryResource.class);

    private static final String ENTITY_NAME = "baptismHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BaptismHistoryService baptismHistoryService;

    private final BaptismHistoryRepository baptismHistoryRepository;

    public BaptismHistoryResource(BaptismHistoryService baptismHistoryService, BaptismHistoryRepository baptismHistoryRepository) {
        this.baptismHistoryService = baptismHistoryService;
        this.baptismHistoryRepository = baptismHistoryRepository;
    }

    /**
     * {@code POST  /baptism-histories} : Create a new baptismHistory.
     *
     * @param baptismHistory the baptismHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baptismHistory, or with status {@code 400 (Bad Request)} if the baptismHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/baptism-histories")
    public ResponseEntity<BaptismHistory> createBaptismHistory(@Valid @RequestBody BaptismHistory baptismHistory)
        throws URISyntaxException {
        log.debug("REST request to save BaptismHistory : {}", baptismHistory);
        if (baptismHistory.getId() != null) {
            throw new BadRequestAlertException("A new baptismHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BaptismHistory result = baptismHistoryService.save(baptismHistory);
        return ResponseEntity
            .created(new URI("/api/baptism-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /baptism-histories/:id} : Updates an existing baptismHistory.
     *
     * @param id the id of the baptismHistory to save.
     * @param baptismHistory the baptismHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baptismHistory,
     * or with status {@code 400 (Bad Request)} if the baptismHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the baptismHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/baptism-histories/{id}")
    public ResponseEntity<BaptismHistory> updateBaptismHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BaptismHistory baptismHistory
    ) throws URISyntaxException {
        log.debug("REST request to update BaptismHistory : {}, {}", id, baptismHistory);
        if (baptismHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baptismHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baptismHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BaptismHistory result = baptismHistoryService.update(baptismHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baptismHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /baptism-histories/:id} : Partial updates given fields of an existing baptismHistory, field will ignore if it is null
     *
     * @param id the id of the baptismHistory to save.
     * @param baptismHistory the baptismHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baptismHistory,
     * or with status {@code 400 (Bad Request)} if the baptismHistory is not valid,
     * or with status {@code 404 (Not Found)} if the baptismHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the baptismHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/baptism-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BaptismHistory> partialUpdateBaptismHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BaptismHistory baptismHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update BaptismHistory partially : {}, {}", id, baptismHistory);
        if (baptismHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baptismHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baptismHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BaptismHistory> result = baptismHistoryService.partialUpdate(baptismHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baptismHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /baptism-histories} : get all the baptismHistories.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baptismHistories in body.
     */
    @GetMapping("/baptism-histories")
    public ResponseEntity<List<BaptismHistory>> getAllBaptismHistories(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("profile-is-null".equals(filter)) {
            log.debug("REST request to get all BaptismHistorys where profile is null");
            return new ResponseEntity<>(baptismHistoryService.findAllWhereProfileIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of BaptismHistories");
        Page<BaptismHistory> page = baptismHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /baptism-histories/:id} : get the "id" baptismHistory.
     *
     * @param id the id of the baptismHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baptismHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/baptism-histories/{id}")
    public ResponseEntity<BaptismHistory> getBaptismHistory(@PathVariable Long id) {
        log.debug("REST request to get BaptismHistory : {}", id);
        Optional<BaptismHistory> baptismHistory = baptismHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(baptismHistory);
    }

    /**
     * {@code DELETE  /baptism-histories/:id} : delete the "id" baptismHistory.
     *
     * @param id the id of the baptismHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/baptism-histories/{id}")
    public ResponseEntity<Void> deleteBaptismHistory(@PathVariable Long id) {
        log.debug("REST request to delete BaptismHistory : {}", id);
        baptismHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
