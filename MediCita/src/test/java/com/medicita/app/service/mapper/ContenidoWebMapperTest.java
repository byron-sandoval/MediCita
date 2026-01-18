package com.medicita.app.service.mapper;

import static com.medicita.app.domain.ContenidoWebAsserts.*;
import static com.medicita.app.domain.ContenidoWebTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContenidoWebMapperTest {

    private ContenidoWebMapper contenidoWebMapper;

    @BeforeEach
    void setUp() {
        contenidoWebMapper = new ContenidoWebMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContenidoWebSample1();
        var actual = contenidoWebMapper.toEntity(contenidoWebMapper.toDto(expected));
        assertContenidoWebAllPropertiesEquals(expected, actual);
    }
}
