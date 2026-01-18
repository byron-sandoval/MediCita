package com.medicita.app.service.mapper;

import static com.medicita.app.domain.CitaAsserts.*;
import static com.medicita.app.domain.CitaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CitaMapperTest {

    private CitaMapper citaMapper;

    @BeforeEach
    void setUp() {
        citaMapper = new CitaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCitaSample1();
        var actual = citaMapper.toEntity(citaMapper.toDto(expected));
        assertCitaAllPropertiesEquals(expected, actual);
    }
}
