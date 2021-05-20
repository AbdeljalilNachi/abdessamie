package com.betterfly.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ProcessusSMISearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProcessusSMISearchRepositoryMockConfiguration {

    @MockBean
    private ProcessusSMISearchRepository mockProcessusSMISearchRepository;
}
