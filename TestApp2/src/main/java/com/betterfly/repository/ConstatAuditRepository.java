package com.betterfly.repository;

import com.betterfly.domain.ConstatAudit;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ConstatAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConstatAuditRepository extends JpaRepository<ConstatAudit, Long> {
    @Query("select constatAudit from ConstatAudit constatAudit where constatAudit.delegue.login = ?#{principal.username}")
    List<ConstatAudit> findByDelegueIsCurrentUser();
}
