package com.medicita.app.service.mapper;

import com.medicita.app.domain.Cita;
import com.medicita.app.domain.Medico;
import com.medicita.app.domain.Paciente;
import com.medicita.app.service.dto.CitaDTO;
import com.medicita.app.service.dto.MedicoDTO;
import com.medicita.app.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cita} and its DTO {@link CitaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CitaMapper extends EntityMapper<CitaDTO, Cita> {
    @Mapping(target = "medico", source = "medico", qualifiedByName = "medicoId")
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    CitaDTO toDto(Cita s);

    @Named("medicoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicoDTO toDtoMedicoId(Medico medico);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);
}
