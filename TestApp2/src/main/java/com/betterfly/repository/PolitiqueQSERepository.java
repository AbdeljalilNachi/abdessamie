package com.betterfly.repository;

import com.betterfly.domain.PolitiqueQSE;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PolitiqueQSE entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PolitiqueQSERepository extends JpaRepository<PolitiqueQSE, Long> {}
