package com.medicita.app.service.mapper;

import static com.medicita.app.domain.DisponibilidadAsserts.*;
import static com.medicita.app.domain.DisponibilidadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisponibilidadMapperTest {

    private DisponibilidadMapper disponibilidadMapper;

    @BeforeEach
    void setUp() {
        disponibilidadMapper = new DisponibilidadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDisponibilidadSample1();
        var actual = disponibilidadMapper.toEntity(disponibilidadMapper.toDto(expected));
        assertDisponibilidadAllPropertiesEquals(expected, actual);
    }
}
