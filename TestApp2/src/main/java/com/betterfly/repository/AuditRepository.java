package com.betterfly.repository;

import com.betterfly.domain.Audit;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Audit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
    @Query("select audit from Audit audit where audit.auditeur.login = ?#{principal.username}")
    List<Audit> findByAuditeurIsCurrentUser();
}
