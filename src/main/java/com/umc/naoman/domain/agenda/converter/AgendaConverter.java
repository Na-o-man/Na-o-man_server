package com.umc.naoman.domain.agenda.converter;

import com.umc.naoman.domain.agenda.dto.AgendaPhotoResponse;
import com.umc.naoman.domain.agenda.dto.AgendaResponse;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AgendaConverter {

    private final AgendaPhotoConverter agendaPhotoConverter;

    public AgendaResponse.AgendaInfo toAgendaInfo(Agenda agenda){
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

    public AgendaResponse.AgendaDetailInfo toAgendaDetailInfo(Agenda agenda) {
        List<AgendaPhotoResponse.AgendaPhotoInfo> agendaPhotoInfoList = agenda.getAgendaPhotoList()
                .stream()
                .map(agendaPhotoConverter::toAgendaPhotoInfo)
                .collect(Collectors.toList());

        return AgendaResponse.AgendaDetailInfo.builder()
                .agendaId(agenda.getId())
                .title(agenda.getTitle())
                .agendaPhotoInfoList(agendaPhotoInfoList)
                .build();
    }
}
