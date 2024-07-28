package com.umc.naoman.domain.photo.controller;

import com.umc.naoman.domain.photo.converter.PhotoConverter;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.service.PhotoService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.PhotoResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/preSignedUrl")
    public ResultResponse<PhotoResponse.PreSignedUrlListResponse> getPreSignedUrlList(@Valid @RequestBody PhotoRequest.PreSignedUrlRequest request) {
        List<PhotoResponse.PreSignedUrlResponse> preSignedUrlList = request.getImageNameList().stream()
                .map(photoService::getPreSignedUrl)
                .collect(Collectors.toList());

        PhotoResponse.PreSignedUrlListResponse preSignedUrlListResponse = PhotoConverter.toPreSignedUrlListResponse(preSignedUrlList);
        return ResultResponse.of(PhotoResultCode.CREATE_SHARE_GROUP, preSignedUrlListResponse);
    }
}
