package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.converter.AgendaConverter;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.repository.AgendaRepository;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.repository.ShareGroupRepository;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.AgendaErrorCode;
import com.umc.naoman.global.error.code.ShareGroupErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository agendaRepository;
    private final ShareGroupRepository shareGroupRepository;

    @Override
    @Transactional(readOnly = true)
    public Agenda findAgenda(Long agendaId) {
        return agendaRepository.findById(agendaId)
                .orElseThrow(() -> new BusinessException(AgendaErrorCode.AGENDA_NOT_FOUND_BY_AGENDA_ID));
    }

    @Override
    @Transactional
    public Agenda createAgenda(Member member, Long shareGroupId, String title) {
        ShareGroup shareGroup = shareGroupRepository.findById(shareGroupId)
                .orElseThrow(() -> new BusinessException(ShareGroupErrorCode.SHARE_GROUP_NOT_FOUND));
        Agenda newAgenda = AgendaConverter.toAgenda(member,title,shareGroup);
        return agendaRepository.save(newAgenda);
    }
}
