package com.betterfly.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AuditSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AuditSearchRepositoryMockConfiguration {

    @MockBean
    private AuditSearchRepository mockAuditSearchRepository;
}
