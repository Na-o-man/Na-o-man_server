package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.dto.AgendaRequest;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.member.entity.Member;

public interface AgendaService {
    Agenda createAgenda(Member member, AgendaRequest.CreateAgendaRequest request);
    Agenda findAgenda(Long agendaId);
    Agenda getAgendaDetailInfo(Long agendaId, Member member);
}
