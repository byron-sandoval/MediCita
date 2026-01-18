package com.medicita.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdjuntoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Adjunto getAdjuntoSample1() {
        return new Adjunto().id(1L).nombre("nombre1").tipoContenido("tipoContenido1");
    }

    public static Adjunto getAdjuntoSample2() {
        return new Adjunto().id(2L).nombre("nombre2").tipoContenido("tipoContenido2");
    }

    public static Adjunto getAdjuntoRandomSampleGenerator() {
        return new Adjunto()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .tipoContenido(UUID.randomUUID().toString());
    }
}
