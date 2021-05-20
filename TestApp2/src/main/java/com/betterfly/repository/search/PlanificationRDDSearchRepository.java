package com.betterfly.repository.search;

import com.betterfly.domain.PlanificationRDD;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PlanificationRDD} entity.
 */
public interface PlanificationRDDSearchRepository extends ElasticsearchRepository<PlanificationRDD, Long> {}
