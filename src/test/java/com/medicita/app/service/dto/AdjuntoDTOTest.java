package com.medicita.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdjuntoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdjuntoDTO.class);
        AdjuntoDTO adjuntoDTO1 = new AdjuntoDTO();
        adjuntoDTO1.setId(1L);
        AdjuntoDTO adjuntoDTO2 = new AdjuntoDTO();
        assertThat(adjuntoDTO1).isNotEqualTo(adjuntoDTO2);
        adjuntoDTO2.setId(adjuntoDTO1.getId());
        assertThat(adjuntoDTO1).isEqualTo(adjuntoDTO2);
        adjuntoDTO2.setId(2L);
        assertThat(adjuntoDTO1).isNotEqualTo(adjuntoDTO2);
        adjuntoDTO1.setId(null);
        assertThat(adjuntoDTO1).isNotEqualTo(adjuntoDTO2);
    }
}
