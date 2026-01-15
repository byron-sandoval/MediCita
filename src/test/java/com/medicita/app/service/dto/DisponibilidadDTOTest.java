package com.medicita.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisponibilidadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisponibilidadDTO.class);
        DisponibilidadDTO disponibilidadDTO1 = new DisponibilidadDTO();
        disponibilidadDTO1.setId(1L);
        DisponibilidadDTO disponibilidadDTO2 = new DisponibilidadDTO();
        assertThat(disponibilidadDTO1).isNotEqualTo(disponibilidadDTO2);
        disponibilidadDTO2.setId(disponibilidadDTO1.getId());
        assertThat(disponibilidadDTO1).isEqualTo(disponibilidadDTO2);
        disponibilidadDTO2.setId(2L);
        assertThat(disponibilidadDTO1).isNotEqualTo(disponibilidadDTO2);
        disponibilidadDTO1.setId(null);
        assertThat(disponibilidadDTO1).isNotEqualTo(disponibilidadDTO2);
    }
}
