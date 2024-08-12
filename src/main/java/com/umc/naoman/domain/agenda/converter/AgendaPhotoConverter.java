package com.umc.naoman.domain.agenda.converter;

import com.umc.naoman.domain.agenda.dto.AgendaPhotoResponse;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.photo.entity.Photo;
import org.springframework.stereotype.Component;

@Component
public class AgendaPhotoConverter {

    public static AgendaPhoto toEntity(Agenda agenda, Photo photo) {
        return AgendaPhoto.builder()
                .agenda(agenda)
                .photo(photo)
                .build();
    }

    public AgendaPhotoResponse.AgendaPhotoInfo toAgendaPhotoInfo(AgendaPhoto agendaPhoto) {
        return AgendaPhotoResponse.AgendaPhotoInfo
                .builder()
                .agendaPhotoId(agendaPhoto.getId())
                .url(agendaPhoto.getPhoto() != null ? agendaPhoto.getPhoto().getUrl() : null)
                .build();
    }
}