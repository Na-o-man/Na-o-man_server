package com.umc.naoman.domain.photo.converter;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PagedPhotoInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoDeleteInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoDownloadUrlListInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PhotoUploadInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PreSignedUrlInfo;
import com.umc.naoman.domain.photo.dto.PhotoResponse.PreSignedUrlListInfo;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.umc.naoman.domain.photo.service.PhotoServiceImpl.*;

@Component
public class PhotoConverter {
    public Photo toEntity(String photoUrl, String photoName, ShareGroup shareGroup) {
        return Photo.builder()
                .url(photoUrl)
                .name(photoName)
                .shareGroup(shareGroup)
                .build();
    }

    public PreSignedUrlListInfo toPreSignedUrlListInfo(List<PreSignedUrlInfo> preSignedUrlList) {
        List<PreSignedUrlInfo> preSignedUrlInfoList = preSignedUrlList.stream()
                .map(preSignedUrlInfo -> toPreSignedUrlInfo(
                        preSignedUrlInfo.getPreSignedUrl(),
                        preSignedUrlInfo.getPhotoUrl(),
                        preSignedUrlInfo.getPhotoName()
                ))
                .collect(Collectors.toList());

        return PreSignedUrlListInfo.builder()
                .preSignedUrlInfoList(preSignedUrlInfoList)
                .build();
    }

    public PreSignedUrlInfo toPreSignedUrlInfo(String preSignedUrl, String photoUrl, String photoName) {
        return PreSignedUrlInfo.builder()
                .preSignedUrl(preSignedUrl)
                .photoUrl(photoUrl)
                .photoName(photoName)
                .build();
    }

    public PhotoUploadInfo toPhotoUploadInfo(Long shareGroupId, int uploadCount) {
        return PhotoUploadInfo.builder()
                .shareGroupId(shareGroupId)
                .uploadCount(uploadCount)
                .build();
    }

    public PagedPhotoInfo toPagedPhotoInfo(Page<PhotoEs> photoEsList, Member member) {
        List<PhotoInfo> photoInfoList = photoEsList.stream()
                .map(photoEs -> toPhotoInfo(photoEs, member))
                .collect(Collectors.toList());

        return PagedPhotoInfo.builder()
                .isLast(photoEsList.isLast())
                .isFirst(photoEsList.isFirst())
                .totalPages(photoEsList.getTotalPages())
                .totalElements(photoEsList.getTotalElements())
                .photoInfoList(photoInfoList)
                .build();
    }

    public PhotoInfo toPhotoInfo(PhotoEs photoEs, Member member) {
        String rawUrl = photoEs.getUrl();
        Boolean isDownload = !photoEs.getDownloadTag().isEmpty() && photoEs.getDownloadTag().contains(member.getId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdAt = LocalDateTime.parse(photoEs.getCreatedAt(), dateTimeFormatter);

        return PhotoInfo.builder()
                .photoId(photoEs.getRdsId())
                .rawPhotoUrl(rawUrl)
                .w200PhotoUrl(createResizedPhotoUrl(rawUrl, W200_PATH_PREFIX))
                .w400PhotoUrl(createResizedPhotoUrl(rawUrl, W400_PATH_PREFIX))
                .isDownload(isDownload)
                .createdAt(createdAt)
                .build();
    }

    private String createResizedPhotoUrl(String photoUrl, String size) {
        String resizedUrl = getResizedUrl(photoUrl, size);
        return convertExtension(resizedUrl);
    }

    // 리사이즈된 사진의 URL로 변환하는 메서드
    private String getResizedUrl(String photoUrl, String size) {
        return photoUrl.replace("/" + RAW_PATH_PREFIX + "/", "/" + size + "/");
    }

    // 확장자 변환 메서드
    public String convertExtension(String photoUrl) {
        return photoUrl.replace(".HEIC", ".jpg");
    }

    public PhotoDeleteInfo toPhotoDeleteInfo(List<Photo> photoList) {
        List<Long> photoIdList = photoList.stream()
                .map(Photo::getId)
                .collect(Collectors.toList());

        return PhotoDeleteInfo.builder()
                .photoIdList(photoIdList)
                .build();
    }

    public PhotoDownloadUrlListInfo toPhotoDownloadUrlListInfo(List<String> photoUrlList) {
        return PhotoDownloadUrlListInfo.builder()
                .photoDownloadUrlList(photoUrlList)
                .build();
    }
}
