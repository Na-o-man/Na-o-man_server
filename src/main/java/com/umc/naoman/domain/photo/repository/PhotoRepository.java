package com.umc.naoman.domain.photo.repository;

import com.umc.naoman.domain.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
