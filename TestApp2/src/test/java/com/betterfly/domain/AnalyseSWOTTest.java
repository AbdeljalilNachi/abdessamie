package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnalyseSWOTTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalyseSWOT.class);
        AnalyseSWOT analyseSWOT1 = new AnalyseSWOT();
        analyseSWOT1.setId(1L);
        AnalyseSWOT analyseSWOT2 = new AnalyseSWOT();
        analyseSWOT2.setId(analyseSWOT1.getId());
        assertThat(analyseSWOT1).isEqualTo(analyseSWOT2);
        analyseSWOT2.setId(2L);
        assertThat(analyseSWOT1).isNotEqualTo(analyseSWOT2);
        analyseSWOT1.setId(null);
        assertThat(analyseSWOT1).isNotEqualTo(analyseSWOT2);
    }
}
