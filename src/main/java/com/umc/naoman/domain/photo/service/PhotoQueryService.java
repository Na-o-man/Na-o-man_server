package com.umc.naoman.domain.photo.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.entity.Photo;

public interface PhotoQueryService {
    Photo findPhoto(Long photoId);
    boolean hasSamplePhoto(Member member);
}
