package com.medicita.app.service.mapper;

import static com.medicita.app.domain.PagoAsserts.*;
import static com.medicita.app.domain.PagoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PagoMapperTest {

    private PagoMapper pagoMapper;

    @BeforeEach
    void setUp() {
        pagoMapper = new PagoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPagoSample1();
        var actual = pagoMapper.toEntity(pagoMapper.toDto(expected));
        assertPagoAllPropertiesEquals(expected, actual);
    }
}
