package com.medicita.app.service.impl;

import com.medicita.app.domain.Adjunto;
import com.medicita.app.repository.AdjuntoRepository;
import com.medicita.app.service.AdjuntoService;
import com.medicita.app.service.dto.AdjuntoDTO;
import com.medicita.app.service.mapper.AdjuntoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.medicita.app.domain.Adjunto}.
 */
@Service
@Transactional
public class AdjuntoServiceImpl implements AdjuntoService {

    private static final Logger LOG = LoggerFactory.getLogger(AdjuntoServiceImpl.class);

    private final AdjuntoRepository adjuntoRepository;

    private final AdjuntoMapper adjuntoMapper;

    public AdjuntoServiceImpl(AdjuntoRepository adjuntoRepository, AdjuntoMapper adjuntoMapper) {
        this.adjuntoRepository = adjuntoRepository;
        this.adjuntoMapper = adjuntoMapper;
    }

    @Override
    public AdjuntoDTO save(AdjuntoDTO adjuntoDTO) {
        LOG.debug("Request to save Adjunto : {}", adjuntoDTO);
        Adjunto adjunto = adjuntoMapper.toEntity(adjuntoDTO);
        adjunto = adjuntoRepository.save(adjunto);
        return adjuntoMapper.toDto(adjunto);
    }

    @Override
    public AdjuntoDTO update(AdjuntoDTO adjuntoDTO) {
        LOG.debug("Request to update Adjunto : {}", adjuntoDTO);
        Adjunto adjunto = adjuntoMapper.toEntity(adjuntoDTO);
        adjunto = adjuntoRepository.save(adjunto);
        return adjuntoMapper.toDto(adjunto);
    }

    @Override
    public Optional<AdjuntoDTO> partialUpdate(AdjuntoDTO adjuntoDTO) {
        LOG.debug("Request to partially update Adjunto : {}", adjuntoDTO);

        return adjuntoRepository
            .findById(adjuntoDTO.getId())
            .map(existingAdjunto -> {
                adjuntoMapper.partialUpdate(existingAdjunto, adjuntoDTO);

                return existingAdjunto;
            })
            .map(adjuntoRepository::save)
            .map(adjuntoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdjuntoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Adjuntos");
        return adjuntoRepository.findAll(pageable).map(adjuntoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdjuntoDTO> findOne(Long id) {
        LOG.debug("Request to get Adjunto : {}", id);
        return adjuntoRepository.findById(id).map(adjuntoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Adjunto : {}", id);
        adjuntoRepository.deleteById(id);
    }
}
