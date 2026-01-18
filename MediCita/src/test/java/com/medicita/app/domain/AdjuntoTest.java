package com.medicita.app.domain;

import static com.medicita.app.domain.AdjuntoTestSamples.*;
import static com.medicita.app.domain.CitaTestSamples.*;
import static com.medicita.app.domain.HistoriaClinicaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdjuntoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adjunto.class);
        Adjunto adjunto1 = getAdjuntoSample1();
        Adjunto adjunto2 = new Adjunto();
        assertThat(adjunto1).isNotEqualTo(adjunto2);

        adjunto2.setId(adjunto1.getId());
        assertThat(adjunto1).isEqualTo(adjunto2);

        adjunto2 = getAdjuntoSample2();
        assertThat(adjunto1).isNotEqualTo(adjunto2);
    }

    @Test
    void citaTest() {
        Adjunto adjunto = getAdjuntoRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        adjunto.setCita(citaBack);
        assertThat(adjunto.getCita()).isEqualTo(citaBack);

        adjunto.cita(null);
        assertThat(adjunto.getCita()).isNull();
    }

    @Test
    void historiaClinicaTest() {
        Adjunto adjunto = getAdjuntoRandomSampleGenerator();
        HistoriaClinica historiaClinicaBack = getHistoriaClinicaRandomSampleGenerator();

        adjunto.setHistoriaClinica(historiaClinicaBack);
        assertThat(adjunto.getHistoriaClinica()).isEqualTo(historiaClinicaBack);

        adjunto.historiaClinica(null);
        assertThat(adjunto.getHistoriaClinica()).isNull();
    }
}
