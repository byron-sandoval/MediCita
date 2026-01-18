package com.medicita.app.domain;

import static com.medicita.app.domain.CitaTestSamples.*;
import static com.medicita.app.domain.PagoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PagoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pago.class);
        Pago pago1 = getPagoSample1();
        Pago pago2 = new Pago();
        assertThat(pago1).isNotEqualTo(pago2);

        pago2.setId(pago1.getId());
        assertThat(pago1).isEqualTo(pago2);

        pago2 = getPagoSample2();
        assertThat(pago1).isNotEqualTo(pago2);
    }

    @Test
    void citaTest() {
        Pago pago = getPagoRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        pago.setCita(citaBack);
        assertThat(pago.getCita()).isEqualTo(citaBack);

        pago.cita(null);
        assertThat(pago.getCita()).isNull();
    }
}
