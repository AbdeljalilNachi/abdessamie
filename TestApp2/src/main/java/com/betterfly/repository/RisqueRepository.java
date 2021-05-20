package com.betterfly.repository;

import com.betterfly.domain.Risque;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Risque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RisqueRepository extends JpaRepository<Risque, Long> {
    @Query("select risque from Risque risque where risque.delegue.login = ?#{principal.username}")
    List<Risque> findByDelegueIsCurrentUser();
}
