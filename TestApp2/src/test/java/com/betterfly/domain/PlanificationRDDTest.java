package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanificationRDDTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanificationRDD.class);
        PlanificationRDD planificationRDD1 = new PlanificationRDD();
        planificationRDD1.setId(1L);
        PlanificationRDD planificationRDD2 = new PlanificationRDD();
        planificationRDD2.setId(planificationRDD1.getId());
        assertThat(planificationRDD1).isEqualTo(planificationRDD2);
        planificationRDD2.setId(2L);
        assertThat(planificationRDD1).isNotEqualTo(planificationRDD2);
        planificationRDD1.setId(null);
        assertThat(planificationRDD1).isNotEqualTo(planificationRDD2);
    }
}
