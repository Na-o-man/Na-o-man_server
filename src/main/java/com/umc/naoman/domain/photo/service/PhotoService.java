package com.umc.naoman.domain.photo.service;

import com.umc.naoman.domain.photo.dto.PhotoResponse;

import java.util.List;

public interface PhotoService {

    List<PhotoResponse.PreSignedUrlInfo> getPreSignedUrlList(List<String> imageNameList);
}
