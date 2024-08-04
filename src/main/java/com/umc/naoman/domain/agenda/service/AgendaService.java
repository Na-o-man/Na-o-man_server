package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.dto.AgendaRequest;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.shareGroup.entity.Profile;

public interface AgendaService {

    Agenda findAgenda(Long agendaId);
    Agenda createAgenda(Profile profile, AgendaRequest.CreateAgendaRequest request);
}