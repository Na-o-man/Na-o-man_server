package com.umc.naoman.domain.photo.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
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
import com.umc.naoman.global.error.code.S3ErrorCode;
import io.awspring.cloud.s3.S3Template;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.umc.naoman.global.error.code.S3ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private static final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);
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
        shareGroupService.findProfile(request.getShareGroupId(), member.getId());

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
    public PhotoResponse.PhotoUploadInfo uploadPhotoList(PhotoRequest.PhotoUploadRequest request, Member member) {
        shareGroupService.findProfile(request.getShareGroupId(), member.getId());
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
        shareGroupService.findProfile(shareGroup.getId(), member.getId());

        return photoRepository.findAllByShareGroupId(shareGroup.getId(), pageable);
    }

    @Override
    @Transactional
    public List<Photo> deletePhotoList(PhotoRequest.PhotoDeletedRequest request, Member member) {
        // 멤버가 해당 공유 그룹에 대한 권한이 있는지 확인
        shareGroupService.findProfile(request.getShareGroupId(), member.getId());

        // 요청된 사진 ID 목록과 공유 그룹 ID를 기반으로 사진 목록 조회
        List<Photo> photoList = photoRepository.findByIdInAndShareGroupId(request.getPhotoIdList(), request.getShareGroupId());

        // 사진 목록 크기 검증
        if (photoList.size() != request.getPhotoIdList().size()) {
            // 요청한 사진이 일부 또는 전부 없을 경우 예외 발생
            throw new BusinessException(PHOTO_NOT_FOUND);
        }

        // 각 사진에 대해 S3에서 객체 삭제 및 데이터베이스에서 삭제
        for (Photo photo : photoList) {
            deletePhoto(photo);
        }

        return photoList; // 삭제된 사진 목록 반환
    }

    private void deletePhoto(Photo photo) {
        // S3에서 원본 및 변환된 이미지 삭제
        s3Template.deleteObject(bucketName, RAW_PATH_PREFIX + "/" + photo.getName());
        s3Template.deleteObject(bucketName, W200_PATH_PREFIX + "/" + photoConverter.convertExtension(photo.getName()));
        s3Template.deleteObject(bucketName, W400_PATH_PREFIX + "/" + photoConverter.convertExtension(photo.getName()));

        // 데이터베이스에서 사진 삭제
        photoRepository.delete(photo);
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayResource downloadPhoto(Long photoId, Long shareGroupId, Member member) {
        // photoId에 해당하는 사진을 조회, 존재하지 않으면 예외를 던짐
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new BusinessException(PHOTO_NOT_FOUND));

        // S3에서 사진을 가져오기 위해 사진 이름을 키 값으로 사용
        String photoName = photo.getName();

        // S3에서 해당 키 값을 가진 객체를 가져옴
        S3Object s3Object;

        try {
            s3Object = amazonS3.getObject(bucketName + "/" + RAW_PATH_PREFIX, photoName);
        } catch (AmazonS3Exception e) {
            throw new BusinessException(PHOTO_NOT_FOUND);
        }

        // S3 객체의 내용을 읽음
        try (S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent()) {
            return new ByteArrayResource(IOUtils.toByteArray(s3ObjectInputStream));
        } catch (IOException e) {
            throw new BusinessException(FAILED_DOWNLOAD_PHOTO);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getPhotoName(Long photoId) {
        return photoRepository.findById(photoId).orElseThrow(() -> new BusinessException(PHOTO_NOT_FOUND)).getName();
    }

    @Override
    @Transactional(readOnly = true)
    public PhotoResponse.PhotoDownloadUrlListInfo getPhotoDownloadUrlList(List<Long> photoIdList, Long shareGroupId, Member member) {
        shareGroupService.findProfile(shareGroupId, member.getId());
        return photoConverter.toPhotoDownloadUrlListResponse(photoIdList, shareGroupId);
    }
}
