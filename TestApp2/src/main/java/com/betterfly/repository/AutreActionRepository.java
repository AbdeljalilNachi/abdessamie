package com.betterfly.repository;

import com.betterfly.domain.AutreAction;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AutreAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutreActionRepository extends JpaRepository<AutreAction, Long> {
    @Query("select autreAction from AutreAction autreAction where autreAction.delegue.login = ?#{principal.username}")
    List<AutreAction> findByDelegueIsCurrentUser();
}
