package com.umc.naoman.domain.photo.controller;

import com.umc.naoman.domain.photo.converter.PhotoConverter;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.service.PhotoServiceImpl;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.PhotoResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoServiceImpl photoServiceImpl;
    private final PhotoConverter photoConverter;

    @PostMapping("/preSignedUrl")
    public ResultResponse<PhotoResponse.PreSignedUrlListInfo> getPreSignedUrlList(@Valid @RequestBody PhotoRequest.PreSignedUrlRequest request) {
        List<PhotoResponse.PreSignedUrlInfo> preSignedUrlList = photoServiceImpl.getPreSignedUrlList(request.getImageNameList());
        return ResultResponse.of(PhotoResultCode.CREATE_SHARE_GROUP, photoConverter.toPreSignedUrlListResponse(preSignedUrlList));
    }
}
