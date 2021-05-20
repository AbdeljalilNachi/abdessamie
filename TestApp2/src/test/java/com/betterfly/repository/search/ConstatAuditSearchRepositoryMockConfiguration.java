package com.betterfly.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ConstatAuditSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ConstatAuditSearchRepositoryMockConfiguration {

    @MockBean
    private ConstatAuditSearchRepository mockConstatAuditSearchRepository;
}
