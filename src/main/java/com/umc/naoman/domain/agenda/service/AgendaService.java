package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.dto.AgendaRequest;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.member.entity.Member;

public interface AgendaService {

    Agenda findAgenda(Long agendaId);
    Agenda createAgenda(Member member, AgendaRequest.CreateAgendaRequest request);
}
