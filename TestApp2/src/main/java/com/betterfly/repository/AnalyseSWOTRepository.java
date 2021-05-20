package com.betterfly.repository;

import com.betterfly.domain.AnalyseSWOT;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AnalyseSWOT entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyseSWOTRepository extends JpaRepository<AnalyseSWOT, Long> {}
