package com.umc.naoman.domain.photo.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.converter.PhotoConverter;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.elasticsearch.repository.PhotoEsClientRepository;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.repository.PhotoRepository;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.error.BusinessException;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.umc.naoman.global.error.code.S3ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final AmazonS3 amazonS3;
    private final S3Template s3Template;
    private final PhotoRepository photoRepository;
    private final ShareGroupService shareGroupService;
    private final PhotoEsClientRepository photoEsClientRepository;
    private final PhotoConverter photoConverter;
    private final FaceDetectionService faceDetectionService;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public static final String RAW_PATH_PREFIX = "raw";
    public static final String W200_PATH_PREFIX = "w200";
    public static final String W400_PATH_PREFIX = "w400";

    @Override
    public Photo findPhoto(Long photoId) {
        return photoRepository.findById(photoId).orElseThrow(() -> new BusinessException(PHOTO_NOT_FOUND));
    }

    @Override
    @Transactional
    public List<PhotoResponse.PreSignedUrlInfo> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request, Member member) {
        validateShareGroupAndProfile(request.getShareGroupId(), member);

        return request.getPhotoNameList().stream()
                .map(this::getPreSignedUrl)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PhotoResponse.PhotoUploadInfo uploadPhotoList(PhotoRequest.PhotoUploadRequest request, Member member) {

        validateShareGroupAndProfile(request.getShareGroupId(), member);
        ShareGroup shareGroup = shareGroupService.findShareGroup(request.getShareGroupId());

        // 사진 URL 리스트를 기반으로 사진 엔티티를 생성하고 DB에 저장
        List<Photo> photoList = request.getPhotoUrlList().stream()
                .map(photoUrl -> checkAndSavePhotoInDB(photoUrl, extractPhotoNameFromUrl(photoUrl), shareGroup))
                .toList();

        // Elasticsearch 벌크 저장
        photoEsClientRepository.savePhotoBulk(photoList);

        // 사진 이름 리스트 생성
        List<String> photoNameList = photoList.stream()
                .map(Photo::getName)
                .toList();

        // 얼굴 인식 서비스 호출
        faceDetectionService.detectFaceUploadPhoto(photoNameList, request.getShareGroupId());

        return new PhotoResponse.PhotoUploadInfo(request.getShareGroupId(), photoList.size());
    }

    @Override
    @Transactional
    public List<Photo> deletePhotoList(PhotoRequest.PhotoDeletedRequest request, Member member) {
        validateShareGroupAndProfile(request.getShareGroupId(), member);
        // 요청된 사진 ID 목록과 공유 그룹 ID를 기반으로 사진 목록 조회
        List<Photo> photoList = photoRepository.findByIdInAndShareGroupId(request.getPhotoIdList(), request.getShareGroupId());

        // 사진 목록 크기 검증
        if (photoList.size() != request.getPhotoIdList().size()) {
            // 요청한 사진이 일부 또는 전부 없을 경우 예외 발생
            throw new BusinessException(PHOTO_NOT_FOUND);
        }

        // 각 사진에 대해 S3에서 객체 삭제 및 데이터베이스에서 삭제
        for (Photo photo : photoList) {
            deletePhoto(photo.getName());
        }

        // ID 목록을 추출하여 삭제 쿼리 실행
        List<Long> photoIdList = photoList.stream()
                .map(Photo::getId)
                .toList();

        photoRepository.deleteAllByPhotoIdList(photoIdList);

        return photoList; // 삭제된 사진 목록 반환
    }

    @Override
    public PhotoResponse.PhotoDownloadUrlListInfo getPhotoDownloadUrlList(List<Long> photoIdList, Long shareGroupId, Member member) {
        validateShareGroupAndProfile(shareGroupId, member);
        List<Photo> photoList = photoRepository.findByIdIn(photoIdList);

        if (photoList.size() != photoIdList.size()) {
            // 요청한 사진이 일부 또는 전부 없을 경우 예외 발생
            throw new BusinessException(PHOTO_NOT_FOUND);
        }

        return photoConverter.toPhotoDownloadUrlListInfo(photoList);
    }

    // 해당 공유 그룹이 존재하는지 확인 & 멤버가 해당 공유 그룹에 속해있는지 확인
    private void validateShareGroupAndProfile(Long shareGroupId, Member member) {
        shareGroupService.findShareGroup(shareGroupId);
        shareGroupService.findProfile(shareGroupId, member.getId());
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

    // 원본 사진 전체 경로 생성
    private String createPath(String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s", RAW_PATH_PREFIX, fileId + fileName);
    }

    // 사진 고유 ID 생성
    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    // 원본 사진의 접근 URL 생성
    private String generateFileAccessUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    // PreSigned URL 유효 기간 설정
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 3;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    // 사진 URL에서 사진 이름을 추출하는 메서드
    private String extractPhotoNameFromUrl(String photoUrl) {
        int lastSlashIndex = photoUrl.lastIndexOf('/');
        return photoUrl.substring(lastSlashIndex + 1);
    }

    // S3에 객체의 존재 여부 확인 및 DB에 사진을 저장하고 객체를 반환하는 메서드
    private Photo checkAndSavePhotoInDB(String photoUrl, String photoName, ShareGroup shareGroup) {
        if (!amazonS3.doesObjectExist(bucketName, RAW_PATH_PREFIX + "/" + photoName)) {
            throw new BusinessException(PHOTO_NOT_FOUND_S3);
        }

        Photo photo = photoConverter.toEntity(photoUrl, photoName, shareGroup);
        return photoRepository.save(photo); // 저장된 Photo 객체 반환
    }

    private void deletePhoto(String photoName) {
        // S3에서 원본 및 변환된 이미지 삭제
        s3Template.deleteObject(bucketName, RAW_PATH_PREFIX + "/" + photoName);
        s3Template.deleteObject(bucketName, W200_PATH_PREFIX + "/" + photoConverter.convertExtension(photoName));
        s3Template.deleteObject(bucketName, W400_PATH_PREFIX + "/" + photoConverter.convertExtension(photoName));
    }
}
