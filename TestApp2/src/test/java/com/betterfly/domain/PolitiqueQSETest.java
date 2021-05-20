package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PolitiqueQSETest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolitiqueQSE.class);
        PolitiqueQSE politiqueQSE1 = new PolitiqueQSE();
        politiqueQSE1.setId(1L);
        PolitiqueQSE politiqueQSE2 = new PolitiqueQSE();
        politiqueQSE2.setId(politiqueQSE1.getId());
        assertThat(politiqueQSE1).isEqualTo(politiqueQSE2);
        politiqueQSE2.setId(2L);
        assertThat(politiqueQSE1).isNotEqualTo(politiqueQSE2);
        politiqueQSE1.setId(null);
        assertThat(politiqueQSE1).isNotEqualTo(politiqueQSE2);
    }
}
