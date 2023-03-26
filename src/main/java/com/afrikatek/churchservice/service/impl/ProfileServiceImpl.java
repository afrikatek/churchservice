package com.afrikatek.churchservice.service.impl;

import com.afrikatek.churchservice.domain.Profile;
import com.afrikatek.churchservice.repository.ProfileRepository;
import com.afrikatek.churchservice.service.ProfileService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Profile}.
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile save(Profile profile) {
        log.debug("Request to save Profile : {}", profile);
        return profileRepository.save(profile);
    }

    @Override
    public Profile update(Profile profile) {
        log.debug("Request to save Profile : {}", profile);
        return profileRepository.save(profile);
    }

    @Override
    public Optional<Profile> partialUpdate(Profile profile) {
        log.debug("Request to partially update Profile : {}", profile);

        return profileRepository
            .findById(profile.getId())
            .map(existingProfile -> {
                if (profile.getTitle() != null) {
                    existingProfile.setTitle(profile.getTitle());
                }
                if (profile.getFirstName() != null) {
                    existingProfile.setFirstName(profile.getFirstName());
                }
                if (profile.getSecondNames() != null) {
                    existingProfile.setSecondNames(profile.getSecondNames());
                }
                if (profile.getLastName() != null) {
                    existingProfile.setLastName(profile.getLastName());
                }
                if (profile.getIdNumber() != null) {
                    existingProfile.setIdNumber(profile.getIdNumber());
                }
                if (profile.getGender() != null) {
                    existingProfile.setGender(profile.getGender());
                }
                if (profile.getDateOfBirth() != null) {
                    existingProfile.setDateOfBirth(profile.getDateOfBirth());
                }
                if (profile.getProfileImage() != null) {
                    existingProfile.setProfileImage(profile.getProfileImage());
                }
                if (profile.getProfileImageContentType() != null) {
                    existingProfile.setProfileImageContentType(profile.getProfileImageContentType());
                }
                if (profile.getProfession() != null) {
                    existingProfile.setProfession(profile.getProfession());
                }

                return existingProfile;
            })
            .map(profileRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Profile> findAll(Pageable pageable) {
        log.debug("Request to get all Profiles");
        return profileRepository.findAll(pageable);
    }

    public Page<Profile> findAllWithEagerRelationships(Pageable pageable) {
        return profileRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Profile> findOne(Long id) {
        log.debug("Request to get Profile : {}", id);
        return profileRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Profile : {}", id);
        profileRepository.deleteById(id);
    }
}
