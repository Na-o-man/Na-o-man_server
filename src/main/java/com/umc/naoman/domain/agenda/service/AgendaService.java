package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.shareGroup.entity.Profile;

public interface AgendaService {

    public Agenda findAgenda(Long agendaId);
    public Agenda createAgenda(Profile profile, Long shareGroupId, String title);
}
