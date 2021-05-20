package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessusSMITest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessusSMI.class);
        ProcessusSMI processusSMI1 = new ProcessusSMI();
        processusSMI1.setId(1L);
        ProcessusSMI processusSMI2 = new ProcessusSMI();
        processusSMI2.setId(processusSMI1.getId());
        assertThat(processusSMI1).isEqualTo(processusSMI2);
        processusSMI2.setId(2L);
        assertThat(processusSMI1).isNotEqualTo(processusSMI2);
        processusSMI1.setId(null);
        assertThat(processusSMI1).isNotEqualTo(processusSMI2);
    }
}
