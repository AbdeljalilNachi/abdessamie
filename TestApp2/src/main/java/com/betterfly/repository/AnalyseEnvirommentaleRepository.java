package com.betterfly.repository;

import com.betterfly.domain.AnalyseEnvirommentale;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AnalyseEnvirommentale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyseEnvirommentaleRepository extends JpaRepository<AnalyseEnvirommentale, Long> {
    @Query(
        "select analyseEnvirommentale from AnalyseEnvirommentale analyseEnvirommentale where analyseEnvirommentale.delegue.login = ?#{principal.username}"
    )
    List<AnalyseEnvirommentale> findByDelegueIsCurrentUser();
}
