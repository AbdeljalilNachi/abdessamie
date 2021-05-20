package com.betterfly.repository;

import com.betterfly.domain.ResultatIndicateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ResultatIndicateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultatIndicateurRepository extends JpaRepository<ResultatIndicateur, Long> {}
