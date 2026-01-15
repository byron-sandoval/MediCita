package com.medicita.app.domain;

import static com.medicita.app.domain.AdjuntoTestSamples.*;
import static com.medicita.app.domain.HistoriaClinicaTestSamples.*;
import static com.medicita.app.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = getPacienteSample1();
        Paciente paciente2 = new Paciente();
        assertThat(paciente1).isNotEqualTo(paciente2);

        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);

        paciente2 = getPacienteSample2();
        assertThat(paciente1).isNotEqualTo(paciente2);
    }

    @Test
    void fotoTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Adjunto adjuntoBack = getAdjuntoRandomSampleGenerator();

        paciente.setFoto(adjuntoBack);
        assertThat(paciente.getFoto()).isEqualTo(adjuntoBack);

        paciente.foto(null);
        assertThat(paciente.getFoto()).isNull();
    }

    @Test
    void historiaClinicaTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        HistoriaClinica historiaClinicaBack = getHistoriaClinicaRandomSampleGenerator();

        paciente.setHistoriaClinica(historiaClinicaBack);
        assertThat(paciente.getHistoriaClinica()).isEqualTo(historiaClinicaBack);
        assertThat(historiaClinicaBack.getPaciente()).isEqualTo(paciente);

        paciente.historiaClinica(null);
        assertThat(paciente.getHistoriaClinica()).isNull();
        assertThat(historiaClinicaBack.getPaciente()).isNull();
    }
}
