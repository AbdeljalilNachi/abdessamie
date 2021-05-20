package com.betterfly.repository.search;

import com.betterfly.domain.BesoinPI;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link BesoinPI} entity.
 */
public interface BesoinPISearchRepository extends ElasticsearchRepository<BesoinPI, Long> {}
