package com.betterfly.repository.search;

import com.betterfly.domain.ProcessusSMI;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProcessusSMI} entity.
 */
public interface ProcessusSMISearchRepository extends ElasticsearchRepository<ProcessusSMI, Long> {}
