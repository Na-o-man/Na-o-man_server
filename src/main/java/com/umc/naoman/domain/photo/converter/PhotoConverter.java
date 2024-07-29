package com.umc.naoman.domain.photo.converter;

import com.umc.naoman.domain.photo.dto.PhotoResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhotoConverter {

    public PhotoResponse.PreSignedUrlListInfo toPreSignedUrlListInfo(List<PhotoResponse.PreSignedUrlInfo> preSignedUrlList) {
        List<PhotoResponse.PreSignedUrlInfo> preSignedUrlInfoList = preSignedUrlList.stream()
                .map(preSignedUrlInfo -> toPreSignedUrlInfo(
                        preSignedUrlInfo.getPreSignedUrl(),
                        preSignedUrlInfo.getImageUrl(),
                        preSignedUrlInfo.getImageName()
                ))
                .collect(Collectors.toList());

        return PhotoResponse.PreSignedUrlListInfo.builder()
                .preSignedUrlInfoList(preSignedUrlInfoList)
                .build();
    }

    public PhotoResponse.PreSignedUrlInfo toPreSignedUrlInfo(String preSignedUrl, String imageUrl, String imageName) {
        return PhotoResponse.PreSignedUrlInfo.builder()
                .preSignedUrl(preSignedUrl)
                .imageUrl(imageUrl)
                .imageName(imageName)
                .build();
    }

}
