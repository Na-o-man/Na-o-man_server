package com.umc.naoman.domain.photo.service;

import java.util.List;

public interface FaceDetectionService {

    void detectFaceUploadPhoto(List<String> photoNameList, Long shareGroupId);
    void detectFaceJoinShareGroup(Long memberId, Long shareGroupId);
}
