package com.medicita.app.service.impl;

import com.medicita.app.domain.Medico;
import com.medicita.app.repository.MedicoRepository;
import com.medicita.app.service.MedicoService;
import com.medicita.app.service.dto.MedicoDTO;
import com.medicita.app.service.mapper.MedicoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.medicita.app.domain.Medico}.
 */
@Service
@Transactional
public class MedicoServiceImpl implements MedicoService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicoServiceImpl.class);

    private final MedicoRepository medicoRepository;

    private final MedicoMapper medicoMapper;

    public MedicoServiceImpl(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    @Override
    public MedicoDTO save(MedicoDTO medicoDTO) {
        LOG.debug("Request to save Medico : {}", medicoDTO);
        Medico medico = medicoMapper.toEntity(medicoDTO);
        medico = medicoRepository.save(medico);
        return medicoMapper.toDto(medico);
    }

    @Override
    public MedicoDTO update(MedicoDTO medicoDTO) {
        LOG.debug("Request to update Medico : {}", medicoDTO);
        Medico medico = medicoMapper.toEntity(medicoDTO);
        medico = medicoRepository.save(medico);
        return medicoMapper.toDto(medico);
    }

    @Override
    public Optional<MedicoDTO> partialUpdate(MedicoDTO medicoDTO) {
        LOG.debug("Request to partially update Medico : {}", medicoDTO);

        return medicoRepository
            .findById(medicoDTO.getId())
            .map(existingMedico -> {
                medicoMapper.partialUpdate(existingMedico, medicoDTO);

                return existingMedico;
            })
            .map(medicoRepository::save)
            .map(medicoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Medicos");
        return medicoRepository.findAll(pageable).map(medicoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicoDTO> findOne(Long id) {
        LOG.debug("Request to get Medico : {}", id);
        return medicoRepository.findById(id).map(medicoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Medico : {}", id);
        medicoRepository.deleteById(id);
    }
}
