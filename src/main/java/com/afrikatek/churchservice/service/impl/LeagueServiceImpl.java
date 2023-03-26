package com.afrikatek.churchservice.service.impl;

import com.afrikatek.churchservice.domain.League;
import com.afrikatek.churchservice.repository.LeagueRepository;
import com.afrikatek.churchservice.service.LeagueService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link League}.
 */
@Service
@Transactional
public class LeagueServiceImpl implements LeagueService {

    private final Logger log = LoggerFactory.getLogger(LeagueServiceImpl.class);

    private final LeagueRepository leagueRepository;

    public LeagueServiceImpl(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    @Override
    public League save(League league) {
        log.debug("Request to save League : {}", league);
        return leagueRepository.save(league);
    }

    @Override
    public League update(League league) {
        log.debug("Request to save League : {}", league);
        return leagueRepository.save(league);
    }

    @Override
    public Optional<League> partialUpdate(League league) {
        log.debug("Request to partially update League : {}", league);

        return leagueRepository
            .findById(league.getId())
            .map(existingLeague -> {
                if (league.getName() != null) {
                    existingLeague.setName(league.getName());
                }
                if (league.getDescription() != null) {
                    existingLeague.setDescription(league.getDescription());
                }

                return existingLeague;
            })
            .map(leagueRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<League> findAll(Pageable pageable) {
        log.debug("Request to get all Leagues");
        return leagueRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<League> findOne(Long id) {
        log.debug("Request to get League : {}", id);
        return leagueRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete League : {}", id);
        leagueRepository.deleteById(id);
    }
}
