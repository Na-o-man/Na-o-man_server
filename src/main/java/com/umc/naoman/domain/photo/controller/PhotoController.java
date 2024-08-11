package com.umc.naoman.domain.photo.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.converter.PhotoConverter;
import com.umc.naoman.domain.photo.dto.PhotoRequest.PhotoDeletedRequest;
import com.umc.naoman.domain.photo.dto.PhotoRequest.PhotoUploadRequest;
import com.umc.naoman.domain.photo.dto.PhotoRequest.PreSignedUrlRequest;
import com.umc.naoman.domain.photo.dto.PhotoRequest.UploadSamplePhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PagedPhotoEsInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoDeleteInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoDownloadUrlListInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoUploadInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PreSignedUrlInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PreSignedUrlListInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.SamplePhotoUploadInfo;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import com.umc.naoman.domain.photo.elasticsearch.service.PhotoEsService;
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
@Tag(name = "03. 사진 관련 API", description = "사진 업로드, 조회, 삭제, 다운로드를 처리하는 API입니다.")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoEsService photoEsService;
    private final PhotoConverter photoConverter;

    @PostMapping("/preSignedUrl")
    @Operation(summary = "Presigned URL 요청 API", description = "Presigned URL을 요청하는 API입니다.")
    public ResultResponse<PreSignedUrlListInfo> getPreSignedUrlList(@Valid @RequestBody PreSignedUrlRequest request,
                                                                    @LoginMember Member member) {
        List<PreSignedUrlInfo> preSignedUrlList = photoService.getPreSignedUrlList(request, member);
        return ResultResponse.of(CREATE_PRESIGNED_URL, photoConverter.toPreSignedUrlListInfo(preSignedUrlList));
    }

    @PostMapping("/sample")
    @Operation(summary = "샘플 사진 업로드 API", description = "얼굴 분류에 사용할 샘플 사진을 업로드하는 API입니다.")
    public ResultResponse<SamplePhotoUploadInfo> uploadSamplePhotoList(@Valid @RequestBody UploadSamplePhotoRequest request,
                                                                       @LoginMember Member member) {
        return ResultResponse.of(UPLOAD_SAMPLE_PHOTO, photoService.uploadSamplePhotoList(request, member));
    }

    @PostMapping("/upload")
    @Operation(summary = "사진 업로드 API", description = "Presigned URL을 통해 업로드한 사진을 데이터베이스에 저장하는 API입니다.")
    public ResultResponse<PhotoUploadInfo> uploadPhotoList(@Valid @RequestBody PhotoUploadRequest request,
                                                           @LoginMember Member member) {
        PhotoUploadInfo photoUploadInfo = photoService.uploadPhotoList(request, member);
        return ResultResponse.of(UPLOAD_PHOTO, photoUploadInfo);
    }

    @GetMapping
    @Operation(summary = "특정 공유그룹의 특정 앨범 사진 조회 API", description = "특정 공유그룹의 특정 앨범의 사진을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "조회할 공유그룹의 아이디를 입력해주세요."),
            @Parameter(name = "profileId", description = "조회할 프로필의 아이디를 입력해주세요."),
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 사진 개수를 입력해주세요.")
    })
    public ResultResponse<PagedPhotoEsInfo> getPhotoListByShareGroupAndProfile(@RequestParam Long shareGroupId,
                                                                               @RequestParam Long profileId,
                                                                               @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                                               @Parameter(hidden = true) Pageable pageable,
                                                                               @LoginMember Member member) {
        Page<PhotoEs> photoEsList = photoEsService.getPhotoEsListByShareGroupIdAndFaceTag(shareGroupId, profileId, member, pageable);
        return ResultResponse.of(RETRIEVE_PHOTO, photoConverter.toPagedPhotoEsInfo(photoEsList, member));
    }

    @GetMapping("/all")
    @Operation(summary = "특정 공유그룹의 전체 사진 조회 API", description = "특정 공유그룹의 전체 사진을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "조회할 공유그룹의 아이디를 입력해주세요."),
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 사진 개수를 입력해주세요.")
    })
    public ResultResponse<PagedPhotoEsInfo> getAllPhotoListByShareGroup(@RequestParam Long shareGroupId,
                                                                        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                                        @Parameter(hidden = true) Pageable pageable,
                                                                        @LoginMember Member member) {
        Page<PhotoEs> photoEsList = photoEsService.getAllPhotoEsListByShareGroupId(shareGroupId, member, pageable);
        return ResultResponse.of(RETRIEVE_PHOTO, photoConverter.toPagedPhotoEsInfo(photoEsList, member));
    }

    @GetMapping("/etc")
    @Operation(summary = "특정 공유그룹의 아무도 인식되지 않은 사진 조회 API", description = "특정 공유그룹의 아무도 인식되지 않은 사진을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "조회할 공유그룹의 아이디를 입력해주세요."),
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 사진 개수를 입력해주세요.")
    })
    public ResultResponse<PagedPhotoEsInfo> getEtcPhotoListByShareGroup(@RequestParam Long shareGroupId,
                                                                        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                                        @Parameter(hidden = true) Pageable pageable,
                                                                        @LoginMember Member member) {
        Page<PhotoEs> photoEsList = photoEsService.getEtcPhotoEsListByShareGroupId(shareGroupId, member, pageable);
        return ResultResponse.of(RETRIEVE_PHOTO, photoConverter.toPagedPhotoEsInfo(photoEsList, member));
    }

    @GetMapping("/download")
    @Operation(summary = "사진 다운로드 API", description = "여러장의 사진을 다운로드할 주소를 받는  API입니다. 해당 공유그룹에 속해있는 회원만 다운로드 요청할 수 있습니다.")
    public ResultResponse<PhotoDownloadUrlListInfo> getPhotoDownloadUrlList(@RequestParam List<Long> photoIdList,
                                                                            @RequestParam Long shareGroupId,
                                                                            @LoginMember Member member) {
        PhotoDownloadUrlListInfo photoDownloadUrlList = photoService.getPhotoDownloadUrlList(photoIdList, shareGroupId, member);
        return ResultResponse.of(DOWNLOAD_PHOTO, photoDownloadUrlList);
    }

    @DeleteMapping
    @Operation(summary = "사진 삭제 API", description = "사진을 삭제하는 API입니다. 해당 공유그룹에 속해있는 회원만 삭제할 수 있습니다.")
    public ResultResponse<PhotoDeleteInfo> deletePhotoList(@Valid @RequestBody PhotoDeletedRequest request,
                                                           @LoginMember Member member) {
        List<Photo> photoList = photoService.deletePhotoList(request, member);
        return ResultResponse.of(DELETE_PHOTO, photoConverter.toPhotoDeleteInfo(photoList));
    }
}
