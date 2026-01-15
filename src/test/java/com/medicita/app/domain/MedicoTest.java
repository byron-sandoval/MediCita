package com.medicita.app.domain;

import static com.medicita.app.domain.AdjuntoTestSamples.*;
import static com.medicita.app.domain.MedicoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medico.class);
        Medico medico1 = getMedicoSample1();
        Medico medico2 = new Medico();
        assertThat(medico1).isNotEqualTo(medico2);

        medico2.setId(medico1.getId());
        assertThat(medico1).isEqualTo(medico2);

        medico2 = getMedicoSample2();
        assertThat(medico1).isNotEqualTo(medico2);
    }

    @Test
    void fotoTest() {
        Medico medico = getMedicoRandomSampleGenerator();
        Adjunto adjuntoBack = getAdjuntoRandomSampleGenerator();

        medico.setFoto(adjuntoBack);
        assertThat(medico.getFoto()).isEqualTo(adjuntoBack);

        medico.foto(null);
        assertThat(medico.getFoto()).isNull();
    }
}
