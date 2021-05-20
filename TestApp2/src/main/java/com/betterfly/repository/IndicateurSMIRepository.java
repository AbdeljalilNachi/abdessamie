package com.betterfly.repository;

import com.betterfly.domain.IndicateurSMI;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IndicateurSMI entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndicateurSMIRepository extends JpaRepository<IndicateurSMI, Long> {
    @Query("select indicateurSMI from IndicateurSMI indicateurSMI where indicateurSMI.responsableDeCalcul.login = ?#{principal.username}")
    List<IndicateurSMI> findByResponsableDeCalculIsCurrentUser();
}
