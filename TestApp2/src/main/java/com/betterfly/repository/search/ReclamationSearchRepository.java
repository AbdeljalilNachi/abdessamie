package com.betterfly.repository.search;

import com.betterfly.domain.Reclamation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Reclamation} entity.
 */
public interface ReclamationSearchRepository extends ElasticsearchRepository<Reclamation, Long> {}
