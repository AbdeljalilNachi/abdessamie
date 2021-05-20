package com.betterfly.repository.search;

import com.betterfly.domain.AutreAction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AutreAction} entity.
 */
public interface AutreActionSearchRepository extends ElasticsearchRepository<AutreAction, Long> {}
