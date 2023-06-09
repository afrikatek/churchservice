package com.afrikatek.churchservice.repository;

import com.afrikatek.churchservice.domain.Profile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Profile entity.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    default Optional<Profile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Profile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Profile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct profile from Profile profile left join fetch profile.user left join fetch profile.league left join fetch profile.ministry",
        countQuery = "select count(distinct profile) from Profile profile"
    )
    Page<Profile> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct profile from Profile profile left join fetch profile.user left join fetch profile.league left join fetch profile.ministry"
    )
    List<Profile> findAllWithToOneRelationships();

    @Query(
        "select profile from Profile profile left join fetch profile.user left join fetch profile.league left join fetch profile.ministry where profile.id =:id"
    )
    Optional<Profile> findOneWithToOneRelationships(@Param("id") Long id);
}
