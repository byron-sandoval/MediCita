package com.medicita.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente()
            .id(1L)
            .nombre("nombre1")
            .apellido("apellido1")
            .cedula("cedula1")
            .email("email1")
            .telefono("telefono1")
            .keycloakId("keycloakId1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente()
            .id(2L)
            .nombre("nombre2")
            .apellido("apellido2")
            .cedula("cedula2")
            .email("email2")
            .telefono("telefono2")
            .keycloakId("keycloakId2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .cedula(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .keycloakId(UUID.randomUUID().toString());
    }
}
