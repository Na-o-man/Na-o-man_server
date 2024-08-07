package com.umc.naoman.domain.vote.repository;

import com.umc.naoman.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Boolean existsByProfileIdAndAgendaPhotoId(Long profileId, Long agendaPhotoId);
}