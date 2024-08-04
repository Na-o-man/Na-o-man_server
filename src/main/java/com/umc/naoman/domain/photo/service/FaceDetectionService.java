package com.umc.naoman.domain.photo.service;

import java.util.List;

public interface FaceDetectionService {

    void detectFaces(List<String> nameList, Long shareGroupId);
}
