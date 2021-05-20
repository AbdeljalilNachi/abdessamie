package com.betterfly.repository;

import com.betterfly.domain.ResultIndicateurs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ResultIndicateurs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultIndicateursRepository extends JpaRepository<ResultIndicateurs, Long> {}
