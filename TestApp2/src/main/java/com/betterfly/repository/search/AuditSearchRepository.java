package com.betterfly.repository.search;

import com.betterfly.domain.Audit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Audit} entity.
 */
public interface AuditSearchRepository extends ElasticsearchRepository<Audit, Long> {}
