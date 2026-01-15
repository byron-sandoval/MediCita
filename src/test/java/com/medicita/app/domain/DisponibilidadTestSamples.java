package com.medicita.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class DisponibilidadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Disponibilidad getDisponibilidadSample1() {
        return new Disponibilidad().id(1L);
    }

    public static Disponibilidad getDisponibilidadSample2() {
        return new Disponibilidad().id(2L);
    }

    public static Disponibilidad getDisponibilidadRandomSampleGenerator() {
        return new Disponibilidad().id(longCount.incrementAndGet());
    }
}
