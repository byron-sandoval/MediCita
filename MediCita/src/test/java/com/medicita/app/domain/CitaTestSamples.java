package com.medicita.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CitaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cita getCitaSample1() {
        return new Cita().id(1L).motivo("motivo1").enlaceTelemedicina("enlaceTelemedicina1");
    }

    public static Cita getCitaSample2() {
        return new Cita().id(2L).motivo("motivo2").enlaceTelemedicina("enlaceTelemedicina2");
    }

    public static Cita getCitaRandomSampleGenerator() {
        return new Cita()
            .id(longCount.incrementAndGet())
            .motivo(UUID.randomUUID().toString())
            .enlaceTelemedicina(UUID.randomUUID().toString());
    }
}
