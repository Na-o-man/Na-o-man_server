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

    public PhotoResponse.PagedPhotoInfo toPhotoListInfo(Page<Photo> photoList) {
        List<PhotoResponse.PhotoInfo> photoInfoList = photoList.stream()
                .map(this::toPhotoInfo)
                .collect(Collectors.toList());

        return PhotoResponse.PagedPhotoInfo.builder()
                .isLast(photoList.isLast())
                .isFirst(photoList.isFirst())
                .totalPage(photoList.getTotalPages())
                .totalElements(photoList.getTotalElements())
                .photoInfoList(photoInfoList)
                .build();
    }

    public PhotoResponse.PhotoInfo toPhotoInfo(Photo photo) {
        String rawUrl = photo.getUrl();

        return PhotoResponse.PhotoInfo.builder()
                .rawPhotoUrl(rawUrl)
                .w200PhotoUrl(createResizedPhotoUrl(rawUrl, "w200"))
                .w400PhotoUrl(createResizedPhotoUrl(rawUrl, "w400"))
                .createdAt(photo.getCreatedAt())
                .build();
    }

    private String createResizedPhotoUrl(String photoUrl, String size) {
        String resizedUrl = getResizedUrl(photoUrl, size);
        return convertExtension(resizedUrl);
    }

    // 리사이즈된 사진의 URL로 변환하는 메서드
    private String getResizedUrl(String photoUrl, String size) {
        return photoUrl.replace("/raw/", "/" + size + "/");
    }

    // 확장자 변환 메서드
    private String convertExtension(String photoUrl) {
        return photoUrl.replace(".HEIC", ".jpg");
    }

}
