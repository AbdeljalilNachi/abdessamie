package com.betterfly.repository.search;

import com.betterfly.domain.PolitiqueQSE;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PolitiqueQSE} entity.
 */
public interface PolitiqueQSESearchRepository extends ElasticsearchRepository<PolitiqueQSE, Long> {}
