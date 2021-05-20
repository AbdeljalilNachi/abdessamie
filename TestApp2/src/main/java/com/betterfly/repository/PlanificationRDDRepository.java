package com.betterfly.repository;

import com.betterfly.domain.PlanificationRDD;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlanificationRDD entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanificationRDDRepository extends JpaRepository<PlanificationRDD, Long> {}
