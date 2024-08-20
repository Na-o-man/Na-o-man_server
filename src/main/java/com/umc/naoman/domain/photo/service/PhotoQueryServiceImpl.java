package com.umc.naoman.domain.photo.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.photo.repository.PhotoRepository;
import com.umc.naoman.domain.photo.repository.SamplePhotoRepository;
import com.umc.naoman.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.umc.naoman.global.error.code.S3ErrorCode.PHOTO_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoQueryServiceImpl implements PhotoQueryService {
    private final PhotoRepository photoRepository;
    private final SamplePhotoRepository samplePhotoRepository;

    @Override
    public Photo findPhoto(Long photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessException(PHOTO_NOT_FOUND));
    }

    @Override
    public boolean hasSamplePhoto(Member member) {
        return samplePhotoRepository.existsByMemberId(member.getId());
    }
}
