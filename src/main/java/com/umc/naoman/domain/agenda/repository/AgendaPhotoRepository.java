package com.umc.naoman.domain.agenda.repository;

import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaPhotoRepository extends JpaRepository<AgendaPhoto, Long> {
    List<AgendaPhoto> findByAgendaId(Long agendaId);
    List<AgendaPhoto> findByPhotoId(Long photoId);
}
