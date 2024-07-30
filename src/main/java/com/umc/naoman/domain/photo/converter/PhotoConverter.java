package com.umc.naoman.domain.photo.converter;

import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public PhotoResponse.PhotoListInfo toPhotoListInfo(Page<Photo> photoList) {
        List<PhotoResponse.PhotoInfo> photoInfoList = photoList.stream()
                .map(this::toPhotoInfo)
                .collect(Collectors.toList());

        return PhotoResponse.PhotoListInfo.builder()
                .isLast(photoList.isLast())
                .isFirst(photoList.isFirst())
                .totalPage(photoList.getTotalPages())
                .totalElements(photoList.getTotalElements())
                .listSize(photoInfoList.size())
                .photoInfoList(photoInfoList)
                .build();
    }

    public PhotoResponse.PhotoInfo toPhotoInfo(Photo photo) {
        return PhotoResponse.PhotoInfo.builder()
                .photoUrl(photo.getUrl())
                .photoName(photo.getName())
                .resizedPhotoName(convertExtension(photo.getName()))
                .uploadTime(photo.getCreatedAt())
                .build();
    }

    // HEIC에서 JPG로 확장자를 변환하는 메서드
    private String convertExtension(String photoUrl) {
        return photoUrl.replace(".HEIC", ".jpg");
    }

}
