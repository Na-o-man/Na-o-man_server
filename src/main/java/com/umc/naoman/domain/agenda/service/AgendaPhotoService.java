package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;

import java.util.List;

public interface AgendaPhotoService {
    void saveAgendaPhotoList(Agenda agenda, List<Long> photos);

    AgendaPhoto findAgendaPhoto(Long agendaPhotoId);
    List<AgendaPhoto> findAgendaPhotoList(Long agendaId);
    List<AgendaPhoto> findAgendaPhotoListByPhotoId(Long photoId);
}
