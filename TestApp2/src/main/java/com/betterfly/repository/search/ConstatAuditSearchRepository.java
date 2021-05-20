package com.betterfly.repository.search;

import com.betterfly.domain.ConstatAudit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ConstatAudit} entity.
 */
public interface ConstatAuditSearchRepository extends ElasticsearchRepository<ConstatAudit, Long> {}
