package com.umc.naoman.domain.photo.converter;

import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.umc.naoman.domain.photo.service.PhotoServiceImpl.*;

@Component
public class PhotoConverter {

    public PhotoResponse.PreSignedUrlListInfo toPreSignedUrlListInfo(List<PhotoResponse.PreSignedUrlInfo> preSignedUrlList) {
        List<PhotoResponse.PreSignedUrlInfo> preSignedUrlInfoList = preSignedUrlList.stream()
                .map(preSignedUrlInfo -> toPreSignedUrlInfo(
                        preSignedUrlInfo.getPreSignedUrl(),
                        preSignedUrlInfo.getPhotoUrl(),
                        preSignedUrlInfo.getPhotoName()
                ))
                .collect(Collectors.toList());

        return PhotoResponse.PreSignedUrlListInfo.builder()
                .preSignedUrlInfoList(preSignedUrlInfoList)
                .build();
    }

    public PhotoResponse.PreSignedUrlInfo toPreSignedUrlInfo(String preSignedUrl, String photoUrl, String photoName) {
        return PhotoResponse.PreSignedUrlInfo.builder()
                .preSignedUrl(preSignedUrl)
                .photoUrl(photoUrl)
                .photoName(photoName)
                .build();
    }

    public Photo toEntity(String photoUrl, String photoName, ShareGroup shareGroup) {
        return Photo.builder()
                .url(photoUrl)
                .name(photoName)
                .shareGroup(shareGroup)
                .build();
    }

//    public PhotoResponse.PagedPhotoInfo toPagedPhotoInfo(Page<Photo> photoList) {
//        List<PhotoResponse.PhotoInfo> photoInfoList = photoList.stream()
//                .map(this::toPhotoInfo)
//                .collect(Collectors.toList());
//
//        return PhotoResponse.PagedPhotoInfo.builder()
//                .isLast(photoList.isLast())
//                .isFirst(photoList.isFirst())
//                .totalPages(photoList.getTotalPages())
//                .totalElements(photoList.getTotalElements())
//                .photoInfoList(photoInfoList)
//                .build();
//    }
//
//    public PhotoResponse.PhotoInfo toPhotoInfo(Photo photo) {
//        String rawUrl = photo.getUrl();
//
//        return PhotoResponse.PhotoInfo.builder()
//                .photoId(photo.getId())
//                .rawPhotoUrl(rawUrl)
//                .w200PhotoUrl(createResizedPhotoUrl(rawUrl, W200_PATH_PREFIX))
//                .w400PhotoUrl(createResizedPhotoUrl(rawUrl, W400_PATH_PREFIX))
//                .createdAt(photo.getCreatedAt())
//                .build();
//    }

    public PhotoResponse.PagedPhotoEsInfo toPagedPhotoEsInfo(Page<PhotoEs> photoEsList) {
        List<PhotoResponse.PhotoEsInfo> photoEsInfoList = photoEsList.stream()
                .map(this::toPhotoEsInfo)
                .collect(Collectors.toList());

        return PhotoResponse.PagedPhotoEsInfo.builder()
                .isLast(photoEsList.isLast())
                .isFirst(photoEsList.isFirst())
                .totalPages(photoEsList.getTotalPages())
                .totalElements(photoEsList.getTotalElements())
                .photoEsInfoList(photoEsInfoList)
                .build();
    }

    public PhotoResponse.PhotoEsInfo toPhotoEsInfo(PhotoEs photoEs) {
        String rawUrl = photoEs.getUrl();

        return PhotoResponse.PhotoEsInfo.builder()
                .photoId(Long.valueOf(photoEs.getId()))
                .rawPhotoUrl(rawUrl)
                .w200PhotoUrl(createResizedPhotoUrl(rawUrl, W200_PATH_PREFIX))
                .w400PhotoUrl(createResizedPhotoUrl(rawUrl, W400_PATH_PREFIX))
                .downloadTag(photoEs.getDownloadTag())
                .createdAt(LocalDateTime.parse(photoEs.getCreatedAt()))
                .build();
    }


    private String createResizedPhotoUrl(String photoUrl, String size) {
        String resizedUrl = getResizedUrl(photoUrl, size);
        return convertExtension(resizedUrl);
    }

    // 리사이즈된 사진의 URL로 변환하는 메서드
    private String getResizedUrl(String photoUrl, String size) {
        return photoUrl.replace("/" + RAW_PATH_PREFIX + "/", "/" + size + "/");
    }

    // 확장자 변환 메서드
    public String convertExtension(String photoUrl) {
        return photoUrl.replace(".HEIC", ".jpg");
    }

    public PhotoResponse.PhotoDeleteInfo toPhotoDeleteInfo(List<Photo> photoList) {
        List<Long> photoIdList = photoList.stream()
                .map(Photo::getId)
                .collect(Collectors.toList());

        return PhotoResponse.PhotoDeleteInfo.builder()
                .photoIdList(photoIdList)
                .build();
    }

    public PhotoResponse.PhotoDownloadUrlListInfo toPhotoDownloadUrlListInfo(List<Photo> photoList) {
        List<String> photoDownloadUrlList = photoList.stream()
                .map(Photo::getUrl)
                .collect(Collectors.toList());

        return PhotoResponse.PhotoDownloadUrlListInfo.builder()
                .photoDownloadUrlList(photoDownloadUrlList)
                .build();
    }
}
