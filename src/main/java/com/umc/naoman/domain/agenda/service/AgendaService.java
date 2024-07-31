package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.member.entity.Member;

public interface AgendaService {

    public Agenda findAgenda(Long agendaId);
    public Agenda createAgenda(Member member, Long shareGroupId, String title);
}
