package com.betterfly.repository.search;

import com.betterfly.domain.Objectif;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Objectif} entity.
 */
public interface ObjectifSearchRepository extends ElasticsearchRepository<Objectif, Long> {}
