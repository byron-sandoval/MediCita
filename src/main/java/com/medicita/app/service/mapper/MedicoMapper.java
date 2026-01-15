package com.medicita.app.service.mapper;

import com.medicita.app.domain.Adjunto;
import com.medicita.app.domain.Medico;
import com.medicita.app.service.dto.AdjuntoDTO;
import com.medicita.app.service.dto.MedicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medico} and its DTO {@link MedicoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicoMapper extends EntityMapper<MedicoDTO, Medico> {
    @Mapping(target = "foto", source = "foto", qualifiedByName = "adjuntoId")
    MedicoDTO toDto(Medico s);

    @Named("adjuntoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdjuntoDTO toDtoAdjuntoId(Adjunto adjunto);
}
