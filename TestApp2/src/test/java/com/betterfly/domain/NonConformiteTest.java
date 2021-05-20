package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NonConformiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NonConformite.class);
        NonConformite nonConformite1 = new NonConformite();
        nonConformite1.setId(1L);
        NonConformite nonConformite2 = new NonConformite();
        nonConformite2.setId(nonConformite1.getId());
        assertThat(nonConformite1).isEqualTo(nonConformite2);
        nonConformite2.setId(2L);
        assertThat(nonConformite1).isNotEqualTo(nonConformite2);
        nonConformite1.setId(null);
        assertThat(nonConformite1).isNotEqualTo(nonConformite2);
    }
}
