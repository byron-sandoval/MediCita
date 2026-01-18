package com.medicita.app.service.mapper;

import static com.medicita.app.domain.AdjuntoAsserts.*;
import static com.medicita.app.domain.AdjuntoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdjuntoMapperTest {

    private AdjuntoMapper adjuntoMapper;

    @BeforeEach
    void setUp() {
        adjuntoMapper = new AdjuntoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdjuntoSample1();
        var actual = adjuntoMapper.toEntity(adjuntoMapper.toDto(expected));
        assertAdjuntoAllPropertiesEquals(expected, actual);
    }
}
