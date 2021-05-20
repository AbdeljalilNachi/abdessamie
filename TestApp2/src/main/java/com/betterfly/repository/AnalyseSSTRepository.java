package com.betterfly.repository;

import com.betterfly.domain.AnalyseSST;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AnalyseSST entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyseSSTRepository extends JpaRepository<AnalyseSST, Long> {
    @Query("select analyseSST from AnalyseSST analyseSST where analyseSST.delegue.login = ?#{principal.username}")
    List<AnalyseSST> findByDelegueIsCurrentUser();
}
