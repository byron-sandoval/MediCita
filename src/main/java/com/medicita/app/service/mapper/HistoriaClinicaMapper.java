package com.medicita.app.service.mapper;

import com.medicita.app.domain.HistoriaClinica;
import com.medicita.app.domain.Paciente;
import com.medicita.app.service.dto.HistoriaClinicaDTO;
import com.medicita.app.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoriaClinica} and its DTO {@link HistoriaClinicaDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriaClinicaMapper extends EntityMapper<HistoriaClinicaDTO, HistoriaClinica> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    HistoriaClinicaDTO toDto(HistoriaClinica s);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);
}
