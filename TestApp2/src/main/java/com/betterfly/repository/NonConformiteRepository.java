package com.betterfly.repository;

import com.betterfly.domain.NonConformite;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NonConformite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NonConformiteRepository extends JpaRepository<NonConformite, Long> {
    @Query("select nonConformite from NonConformite nonConformite where nonConformite.delegue.login = ?#{principal.username}")
    List<NonConformite> findByDelegueIsCurrentUser();
}
