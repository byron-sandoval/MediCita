package com.medicita.app.domain;

import static com.medicita.app.domain.ContenidoWebTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medicita.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContenidoWebTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContenidoWeb.class);
        ContenidoWeb contenidoWeb1 = getContenidoWebSample1();
        ContenidoWeb contenidoWeb2 = new ContenidoWeb();
        assertThat(contenidoWeb1).isNotEqualTo(contenidoWeb2);

        contenidoWeb2.setId(contenidoWeb1.getId());
        assertThat(contenidoWeb1).isEqualTo(contenidoWeb2);

        contenidoWeb2 = getContenidoWebSample2();
        assertThat(contenidoWeb1).isNotEqualTo(contenidoWeb2);
    }
}
