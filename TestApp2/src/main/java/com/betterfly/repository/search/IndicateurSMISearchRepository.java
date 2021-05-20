package com.betterfly.repository.search;

import com.betterfly.domain.IndicateurSMI;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IndicateurSMI} entity.
 */
public interface IndicateurSMISearchRepository extends ElasticsearchRepository<IndicateurSMI, Long> {}
