package com.afrikatek.churchservice.service;

import com.afrikatek.churchservice.domain.BaptismHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link BaptismHistory}.
 */
public interface BaptismHistoryService {
    /**
     * Save a baptismHistory.
     *
     * @param baptismHistory the entity to save.
     * @return the persisted entity.
     */
    BaptismHistory save(BaptismHistory baptismHistory);

    /**
     * Updates a baptismHistory.
     *
     * @param baptismHistory the entity to update.
     * @return the persisted entity.
     */
    BaptismHistory update(BaptismHistory baptismHistory);

    /**
     * Partially updates a baptismHistory.
     *
     * @param baptismHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BaptismHistory> partialUpdate(BaptismHistory baptismHistory);

    /**
     * Get all the baptismHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaptismHistory> findAll(Pageable pageable);
    /**
     * Get all the BaptismHistory where Profile is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<BaptismHistory> findAllWhereProfileIsNull();

    /**
     * Get the "id" baptismHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaptismHistory> findOne(Long id);

    /**
     * Delete the "id" baptismHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
