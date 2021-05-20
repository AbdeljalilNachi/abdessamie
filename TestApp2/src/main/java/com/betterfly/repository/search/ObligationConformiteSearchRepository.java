package com.betterfly.repository.search;

import com.betterfly.domain.ObligationConformite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ObligationConformite} entity.
 */
public interface ObligationConformiteSearchRepository extends ElasticsearchRepository<ObligationConformite, Long> {}
