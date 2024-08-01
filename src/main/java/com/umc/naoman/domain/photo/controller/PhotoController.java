package com.umc.naoman.domain.photo.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.converter.PhotoConverter;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.service.PhotoService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.security.annotation.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.umc.naoman.global.result.code.PhotoResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoConverter photoConverter;

    @PostMapping("/preSignedUrl")
    public ResultResponse<PhotoResponse.PreSignedUrlListInfo> getPreSignedUrlList(@Valid @RequestBody PhotoRequest.PreSignedUrlRequest request,
                                                                                  @LoginMember Member member) {
        List<PhotoResponse.PreSignedUrlInfo> preSignedUrlList = photoService.getPreSignedUrlList(request, member);
        return ResultResponse.of(CREATE_PRESIGNED_URL, photoConverter.toPreSignedUrlListInfo(preSignedUrlList));
    }

    @PostMapping("/upload")
    public ResultResponse<PhotoResponse.PhotoUploadInfo> uploadPhotoList(@Valid @RequestBody PhotoRequest.PhotoUploadRequest request) {
        PhotoResponse.PhotoUploadInfo photoUploadInfo = photoService.uploadPhotoList(request);
        return ResultResponse.of(UPLOAD_PHOTO, photoUploadInfo);
    }

    @GetMapping("/all")
    public ResultResponse<PhotoResponse.PagedPhotoInfo> getAllPhotoListByShareGroup(@RequestParam Long shareGroupId, @LoginMember Member member,
                                                                                    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Photo> allPhotoListByShareGroup = photoService.getAllPhotoList(shareGroupId, member, pageable);
        return ResultResponse.of(RETRIEVE_PHOTO, photoConverter.toPhotoListInfo(allPhotoListByShareGroup));
    }

    @DeleteMapping
    public ResultResponse<PhotoResponse.PhotoDeleteInfo> deletePhotoList(@Valid @RequestBody PhotoRequest.PhotoDeletedRequest request,
                                                                         @LoginMember Member member) {
        List<Photo> photoList = photoService.deletePhotoList(request, member);
        return ResultResponse.of(DELETE_PHOTO, photoConverter.toPhotoDeleteInfo(photoList));
    }
}
