package com.umc.naoman.domain.photo.elasticsearch.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import com.umc.naoman.domain.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoEsService {
    Page<PhotoEs> getPhotoEsListByShareGroupId(Long shareGroupId, Member member, Pageable pageable);
    Page<PhotoEs> getPhotoEsListByShareGroupIdAndFaceTag(Long shareGroupId, Long profileId, Member member, Pageable pageable);
}
