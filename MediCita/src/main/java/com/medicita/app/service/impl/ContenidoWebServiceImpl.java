package com.medicita.app.service.impl;

import com.medicita.app.domain.ContenidoWeb;
import com.medicita.app.repository.ContenidoWebRepository;
import com.medicita.app.service.ContenidoWebService;
import com.medicita.app.service.dto.ContenidoWebDTO;
import com.medicita.app.service.mapper.ContenidoWebMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.medicita.app.domain.ContenidoWeb}.
 */
@Service
@Transactional
public class ContenidoWebServiceImpl implements ContenidoWebService {

    private static final Logger LOG = LoggerFactory.getLogger(ContenidoWebServiceImpl.class);

    private final ContenidoWebRepository contenidoWebRepository;

    private final ContenidoWebMapper contenidoWebMapper;

    public ContenidoWebServiceImpl(ContenidoWebRepository contenidoWebRepository, ContenidoWebMapper contenidoWebMapper) {
        this.contenidoWebRepository = contenidoWebRepository;
        this.contenidoWebMapper = contenidoWebMapper;
    }

    @Override
    public ContenidoWebDTO save(ContenidoWebDTO contenidoWebDTO) {
        LOG.debug("Request to save ContenidoWeb : {}", contenidoWebDTO);
        ContenidoWeb contenidoWeb = contenidoWebMapper.toEntity(contenidoWebDTO);
        contenidoWeb = contenidoWebRepository.save(contenidoWeb);
        return contenidoWebMapper.toDto(contenidoWeb);
    }

    @Override
    public ContenidoWebDTO update(ContenidoWebDTO contenidoWebDTO) {
        LOG.debug("Request to update ContenidoWeb : {}", contenidoWebDTO);
        ContenidoWeb contenidoWeb = contenidoWebMapper.toEntity(contenidoWebDTO);
        contenidoWeb = contenidoWebRepository.save(contenidoWeb);
        return contenidoWebMapper.toDto(contenidoWeb);
    }

    @Override
    public Optional<ContenidoWebDTO> partialUpdate(ContenidoWebDTO contenidoWebDTO) {
        LOG.debug("Request to partially update ContenidoWeb : {}", contenidoWebDTO);

        return contenidoWebRepository
            .findById(contenidoWebDTO.getId())
            .map(existingContenidoWeb -> {
                contenidoWebMapper.partialUpdate(existingContenidoWeb, contenidoWebDTO);

                return existingContenidoWeb;
            })
            .map(contenidoWebRepository::save)
            .map(contenidoWebMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContenidoWebDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ContenidoWebs");
        return contenidoWebRepository.findAll(pageable).map(contenidoWebMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContenidoWebDTO> findOne(Long id) {
        LOG.debug("Request to get ContenidoWeb : {}", id);
        return contenidoWebRepository.findById(id).map(contenidoWebMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ContenidoWeb : {}", id);
        contenidoWebRepository.deleteById(id);
    }
}
