package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.converter.AgendaPhotoConverter;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.repository.AgendaPhotoRepository;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.repository.PhotoRepository;
import com.umc.naoman.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.umc.naoman.global.error.code.PhotoErrorCode.PHOTO_NOT_FOUND_BY_PHOTO_ID;

@Service
@RequiredArgsConstructor
public class AgendaPhotoServiceImpl implements AgendaPhotoService {

    private final AgendaPhotoRepository agendaPhotoRepository;
    private final PhotoRepository photoRepository;

    @Override
    @Transactional
    public void saveAgendasPhotos(Agenda agenda, List<Long> photos) {
        for (Long photoId : photos) {
            Photo photo = photoRepository.findById(photoId).
                    orElseThrow(() -> new BusinessException(PHOTO_NOT_FOUND_BY_PHOTO_ID));
            agendaPhotoRepository.save(AgendaPhotoConverter.toEntity(agenda, photo));
        }
    }
}
