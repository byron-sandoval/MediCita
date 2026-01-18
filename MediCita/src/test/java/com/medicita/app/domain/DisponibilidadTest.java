package com.medicita.app.domain;

import static com.medicita.app.domain.DisponibilidadTestSamples.*;
import static com.medicita.app.domain.MedicoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisponibilidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disponibilidad.class);
        Disponibilidad disponibilidad1 = getDisponibilidadSample1();
        Disponibilidad disponibilidad2 = new Disponibilidad();
        assertThat(disponibilidad1).isNotEqualTo(disponibilidad2);

        disponibilidad2.setId(disponibilidad1.getId());
        assertThat(disponibilidad1).isEqualTo(disponibilidad2);

        disponibilidad2 = getDisponibilidadSample2();
        assertThat(disponibilidad1).isNotEqualTo(disponibilidad2);
    }

    @Test
    void medicoTest() {
        Disponibilidad disponibilidad = getDisponibilidadRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        disponibilidad.setMedico(medicoBack);
        assertThat(disponibilidad.getMedico()).isEqualTo(medicoBack);

        disponibilidad.medico(null);
        assertThat(disponibilidad.getMedico()).isNull();
    }
}
