package com.betterfly.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link NonConformiteSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class NonConformiteSearchRepositoryMockConfiguration {

    @MockBean
    private NonConformiteSearchRepository mockNonConformiteSearchRepository;
}
