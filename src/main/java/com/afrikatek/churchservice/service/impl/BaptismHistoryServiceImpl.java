package com.afrikatek.churchservice.service.impl;

import com.afrikatek.churchservice.domain.BaptismHistory;
import com.afrikatek.churchservice.repository.BaptismHistoryRepository;
import com.afrikatek.churchservice.service.BaptismHistoryService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BaptismHistory}.
 */
@Service
@Transactional
public class BaptismHistoryServiceImpl implements BaptismHistoryService {

    private final Logger log = LoggerFactory.getLogger(BaptismHistoryServiceImpl.class);

    private final BaptismHistoryRepository baptismHistoryRepository;

    public BaptismHistoryServiceImpl(BaptismHistoryRepository baptismHistoryRepository) {
        this.baptismHistoryRepository = baptismHistoryRepository;
    }

    @Override
    public BaptismHistory save(BaptismHistory baptismHistory) {
        log.debug("Request to save BaptismHistory : {}", baptismHistory);
        return baptismHistoryRepository.save(baptismHistory);
    }

    @Override
    public BaptismHistory update(BaptismHistory baptismHistory) {
        log.debug("Request to save BaptismHistory : {}", baptismHistory);
        return baptismHistoryRepository.save(baptismHistory);
    }

    @Override
    public Optional<BaptismHistory> partialUpdate(BaptismHistory baptismHistory) {
        log.debug("Request to partially update BaptismHistory : {}", baptismHistory);

        return baptismHistoryRepository
            .findById(baptismHistory.getId())
            .map(existingBaptismHistory -> {
                if (baptismHistory.getLutheran() != null) {
                    existingBaptismHistory.setLutheran(baptismHistory.getLutheran());
                }
                if (baptismHistory.getPreviousParish() != null) {
                    existingBaptismHistory.setPreviousParish(baptismHistory.getPreviousParish());
                }
                if (baptismHistory.getBaptised() != null) {
                    existingBaptismHistory.setBaptised(baptismHistory.getBaptised());
                }
                if (baptismHistory.getBaptismDate() != null) {
                    existingBaptismHistory.setBaptismDate(baptismHistory.getBaptismDate());
                }
                if (baptismHistory.getBaptisedAt() != null) {
                    existingBaptismHistory.setBaptisedAt(baptismHistory.getBaptisedAt());
                }
                if (baptismHistory.getConfirmed() != null) {
                    existingBaptismHistory.setConfirmed(baptismHistory.getConfirmed());
                }
                if (baptismHistory.getConfirmationDate() != null) {
                    existingBaptismHistory.setConfirmationDate(baptismHistory.getConfirmationDate());
                }
                if (baptismHistory.getParishConfirmed() != null) {
                    existingBaptismHistory.setParishConfirmed(baptismHistory.getParishConfirmed());
                }
                if (baptismHistory.getMarried() != null) {
                    existingBaptismHistory.setMarried(baptismHistory.getMarried());
                }
                if (baptismHistory.getMarriageDate() != null) {
                    existingBaptismHistory.setMarriageDate(baptismHistory.getMarriageDate());
                }
                if (baptismHistory.getParishMarriedAt() != null) {
                    existingBaptismHistory.setParishMarriedAt(baptismHistory.getParishMarriedAt());
                }

                return existingBaptismHistory;
            })
            .map(baptismHistoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BaptismHistory> findAll(Pageable pageable) {
        log.debug("Request to get all BaptismHistories");
        return baptismHistoryRepository.findAll(pageable);
    }

    /**
     *  Get all the baptismHistories where Profile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BaptismHistory> findAllWhereProfileIsNull() {
        log.debug("Request to get all baptismHistories where Profile is null");
        return StreamSupport
            .stream(baptismHistoryRepository.findAll().spliterator(), false)
            .filter(baptismHistory -> baptismHistory.getProfile() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BaptismHistory> findOne(Long id) {
        log.debug("Request to get BaptismHistory : {}", id);
        return baptismHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BaptismHistory : {}", id);
        baptismHistoryRepository.deleteById(id);
    }
}
