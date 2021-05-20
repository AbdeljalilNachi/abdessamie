package com.betterfly.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ObligationConformiteSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ObligationConformiteSearchRepositoryMockConfiguration {

    @MockBean
    private ObligationConformiteSearchRepository mockObligationConformiteSearchRepository;
}
