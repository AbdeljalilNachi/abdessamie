package com.betterfly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.betterfly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultIndicateursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultIndicateurs.class);
        ResultIndicateurs resultIndicateurs1 = new ResultIndicateurs();
        resultIndicateurs1.setId(1L);
        ResultIndicateurs resultIndicateurs2 = new ResultIndicateurs();
        resultIndicateurs2.setId(resultIndicateurs1.getId());
        assertThat(resultIndicateurs1).isEqualTo(resultIndicateurs2);
        resultIndicateurs2.setId(2L);
        assertThat(resultIndicateurs1).isNotEqualTo(resultIndicateurs2);
        resultIndicateurs1.setId(null);
        assertThat(resultIndicateurs1).isNotEqualTo(resultIndicateurs2);
    }
}
