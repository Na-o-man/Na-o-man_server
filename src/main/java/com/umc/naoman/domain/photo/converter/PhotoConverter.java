package com.umc.naoman.domain.photo.converter;

import com.umc.naoman.domain.photo.dto.PhotoResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PhotoConverter {

    public static PhotoResponse.PreSignedUrlListResponse toPreSignedUrlListResponse(List<PhotoResponse.PreSignedUrlResponse> preSignedUrlResponse) {
        List<PhotoResponse.PreSignedUrlResponse> preSignedUrlResponseList = preSignedUrlResponse.stream()
                .map(response -> PhotoResponse.PreSignedUrlResponse.builder()
                        .preSignedUrl(response.getPreSignedUrl())
                        .imageUrl(response.getImageUrl())
                        .imageName(response.getImageName())
                        .build())
                .collect(Collectors.toList());

        return PhotoResponse.PreSignedUrlListResponse.builder()
                .preSignedUrlList(preSignedUrlResponseList)
                .build();
    }

    public static PhotoResponse.PreSignedUrlResponse toPreSignedUrlResponse(String preSignedUrl, String imageUrl, String imageName) {
        return PhotoResponse.PreSignedUrlResponse.builder()
                .preSignedUrl(preSignedUrl)
                .imageUrl(imageUrl)
                .imageName(imageName)
                .build();
    }
}
