package com.medicita.app.service.impl;

import com.medicita.app.domain.Disponibilidad;
import com.medicita.app.repository.DisponibilidadRepository;
import com.medicita.app.service.DisponibilidadService;
import com.medicita.app.service.dto.DisponibilidadDTO;
import com.medicita.app.service.mapper.DisponibilidadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.medicita.app.domain.Disponibilidad}.
 */
@Service
@Transactional
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private static final Logger LOG = LoggerFactory.getLogger(DisponibilidadServiceImpl.class);

    private final DisponibilidadRepository disponibilidadRepository;

    private final DisponibilidadMapper disponibilidadMapper;

    public DisponibilidadServiceImpl(DisponibilidadRepository disponibilidadRepository, DisponibilidadMapper disponibilidadMapper) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.disponibilidadMapper = disponibilidadMapper;
    }

    @Override
    public DisponibilidadDTO save(DisponibilidadDTO disponibilidadDTO) {
        LOG.debug("Request to save Disponibilidad : {}", disponibilidadDTO);
        Disponibilidad disponibilidad = disponibilidadMapper.toEntity(disponibilidadDTO);
        disponibilidad = disponibilidadRepository.save(disponibilidad);
        return disponibilidadMapper.toDto(disponibilidad);
    }

    @Override
    public DisponibilidadDTO update(DisponibilidadDTO disponibilidadDTO) {
        LOG.debug("Request to update Disponibilidad : {}", disponibilidadDTO);
        Disponibilidad disponibilidad = disponibilidadMapper.toEntity(disponibilidadDTO);
        disponibilidad = disponibilidadRepository.save(disponibilidad);
        return disponibilidadMapper.toDto(disponibilidad);
    }

    @Override
    public Optional<DisponibilidadDTO> partialUpdate(DisponibilidadDTO disponibilidadDTO) {
        LOG.debug("Request to partially update Disponibilidad : {}", disponibilidadDTO);

        return disponibilidadRepository
            .findById(disponibilidadDTO.getId())
            .map(existingDisponibilidad -> {
                disponibilidadMapper.partialUpdate(existingDisponibilidad, disponibilidadDTO);

                return existingDisponibilidad;
            })
            .map(disponibilidadRepository::save)
            .map(disponibilidadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisponibilidadDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Disponibilidads");
        return disponibilidadRepository.findAll(pageable).map(disponibilidadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DisponibilidadDTO> findOne(Long id) {
        LOG.debug("Request to get Disponibilidad : {}", id);
        return disponibilidadRepository.findById(id).map(disponibilidadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Disponibilidad : {}", id);
        disponibilidadRepository.deleteById(id);
    }
}
