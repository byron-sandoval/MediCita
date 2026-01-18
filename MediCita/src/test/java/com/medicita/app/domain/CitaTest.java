package com.medicita.app.domain;

import static com.medicita.app.domain.CitaTestSamples.*;
import static com.medicita.app.domain.MedicoTestSamples.*;
import static com.medicita.app.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cita.class);
        Cita cita1 = getCitaSample1();
        Cita cita2 = new Cita();
        assertThat(cita1).isNotEqualTo(cita2);

        cita2.setId(cita1.getId());
        assertThat(cita1).isEqualTo(cita2);

        cita2 = getCitaSample2();
        assertThat(cita1).isNotEqualTo(cita2);
    }

    @Test
    void medicoTest() {
        Cita cita = getCitaRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        cita.setMedico(medicoBack);
        assertThat(cita.getMedico()).isEqualTo(medicoBack);

        cita.medico(null);
        assertThat(cita.getMedico()).isNull();
    }

    @Test
    void pacienteTest() {
        Cita cita = getCitaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        cita.setPaciente(pacienteBack);
        assertThat(cita.getPaciente()).isEqualTo(pacienteBack);

        cita.paciente(null);
        assertThat(cita.getPaciente()).isNull();
    }
}
