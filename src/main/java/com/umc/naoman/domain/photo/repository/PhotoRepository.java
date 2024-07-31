package com.umc.naoman.domain.photo.repository;

import com.umc.naoman.domain.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Page<Photo> findAllByShareGroupId(Long shareGroupId, Pageable pageable);
}
