package com.betterfly.repository.search;

import com.betterfly.domain.ResultatIndicateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ResultatIndicateur} entity.
 */
public interface ResultatIndicateurSearchRepository extends ElasticsearchRepository<ResultatIndicateur, Long> {}
