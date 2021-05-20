package com.betterfly.repository;

import com.betterfly.domain.Objectif;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Objectif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjectifRepository extends JpaRepository<Objectif, Long> {
    @Query("select objectif from Objectif objectif where objectif.delegue.login = ?#{principal.username}")
    List<Objectif> findByDelegueIsCurrentUser();
}
