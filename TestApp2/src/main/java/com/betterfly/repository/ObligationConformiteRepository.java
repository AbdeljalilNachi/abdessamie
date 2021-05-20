package com.betterfly.repository;

import com.betterfly.domain.ObligationConformite;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ObligationConformite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObligationConformiteRepository extends JpaRepository<ObligationConformite, Long> {
    @Query(
        "select obligationConformite from ObligationConformite obligationConformite where obligationConformite.delegue.login = ?#{principal.username}"
    )
    List<ObligationConformite> findByDelegueIsCurrentUser();
}
