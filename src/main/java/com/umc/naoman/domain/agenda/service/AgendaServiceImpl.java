package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.converter.AgendaConverter;
import com.umc.naoman.domain.agenda.dto.AgendaRequest;
import com.umc.naoman.domain.agenda.dto.AgendaRequest.CreateAgendaRequest;
import com.umc.naoman.domain.agenda.dto.AgendaResponse;
import com.umc.naoman.domain.agenda.dto.AgendaResponse.AgendaDetailInfo;
import com.umc.naoman.domain.agenda.dto.AgendaResponse.PagedAgendaDetailInfo;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.agenda.repository.AgendaRepository;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.AgendaErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaService {
    private final AgendaRepository agendaRepository;
    private final ShareGroupService shareGroupService;
    private final AgendaPhotoService agendaPhotoService;
    private final AgendaConverter agendaConverter;

    @Override
    public Agenda findAgenda(Long agendaId) {
        return agendaRepository.findById(agendaId)
                .orElseThrow(() -> new BusinessException(AgendaErrorCode.AGENDA_NOT_FOUND_BY_AGENDA_ID));
    }

    @Override
    @Transactional
    public Agenda createAgenda(Member member, CreateAgendaRequest request) {
        Long shareGroupId = request.getShareGroupId();
        ShareGroup shareGroup = shareGroupService.findShareGroup(shareGroupId);
        Profile profile = shareGroupService.findProfile(shareGroupId, member.getId());
        Agenda newAgenda = agendaConverter.toEntity(profile, request.getTitle(),shareGroup);
        agendaPhotoService.saveAgendaPhotoList(newAgenda, request.getPhotoIdList());

        return agendaRepository.save(newAgenda);
    }

    @Override
    public PagedAgendaDetailInfo getAgendaList(Long shareGroupId, Member member, Pageable pageable) {
        ShareGroup shareGroup = shareGroupService.findShareGroup(shareGroupId);

        Page<Agenda> agendaList = agendaRepository.findByShareGroupId(shareGroupId, pageable);
        return agendaConverter.toPageAgendaDetailInfo(agendaList);
    }

    @Override
    public Agenda getAgendaDetailInfo(Long agendaId, Member member) {
        Agenda agenda = findAgenda(agendaId);

        shareGroupService.findProfile(agenda.getShareGroup().getId(),
                member.getId());

        return agenda;
    }

    @Override
    @Transactional
    public Agenda deleteAgenda(Long agendaId) {
        Agenda agenda = findAgenda(agendaId);

        agenda.delete();
        return agenda;
    }

    // 특정 사진이 안건 후보로 담겨 있는 안건 목록을 조회하는 함수
    @Override
    public List<Agenda> findAgendaListByPhotoId(Long photoId) {
        List<AgendaPhoto> agendaPhotoList = agendaPhotoService.findAgendaPhotoListByPhotoId(photoId);
        return agendaPhotoList.stream()
                .map(agendaPhoto -> agendaPhoto.getAgenda())
                .collect(Collectors.toList());
    }
}