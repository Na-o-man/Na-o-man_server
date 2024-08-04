package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.converter.AgendaConverter;
import com.umc.naoman.domain.agenda.dto.AgendaRequest;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.repository.AgendaRepository;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.AgendaErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository agendaRepository;
    private final ShareGroupService shareGroupService;
    private final AgendaPhotoService agendaPhotoService;
    private final AgendaConverter agendaConverter;

    @Override
    @Transactional(readOnly = true)
    public Agenda findAgenda(Long agendaId) {
        return agendaRepository.findById(agendaId)
                .orElseThrow(() -> new BusinessException(AgendaErrorCode.AGENDA_NOT_FOUND_BY_AGENDA_ID));
    }

    @Override
    @Transactional
    public Agenda createAgenda(Profile profile, AgendaRequest.CreateAgendaRequest request) {
        ShareGroup shareGroup = shareGroupService.findShareGroup(request.getShareGroupId());
        Agenda newAgenda = agendaConverter.toEntity(profile,request.getTitle(),shareGroup);
        agendaPhotoService.saveAgendaPhotos(newAgenda,request.getAgendasPhotoList());

        return agendaRepository.save(newAgenda);
    }
}