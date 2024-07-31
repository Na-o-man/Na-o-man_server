package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.entity.Agenda;

import java.util.List;

public interface AgendaPhotoService {
    void saveAgendasPhotos(Agenda agenda, List<Long> photos);
}
