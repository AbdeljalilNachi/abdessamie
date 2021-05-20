package com.betterfly.repository;

import com.betterfly.domain.Reclamation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reclamation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    @Query("select reclamation from Reclamation reclamation where reclamation.delegue.login = ?#{principal.username}")
    List<Reclamation> findByDelegueIsCurrentUser();
}
