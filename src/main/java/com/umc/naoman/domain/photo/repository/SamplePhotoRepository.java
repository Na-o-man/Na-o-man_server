package com.umc.naoman.domain.photo.repository;

import com.umc.naoman.domain.photo.entity.SamplePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SamplePhotoRepository extends JpaRepository<SamplePhoto, Long> {
    boolean existsByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
}
