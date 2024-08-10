package com.umc.naoman.domain.photo.elasticsearch.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhotoEsService {
    Page<PhotoEs> getPhotoEsListByShareGroupIdAndFaceTag(Long shareGroupId, Long profileId, Member member, Pageable pageable);
    Page<PhotoEs> getAllPhotoEsListByShareGroupId(Long shareGroupId, Member member, Pageable pageable);
    Page<PhotoEs> getEtcPhotoEsListByShareGroupId(Long shareGroupId, Member member, Pageable pageable);
}
