package com.afrikatek.churchservice.service;

import com.afrikatek.churchservice.domain.Ministry;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Ministry}.
 */
public interface MinistryService {
    /**
     * Save a ministry.
     *
     * @param ministry the entity to save.
     * @return the persisted entity.
     */
    Ministry save(Ministry ministry);

    /**
     * Updates a ministry.
     *
     * @param ministry the entity to update.
     * @return the persisted entity.
     */
    Ministry update(Ministry ministry);

    /**
     * Partially updates a ministry.
     *
     * @param ministry the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ministry> partialUpdate(Ministry ministry);

    /**
     * Get all the ministries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ministry> findAll(Pageable pageable);

    /**
     * Get the "id" ministry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ministry> findOne(Long id);

    /**
     * Delete the "id" ministry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
