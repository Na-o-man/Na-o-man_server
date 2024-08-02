package com.umc.naoman.domain.agenda.converter;

import com.umc.naoman.domain.agenda.dto.AgendaResponse;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;

public class AgendaConverter {

    public static AgendaResponse.CreateAgenda toCreateAgenda(Agenda agenda){
        return AgendaResponse.CreateAgenda.builder()
                .agendaId(agenda.getId())
                .createdAt(agenda.getCreatedAt())
                .build();
    }

    public static Agenda toAgenda(Profile profile, String title, ShareGroup shareGroup){
        return Agenda.builder()
                .profile(profile)
                .title(title)
                .shareGroup(shareGroup)
                .build();
    }
}
