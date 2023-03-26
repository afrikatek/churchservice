package com.afrikatek.churchservice.service;

import com.afrikatek.churchservice.domain.League;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link League}.
 */
public interface LeagueService {
    /**
     * Save a league.
     *
     * @param league the entity to save.
     * @return the persisted entity.
     */
    League save(League league);

    /**
     * Updates a league.
     *
     * @param league the entity to update.
     * @return the persisted entity.
     */
    League update(League league);

    /**
     * Partially updates a league.
     *
     * @param league the entity to update partially.
     * @return the persisted entity.
     */
    Optional<League> partialUpdate(League league);

    /**
     * Get all the leagues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<League> findAll(Pageable pageable);

    /**
     * Get the "id" league.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<League> findOne(Long id);

    /**
     * Delete the "id" league.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
