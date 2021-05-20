package com.betterfly.repository;

import com.betterfly.domain.BesoinPI;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BesoinPI entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BesoinPIRepository extends JpaRepository<BesoinPI, Long> {}
