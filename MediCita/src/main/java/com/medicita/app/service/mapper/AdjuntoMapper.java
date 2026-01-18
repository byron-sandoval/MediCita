package com.medicita.app.service.mapper;

import com.medicita.app.domain.Adjunto;
import com.medicita.app.domain.Cita;
import com.medicita.app.domain.HistoriaClinica;
import com.medicita.app.service.dto.AdjuntoDTO;
import com.medicita.app.service.dto.CitaDTO;
import com.medicita.app.service.dto.HistoriaClinicaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Adjunto} and its DTO {@link AdjuntoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdjuntoMapper extends EntityMapper<AdjuntoDTO, Adjunto> {
    @Mapping(target = "cita", source = "cita", qualifiedByName = "citaId")
    @Mapping(target = "historiaClinica", source = "historiaClinica", qualifiedByName = "historiaClinicaId")
    AdjuntoDTO toDto(Adjunto s);

    @Named("citaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitaDTO toDtoCitaId(Cita cita);

    @Named("historiaClinicaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HistoriaClinicaDTO toDtoHistoriaClinicaId(HistoriaClinica historiaClinica);
}
