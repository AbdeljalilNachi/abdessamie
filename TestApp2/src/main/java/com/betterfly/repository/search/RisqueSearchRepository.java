package com.betterfly.repository.search;

import com.betterfly.domain.Risque;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Risque} entity.
 */
public interface RisqueSearchRepository extends ElasticsearchRepository<Risque, Long> {}
