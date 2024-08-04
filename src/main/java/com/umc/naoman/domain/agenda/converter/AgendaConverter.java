package com.umc.naoman.domain.agenda.converter;

import com.umc.naoman.domain.agenda.dto.AgendaResponse;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.stereotype.Component;

@Component
public class AgendaConverter {

    public AgendaResponse.AgendaInfo toCreateAgenda(Agenda agenda){
        return AgendaResponse.AgendaInfo.builder()
                .agendaId(agenda.getId())
                .createdAt(agenda.getCreatedAt())
                .build();
    }

    public Agenda toEntity(Profile profile, String title, ShareGroup shareGroup){
        return Agenda.builder()
                .profile(profile)
                .title(title)
                .shareGroup(shareGroup)
                .build();
    }
}
