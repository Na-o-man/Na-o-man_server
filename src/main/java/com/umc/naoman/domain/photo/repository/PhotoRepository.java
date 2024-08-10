package com.umc.naoman.domain.photo.repository;

import com.umc.naoman.domain.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByIdInAndShareGroupId(List<Long> photoIdList, Long shareGroupId);
    List<Photo> findByIdIn(List<Long> photoIdList);
}
