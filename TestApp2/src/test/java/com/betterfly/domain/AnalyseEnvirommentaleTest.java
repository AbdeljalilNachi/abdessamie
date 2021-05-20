package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnalyseEnvirommentaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalyseEnvirommentale.class);
        AnalyseEnvirommentale analyseEnvirommentale1 = new AnalyseEnvirommentale();
        analyseEnvirommentale1.setId(1L);
        AnalyseEnvirommentale analyseEnvirommentale2 = new AnalyseEnvirommentale();
        analyseEnvirommentale2.setId(analyseEnvirommentale1.getId());
        assertThat(analyseEnvirommentale1).isEqualTo(analyseEnvirommentale2);
        analyseEnvirommentale2.setId(2L);
        assertThat(analyseEnvirommentale1).isNotEqualTo(analyseEnvirommentale2);
        analyseEnvirommentale1.setId(null);
        assertThat(analyseEnvirommentale1).isNotEqualTo(analyseEnvirommentale2);
    }
}
