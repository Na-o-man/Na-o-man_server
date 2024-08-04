package com.umc.naoman.domain.agenda.converter;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.photo.entity.Photo;

public class AgendaPhotoConverter {

    public static AgendaPhoto toEntity(Agenda agenda, Photo photo) {
        return AgendaPhoto.builder()
                .agenda(agenda)
                .photo(photo)
                .build();
    }
}
