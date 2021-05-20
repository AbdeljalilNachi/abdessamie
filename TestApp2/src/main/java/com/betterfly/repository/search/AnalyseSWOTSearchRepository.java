package com.betterfly.repository.search;

import com.betterfly.domain.AnalyseSWOT;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AnalyseSWOT} entity.
 */
public interface AnalyseSWOTSearchRepository extends ElasticsearchRepository<AnalyseSWOT, Long> {}
