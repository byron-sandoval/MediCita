package com.medicita.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContenidoWebTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContenidoWeb getContenidoWebSample1() {
        return new ContenidoWeb().id(1L).clave("clave1").valorTexto("valorTexto1");
    }

    public static ContenidoWeb getContenidoWebSample2() {
        return new ContenidoWeb().id(2L).clave("clave2").valorTexto("valorTexto2");
    }

    public static ContenidoWeb getContenidoWebRandomSampleGenerator() {
        return new ContenidoWeb()
            .id(longCount.incrementAndGet())
            .clave(UUID.randomUUID().toString())
            .valorTexto(UUID.randomUUID().toString());
    }
}
