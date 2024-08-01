package com.umc.naoman.domain.photo.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoService {

    List<PhotoResponse.PreSignedUrlInfo> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request, Member member);

    PhotoResponse.PhotoUploadInfo uploadPhotoList(PhotoRequest.PhotoUploadRequest request, Member member);

    Page<Photo> getAllPhotoList(Long shareGroupId, Member member, Pageable pageable);

    List<Photo> deletePhotoList(PhotoRequest.PhotoDeletedRequest request, Member member);
}
