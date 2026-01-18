package com.medicita.app.service.mapper;

import com.medicita.app.domain.Adjunto;
import com.medicita.app.domain.Paciente;
import com.medicita.app.service.dto.AdjuntoDTO;
import com.medicita.app.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {
    @Mapping(target = "foto", source = "foto", qualifiedByName = "adjuntoId")
    PacienteDTO toDto(Paciente s);

    @Named("adjuntoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdjuntoDTO toDtoAdjuntoId(Adjunto adjunto);
}
