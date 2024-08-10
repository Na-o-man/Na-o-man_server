package com.umc.naoman.domain.photo.elasticsearch.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PhotoEsService {

    Page<PhotoEs> getPhotoEsListByShareGroupId(Long shareGroupId, Member member, Pageable pageable);

    Page<PhotoEs> getPhotoEsListByShareGroupIdAndFaceTag(Long shareGroupId, Long faceTag, Member member, Pageable pageable) throws IOException;
}
