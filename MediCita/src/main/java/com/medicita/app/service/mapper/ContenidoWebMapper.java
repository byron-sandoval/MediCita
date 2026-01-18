package com.medicita.app.service.mapper;

import com.medicita.app.domain.ContenidoWeb;
import com.medicita.app.service.dto.ContenidoWebDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContenidoWeb} and its DTO {@link ContenidoWebDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContenidoWebMapper extends EntityMapper<ContenidoWebDTO, ContenidoWeb> {}
