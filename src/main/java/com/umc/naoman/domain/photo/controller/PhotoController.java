package com.umc.naoman.domain.photo.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.converter.PhotoConverter;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.service.PhotoService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/photos")
@RequiredArgsConstructor
@Tag(name = "사진 관련 API", description = "사진 업로드, 조회, 삭제, 다운로드를 처리하는 API입니다.")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoConverter photoConverter;

    @PostMapping("/preSignedUrl")
    @Operation(summary = "Presigned URL 요청 API", description = "Presigned URL을 요청하는 API입니다.")
    public ResultResponse<PhotoResponse.PreSignedUrlListInfo> getPreSignedUrlList(@Valid @RequestBody PhotoRequest.PreSignedUrlRequest request,
                                                                                  @LoginMember Member member) {
        List<PhotoResponse.PreSignedUrlInfo> preSignedUrlList = photoService.getPreSignedUrlList(request, member);
        return ResultResponse.of(CREATE_PRESIGNED_URL, photoConverter.toPreSignedUrlListInfo(preSignedUrlList));
    }

    @PostMapping("/upload")
    @Operation(summary = "사진 업로드 API", description = "Presigned URL을 통해 업로드한 사진을 데이터베이스에 저장하는 API입니다.")
    public ResultResponse<PhotoResponse.PhotoUploadInfo> uploadPhotoList(@Valid @RequestBody PhotoRequest.PhotoUploadRequest request,
                                                                         @LoginMember Member member) {
        PhotoResponse.PhotoUploadInfo photoUploadInfo = photoService.uploadPhotoList(request, member);
        return ResultResponse.of(UPLOAD_PHOTO, photoUploadInfo);
    }

    @GetMapping("/all")
    @Operation(summary = "특정 공유그룹의 전체 사진 조회 API", description = "특정 공유 그룹의 전체 사진을 조회하는 API입니다.")
    public ResultResponse<PhotoResponse.PagedPhotoInfo> getAllPhotoListByShareGroup(@RequestParam Long shareGroupId, @LoginMember Member member,
                                                                                    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Photo> allPhotoListByShareGroup = photoService.getAllPhotoList(shareGroupId, member, pageable);
        return ResultResponse.of(RETRIEVE_PHOTO, photoConverter.toPhotoListInfo(allPhotoListByShareGroup));
    }

    @DeleteMapping
    @Operation(summary = "사진 삭제 API", description = "사진을 삭제하는 API입니다. 해당 공유그룹에 속해있는 회원만 삭제할 수 있습니다.")
    public ResultResponse<PhotoResponse.PhotoDeleteInfo> deletePhotoList(@Valid @RequestBody PhotoRequest.PhotoDeletedRequest request,
                                                                         @LoginMember Member member) {
        List<Photo> photoList = photoService.deletePhotoList(request, member);
        return ResultResponse.of(DELETE_PHOTO, photoConverter.toPhotoDeleteInfo(photoList));
    }
}
