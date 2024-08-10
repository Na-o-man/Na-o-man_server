package com.umc.naoman.domain.photo.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.Photo;

import java.util.List;

public interface PhotoService {

    List<PhotoResponse.PreSignedUrlInfo> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request, Member member);

    PhotoResponse.PhotoUploadInfo uploadPhotoList(PhotoRequest.PhotoUploadRequest request, Member member);

    List<Photo> deletePhotoList(PhotoRequest.PhotoDeletedRequest request, Member member);

    Photo findPhoto(Long photoId);

    PhotoResponse.PhotoDownloadUrlListInfo getPhotoDownloadUrlList(List<Long> photoIdList, Long shareGroupId, Member member);
}
