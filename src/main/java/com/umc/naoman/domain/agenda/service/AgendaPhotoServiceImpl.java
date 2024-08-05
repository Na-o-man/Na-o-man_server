package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.converter.AgendaPhotoConverter;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.repository.AgendaPhotoRepository;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaPhotoServiceImpl implements AgendaPhotoService {

    private final AgendaPhotoRepository agendaPhotoRepository;
    private final PhotoService photoService;

    @Override
    @Transactional
    public void saveAgendaPhotoList(Agenda agenda, List<Long> photos) {
        for (Long photoId : photos) {
            Photo photo = photoService.findPhoto(photoId);
            agendaPhotoRepository.save(AgendaPhotoConverter.toEntity(agenda, photo));
        }
    }
}
