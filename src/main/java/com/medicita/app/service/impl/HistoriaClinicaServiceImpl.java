package com.medicita.app.service.impl;

import com.medicita.app.domain.HistoriaClinica;
import com.medicita.app.repository.HistoriaClinicaRepository;
import com.medicita.app.service.HistoriaClinicaService;
import com.medicita.app.service.dto.HistoriaClinicaDTO;
import com.medicita.app.service.mapper.HistoriaClinicaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.medicita.app.domain.HistoriaClinica}.
 */
@Service
@Transactional
public class HistoriaClinicaServiceImpl implements HistoriaClinicaService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriaClinicaServiceImpl.class);

    private final HistoriaClinicaRepository historiaClinicaRepository;

    private final HistoriaClinicaMapper historiaClinicaMapper;

    public HistoriaClinicaServiceImpl(HistoriaClinicaRepository historiaClinicaRepository, HistoriaClinicaMapper historiaClinicaMapper) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.historiaClinicaMapper = historiaClinicaMapper;
    }

    @Override
    public HistoriaClinicaDTO save(HistoriaClinicaDTO historiaClinicaDTO) {
        LOG.debug("Request to save HistoriaClinica : {}", historiaClinicaDTO);
        HistoriaClinica historiaClinica = historiaClinicaMapper.toEntity(historiaClinicaDTO);
        historiaClinica = historiaClinicaRepository.save(historiaClinica);
        return historiaClinicaMapper.toDto(historiaClinica);
    }

    @Override
    public HistoriaClinicaDTO update(HistoriaClinicaDTO historiaClinicaDTO) {
        LOG.debug("Request to update HistoriaClinica : {}", historiaClinicaDTO);
        HistoriaClinica historiaClinica = historiaClinicaMapper.toEntity(historiaClinicaDTO);
        historiaClinica = historiaClinicaRepository.save(historiaClinica);
        return historiaClinicaMapper.toDto(historiaClinica);
    }

    @Override
    public Optional<HistoriaClinicaDTO> partialUpdate(HistoriaClinicaDTO historiaClinicaDTO) {
        LOG.debug("Request to partially update HistoriaClinica : {}", historiaClinicaDTO);

        return historiaClinicaRepository
            .findById(historiaClinicaDTO.getId())
            .map(existingHistoriaClinica -> {
                historiaClinicaMapper.partialUpdate(existingHistoriaClinica, historiaClinicaDTO);

                return existingHistoriaClinica;
            })
            .map(historiaClinicaRepository::save)
            .map(historiaClinicaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriaClinicaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HistoriaClinicas");
        return historiaClinicaRepository.findAll(pageable).map(historiaClinicaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriaClinicaDTO> findOne(Long id) {
        LOG.debug("Request to get HistoriaClinica : {}", id);
        return historiaClinicaRepository.findById(id).map(historiaClinicaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HistoriaClinica : {}", id);
        historiaClinicaRepository.deleteById(id);
    }
}
