package com.afrikatek.churchservice.service.impl;

import com.afrikatek.churchservice.domain.Ministry;
import com.afrikatek.churchservice.repository.MinistryRepository;
import com.afrikatek.churchservice.service.MinistryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ministry}.
 */
@Service
@Transactional
public class MinistryServiceImpl implements MinistryService {

    private final Logger log = LoggerFactory.getLogger(MinistryServiceImpl.class);

    private final MinistryRepository ministryRepository;

    public MinistryServiceImpl(MinistryRepository ministryRepository) {
        this.ministryRepository = ministryRepository;
    }

    @Override
    public Ministry save(Ministry ministry) {
        log.debug("Request to save Ministry : {}", ministry);
        return ministryRepository.save(ministry);
    }

    @Override
    public Ministry update(Ministry ministry) {
        log.debug("Request to save Ministry : {}", ministry);
        return ministryRepository.save(ministry);
    }

    @Override
    public Optional<Ministry> partialUpdate(Ministry ministry) {
        log.debug("Request to partially update Ministry : {}", ministry);

        return ministryRepository
            .findById(ministry.getId())
            .map(existingMinistry -> {
                if (ministry.getName() != null) {
                    existingMinistry.setName(ministry.getName());
                }
                if (ministry.getDescription() != null) {
                    existingMinistry.setDescription(ministry.getDescription());
                }

                return existingMinistry;
            })
            .map(ministryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ministry> findAll(Pageable pageable) {
        log.debug("Request to get all Ministries");
        return ministryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ministry> findOne(Long id) {
        log.debug("Request to get Ministry : {}", id);
        return ministryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ministry : {}", id);
        ministryRepository.deleteById(id);
    }
}
