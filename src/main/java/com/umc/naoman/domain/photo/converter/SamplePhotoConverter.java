package com.umc.naoman.domain.photo.converter;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.dto.PhotoResponse;
import com.umc.naoman.domain.photo.entity.SamplePhoto;
import org.springframework.stereotype.Component;

@Component
public class SamplePhotoConverter {
    public SamplePhoto toEntity(String photoUrl, String photoName, Member member) {
        return SamplePhoto.builder()
                .url(photoUrl)
                .name(photoName)
                .member(member)
                .build();
    }

    public PhotoResponse.SamplePhotoUploadInfo toSamplePhotoUploadInfo(Long memberId, int uploadCount) {
        return PhotoResponse.SamplePhotoUploadInfo.builder()
                .memberId(memberId)
                .uploadCount(uploadCount)
                .build();
    }
}
