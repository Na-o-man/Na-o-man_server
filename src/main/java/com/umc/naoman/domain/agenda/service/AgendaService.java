package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.dto.AgendaRequest;
import com.umc.naoman.domain.agenda.dto.AgendaResponse.PagedAgendaDetailInfo;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgendaService {
    Agenda createAgenda(Member member, AgendaRequest.CreateAgendaRequest request);
    Agenda getAgendaDetailInfo(Long agendaId, Member member);
    PagedAgendaDetailInfo getAgendaList(Long shareGroupId, Member member, Pageable pageable);
    Agenda deleteAgenda(Long agendaId);

    Agenda findAgenda(Long agendaId);
    List<Agenda> findAgendaListByPhotoId(Long photoId);
}
