package com.betterfly.repository.search;

import com.betterfly.domain.AnalyseEnvirommentale;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AnalyseEnvirommentale} entity.
 */
public interface AnalyseEnvirommentaleSearchRepository extends ElasticsearchRepository<AnalyseEnvirommentale, Long> {}
