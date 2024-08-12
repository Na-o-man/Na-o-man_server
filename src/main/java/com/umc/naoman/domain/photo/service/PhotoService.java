package com.umc.naoman.domain.photo.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoRequest.UploadSamplePhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoDownloadUrlListInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoUploadInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PreSignedUrlInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.SamplePhotoUploadInfo;
import com.umc.naoman.domain.photo.entity.Photo;

import java.util.List;

public interface PhotoService {
    List<PreSignedUrlInfo> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request, Member member);

    SamplePhotoUploadInfo uploadSamplePhotoList(UploadSamplePhotoRequest request, Member member);

    PhotoUploadInfo uploadPhotoList(PhotoRequest.PhotoUploadRequest request, Member member);

    PhotoDownloadUrlListInfo getPhotoDownloadUrlList(List<Long> photoIdList, Long shareGroupId, Member member);

    PhotoDownloadUrlListInfo getPhotoDownloadUrlListByProfile(Long shareGroupId, Long profileId, Member member);

    PhotoDownloadUrlListInfo getEtcPhotoDownloadUrlList(Long shareGroupId, Member member);

    List<Photo> deletePhotoList(PhotoRequest.PhotoDeletedRequest request, Member member);

    Photo findPhoto(Long photoId);
    boolean hasSamplePhoto(Member member);
}
