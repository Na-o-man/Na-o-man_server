package com.umc.naoman.domain.photo.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.umc.naoman.domain.photo.converter.PhotoConverter;
import com.umc.naoman.domain.photo.dto.PhotoRequest;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.repository.PhotoRepository;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.repository.ShareGroupRepository;
import com.umc.naoman.global.error.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.umc.naoman.global.error.code.S3ErrorCode.PHOTO_NOT_FOUND_S3;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final AmazonS3 amazonS3;
    private final PhotoRepository photoRepository;
    private final ShareGroupRepository shareGroupRepository;
    private final PhotoConverter photoConverter;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    private static final String RAW_PATH_PREFIX = "raw";

    @Override
    @Transactional
    public List<PhotoResponse.PreSignedUrlInfo> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request) {
        return request.getPhotoNameList().stream()
                .map(this::getPreSignedUrl)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PhotoResponse.PhotoUploadInfo uploadPhotoList(PhotoRequest.PhotoUploadRequest request) {

        // 공유 그룹 오류 코드 생기면 추후에 수정
        ShareGroup shareGroup = shareGroupRepository.findById(request.getShareGroupId()).orElseThrow(EntityNotFoundException::new);
        int uploadCount = 0;

        for (int i = 0; i < request.getPhotoUrlList().size(); i++) {
            String photoUrl = request.getPhotoUrlList().get(i);
            String photoName = extractPhotoNameFromUrl(photoUrl);

            // S3에서 객체의 존재를 확인
            S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName + "/" + RAW_PATH_PREFIX, photoName));

            // 객체가 존재한다면 DB에 저장
            if (s3Object != null) {
                Photo photo = photoConverter.toPhoto(photoUrl, photoName, shareGroup);
                photoRepository.save(photo);
                uploadCount++;
            } else {
                throw new BusinessException(PHOTO_NOT_FOUND_S3);
            }
        }

        return new PhotoResponse.PhotoUploadInfo(shareGroup.getId(), uploadCount);
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

    // 사진 URL에서 사진 이름을 추출하는 메서드
    private String extractPhotoNameFromUrl(String photoUrl) {
        int lastSlashIndex = photoUrl.lastIndexOf('/');
        return photoUrl.substring(lastSlashIndex + 1);
    }
}
