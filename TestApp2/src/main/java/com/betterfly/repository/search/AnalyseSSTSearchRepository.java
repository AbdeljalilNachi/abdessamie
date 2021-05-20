package com.betterfly.repository.search;

import com.betterfly.domain.AnalyseSST;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AnalyseSST} entity.
 */
public interface AnalyseSSTSearchRepository extends ElasticsearchRepository<AnalyseSST, Long> {}
