package com.umc.naoman.domain.photo.service;

import java.util.List;

public interface FaceDetectionService {

    void detectFace(List<String> nameList, Long shareGroupId);
}
