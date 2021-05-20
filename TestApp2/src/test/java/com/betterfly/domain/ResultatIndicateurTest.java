package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultatIndicateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultatIndicateur.class);
        ResultatIndicateur resultatIndicateur1 = new ResultatIndicateur();
        resultatIndicateur1.setId(1L);
        ResultatIndicateur resultatIndicateur2 = new ResultatIndicateur();
        resultatIndicateur2.setId(resultatIndicateur1.getId());
        assertThat(resultatIndicateur1).isEqualTo(resultatIndicateur2);
        resultatIndicateur2.setId(2L);
        assertThat(resultatIndicateur1).isNotEqualTo(resultatIndicateur2);
        resultatIndicateur1.setId(null);
        assertThat(resultatIndicateur1).isNotEqualTo(resultatIndicateur2);
    }
}
