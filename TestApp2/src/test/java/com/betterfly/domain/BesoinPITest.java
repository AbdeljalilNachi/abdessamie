package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BesoinPITest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BesoinPI.class);
        BesoinPI besoinPI1 = new BesoinPI();
        besoinPI1.setId(1L);
        BesoinPI besoinPI2 = new BesoinPI();
        besoinPI2.setId(besoinPI1.getId());
        assertThat(besoinPI1).isEqualTo(besoinPI2);
        besoinPI2.setId(2L);
        assertThat(besoinPI1).isNotEqualTo(besoinPI2);
        besoinPI1.setId(null);
        assertThat(besoinPI1).isNotEqualTo(besoinPI2);
    }
}
