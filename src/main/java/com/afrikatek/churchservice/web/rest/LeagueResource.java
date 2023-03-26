package com.afrikatek.churchservice.web.rest;

import com.afrikatek.churchservice.domain.League;
import com.afrikatek.churchservice.repository.LeagueRepository;
import com.afrikatek.churchservice.service.LeagueService;
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
 * REST controller for managing {@link com.afrikatek.churchservice.domain.League}.
 */
@RestController
@RequestMapping("/api")
public class LeagueResource {

    private final Logger log = LoggerFactory.getLogger(LeagueResource.class);

    private static final String ENTITY_NAME = "league";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeagueService leagueService;

    private final LeagueRepository leagueRepository;

    public LeagueResource(LeagueService leagueService, LeagueRepository leagueRepository) {
        this.leagueService = leagueService;
        this.leagueRepository = leagueRepository;
    }

    /**
     * {@code POST  /leagues} : Create a new league.
     *
     * @param league the league to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new league, or with status {@code 400 (Bad Request)} if the league has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leagues")
    public ResponseEntity<League> createLeague(@Valid @RequestBody League league) throws URISyntaxException {
        log.debug("REST request to save League : {}", league);
        if (league.getId() != null) {
            throw new BadRequestAlertException("A new league cannot already have an ID", ENTITY_NAME, "idexists");
        }
        League result = leagueService.save(league);
        return ResponseEntity
            .created(new URI("/api/leagues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leagues/:id} : Updates an existing league.
     *
     * @param id the id of the league to save.
     * @param league the league to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated league,
     * or with status {@code 400 (Bad Request)} if the league is not valid,
     * or with status {@code 500 (Internal Server Error)} if the league couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leagues/{id}")
    public ResponseEntity<League> updateLeague(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody League league
    ) throws URISyntaxException {
        log.debug("REST request to update League : {}, {}", id, league);
        if (league.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, league.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leagueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        League result = leagueService.update(league);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, league.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /leagues/:id} : Partial updates given fields of an existing league, field will ignore if it is null
     *
     * @param id the id of the league to save.
     * @param league the league to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated league,
     * or with status {@code 400 (Bad Request)} if the league is not valid,
     * or with status {@code 404 (Not Found)} if the league is not found,
     * or with status {@code 500 (Internal Server Error)} if the league couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/leagues/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<League> partialUpdateLeague(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody League league
    ) throws URISyntaxException {
        log.debug("REST request to partial update League partially : {}, {}", id, league);
        if (league.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, league.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leagueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<League> result = leagueService.partialUpdate(league);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, league.getId().toString())
        );
    }

    /**
     * {@code GET  /leagues} : get all the leagues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leagues in body.
     */
    @GetMapping("/leagues")
    public ResponseEntity<List<League>> getAllLeagues(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Leagues");
        Page<League> page = leagueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leagues/:id} : get the "id" league.
     *
     * @param id the id of the league to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the league, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leagues/{id}")
    public ResponseEntity<League> getLeague(@PathVariable Long id) {
        log.debug("REST request to get League : {}", id);
        Optional<League> league = leagueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(league);
    }

    /**
     * {@code DELETE  /leagues/:id} : delete the "id" league.
     *
     * @param id the id of the league to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leagues/{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id) {
        log.debug("REST request to delete League : {}", id);
        leagueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
