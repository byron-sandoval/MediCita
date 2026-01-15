package com.medicita.app.service.mapper;

import com.medicita.app.domain.Disponibilidad;
import com.medicita.app.domain.Medico;
import com.medicita.app.service.dto.DisponibilidadDTO;
import com.medicita.app.service.dto.MedicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Disponibilidad} and its DTO {@link DisponibilidadDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisponibilidadMapper extends EntityMapper<DisponibilidadDTO, Disponibilidad> {
    @Mapping(target = "medico", source = "medico", qualifiedByName = "medicoId")
    DisponibilidadDTO toDto(Disponibilidad s);

    @Named("medicoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicoDTO toDtoMedicoId(Medico medico);
}
