package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AutreActionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutreAction.class);
        AutreAction autreAction1 = new AutreAction();
        autreAction1.setId(1L);
        AutreAction autreAction2 = new AutreAction();
        autreAction2.setId(autreAction1.getId());
        assertThat(autreAction1).isEqualTo(autreAction2);
        autreAction2.setId(2L);
        assertThat(autreAction1).isNotEqualTo(autreAction2);
        autreAction1.setId(null);
        assertThat(autreAction1).isNotEqualTo(autreAction2);
    }
}
