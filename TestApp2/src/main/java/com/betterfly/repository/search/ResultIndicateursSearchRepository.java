package com.betterfly.repository.search;

import com.betterfly.domain.ResultIndicateurs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ResultIndicateurs} entity.
 */
public interface ResultIndicateursSearchRepository extends ElasticsearchRepository<ResultIndicateurs, Long> {}
