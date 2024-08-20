package com.umc.naoman.domain.agenda.service;

import com.umc.naoman.domain.agenda.converter.AgendaPhotoConverter;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.agenda.repository.AgendaPhotoRepository;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.service.PhotoQueryService;
import com.umc.naoman.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.umc.naoman.global.error.code.AgendaErrorCode.AGENDA_PHOTO_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AgendaPhotoServiceImpl implements AgendaPhotoService {
    private final PhotoQueryService photoQueryService;
    private final AgendaPhotoRepository agendaPhotoRepository;

    @Override
    @Transactional
    public void saveAgendaPhotoList(Agenda agenda, List<Long> photos) {
        for (Long photoId : photos) {
            Photo photo = photoQueryService.findPhoto(photoId);
            agendaPhotoRepository.save(AgendaPhotoConverter.toEntity(agenda, photo));
        }
    }

    @Override
    public AgendaPhoto findAgendaPhoto(Long agendaPhotoId) {
        return agendaPhotoRepository.findById(agendaPhotoId)
                .orElseThrow(() -> new BusinessException(AGENDA_PHOTO_NOT_FOUND));
    }

    @Override
    public List<AgendaPhoto> findAgendaPhotoList(Long agendaId) {
        return agendaPhotoRepository.findByAgendaId(agendaId);
    }

    @Override
    public List<AgendaPhoto> findAgendaPhotoListByPhotoId(Long photoId) {
        return agendaPhotoRepository.findByPhotoId(photoId);
    }

    @Override
    @Transactional
    public void nullifyPhotoInAgendaPhotoList(List<Long> photoIdList) {
        List<AgendaPhoto> agendaPhotoList = agendaPhotoRepository.findByPhotoIdIn(photoIdList);
        agendaPhotoList
                .forEach(AgendaPhoto::nullifyPhoto);
    }
}
