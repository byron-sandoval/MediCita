package com.medicita.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medico getMedicoSample1() {
        return new Medico()
            .id(1L)
            .nombre("nombre1")
            .apellido("apellido1")
            .email("email1")
            .numeroLicencia("numeroLicencia1")
            .keycloakId("keycloakId1");
    }

    public static Medico getMedicoSample2() {
        return new Medico()
            .id(2L)
            .nombre("nombre2")
            .apellido("apellido2")
            .email("email2")
            .numeroLicencia("numeroLicencia2")
            .keycloakId("keycloakId2");
    }

    public static Medico getMedicoRandomSampleGenerator() {
        return new Medico()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .numeroLicencia(UUID.randomUUID().toString())
            .keycloakId(UUID.randomUUID().toString());
    }
}
