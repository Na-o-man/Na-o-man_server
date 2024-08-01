package com.umc.naoman.domain.photo.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.converter.PhotoConverter;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.repository.PhotoRepository;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.repository.ProfileRepository;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.error.BusinessException;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.umc.naoman.global.error.code.S3ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final AmazonS3 amazonS3;
    private final S3Template s3Template;
    private final PhotoRepository photoRepository;
    private final ShareGroupService shareGroupService;
    private final PhotoConverter photoConverter;
    private final ProfileRepository profileRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public static final String RAW_PATH_PREFIX = "raw";
    public static final String W200_PATH_PREFIX = "w200";
    public static final String W400_PATH_PREFIX = "w400";

    @Override
    @Transactional
    public List<PhotoResponse.PreSignedUrlInfo> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request, Member member) {
        if (profileRepository.findByShareGroupIdAndMemberId(request.getShareGroupId(), member.getId()) == null) {
            throw new BusinessException(UNAUTHORIZED_UPLOAD);
        }

        return request.getPhotoNameList().stream()
                .map(this::getPreSignedUrl)
                .collect(Collectors.toList());
    }

    private PhotoResponse.PreSignedUrlInfo getPreSignedUrl(String originalFilename) {
        String fileName = createPath(originalFilename);

        String photoName = fileName.split("/")[1];
        String photoUrl = generateFileAccessUrl(fileName);

        URL preSignedUrl = amazonS3.generatePresignedUrl(getGeneratePreSignedUrlRequest(bucketName, fileName));
        return photoConverter.toPreSignedUrlInfo(preSignedUrl.toString(), photoUrl, photoName);
    }

    // 사진 업로드용(PUT) PreSigned URL 생성
    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return generatePresignedUrlRequest;
    }

    // PreSigned URL 유효 기간 설정
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 3;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    // 사진 고유 ID 생성
    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    // 원본 사진 전체 경로 생성
    private String createPath(String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s", RAW_PATH_PREFIX, fileId + fileName);
    }

    // 원본 사진의 접근 URL 생성
    private String generateFileAccessUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    @Override
    @Transactional
    public PhotoResponse.PhotoUploadInfo uploadPhotoList(PhotoRequest.PhotoUploadRequest request) {
        ShareGroup shareGroup = shareGroupService.findShareGroup(request.getShareGroupId());
        int uploadCount = 0;

        for (String photoUrl : request.getPhotoUrlList()) {
            String photoName = extractPhotoNameFromUrl(photoUrl);
            if (checkAndSavePhoto(photoUrl, photoName, shareGroup)) {
                uploadCount++;
            }
        }

        return new PhotoResponse.PhotoUploadInfo(shareGroup.getId(), uploadCount);
    }

    // 사진 URL에서 사진 이름을 추출하는 메서드
    private String extractPhotoNameFromUrl(String photoUrl) {
        int lastSlashIndex = photoUrl.lastIndexOf('/');
        return photoUrl.substring(lastSlashIndex + 1);
    }

    // S3에 객체의 존재 여부 확인 및 저장하는 메서드
    private boolean checkAndSavePhoto(String photoUrl, String photoName, ShareGroup shareGroup) {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName + "/" + RAW_PATH_PREFIX, photoName));
        if (s3Object != null) {
            Photo photo = photoConverter.toEntity(photoUrl, photoName, shareGroup);
            photoRepository.save(photo);
            return true;
        } else {
            throw new BusinessException(PHOTO_NOT_FOUND_S3);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Photo> getAllPhotoList(Long shareGroupId, Member member, Pageable pageable) {
        ShareGroup shareGroup = shareGroupService.findShareGroup(shareGroupId);

        if (profileRepository.findByShareGroupIdAndMemberId(shareGroup.getId(), member.getId()) == null) {
            throw new BusinessException(UNAUTHORIZED_GET);
        }

        return photoRepository.findAllByShareGroupId(shareGroup.getId(), pageable);
    }

    @Override
    @Transactional
    public List<Photo> deletePhotoList(PhotoRequest.PhotoDeletedRequest request, Member member) {
        if (profileRepository.findByShareGroupIdAndMemberId(request.getShareGroupId(), member.getId()) == null) {
            throw new BusinessException(UNAUTHORIZED_DELETE);
        }

        List<Long> photoIdList = request.getPhotoIdList();
        List<Photo> deletedPhotoList = new ArrayList<>();

        for (Long photoId : photoIdList) {
            Optional<Photo> photoOptional = photoRepository.findById(photoId);
            if (photoOptional.isPresent()) {
                Photo photo = photoOptional.get();
                deletePhoto(photo);
                deletedPhotoList.add(photo);
            }
        }

        return deletedPhotoList;
    }

    private void deletePhoto(Photo photo) {
        s3Template.deleteObject(bucketName, RAW_PATH_PREFIX+ "/" + photo.getName());
        s3Template.deleteObject(bucketName, W200_PATH_PREFIX+ "/" + photoConverter.convertExtension(photo.getName()));
        s3Template.deleteObject(bucketName, W400_PATH_PREFIX+ "/" + photoConverter.convertExtension(photo.getName()));
        photoRepository.delete(photo);
    }
}
