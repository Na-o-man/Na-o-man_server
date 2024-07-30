package com.umc.naoman.domain.photo.converter;

import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
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
}
