package com.betterfly.repository.search;

import com.betterfly.domain.NonConformite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link NonConformite} entity.
 */
public interface NonConformiteSearchRepository extends ElasticsearchRepository<NonConformite, Long> {}
