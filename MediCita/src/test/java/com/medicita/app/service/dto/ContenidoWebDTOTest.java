package com.medicita.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContenidoWebDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContenidoWebDTO.class);
        ContenidoWebDTO contenidoWebDTO1 = new ContenidoWebDTO();
        contenidoWebDTO1.setId(1L);
        ContenidoWebDTO contenidoWebDTO2 = new ContenidoWebDTO();
        assertThat(contenidoWebDTO1).isNotEqualTo(contenidoWebDTO2);
        contenidoWebDTO2.setId(contenidoWebDTO1.getId());
        assertThat(contenidoWebDTO1).isEqualTo(contenidoWebDTO2);
        contenidoWebDTO2.setId(2L);
        assertThat(contenidoWebDTO1).isNotEqualTo(contenidoWebDTO2);
        contenidoWebDTO1.setId(null);
        assertThat(contenidoWebDTO1).isNotEqualTo(contenidoWebDTO2);
    }
}
