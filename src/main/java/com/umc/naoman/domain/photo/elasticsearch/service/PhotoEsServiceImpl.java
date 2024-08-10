package com.umc.naoman.domain.photo.elasticsearch.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import com.umc.naoman.domain.photo.elasticsearch.repository.PhotoEsClientRepository;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoEsServiceImpl implements PhotoEsService {

    private final PhotoEsClientRepository photoEsClientRepository;
    private final ShareGroupService shareGroupService;

    @Override
    @Transactional(readOnly = true)
    public Page<PhotoEs> getPhotoEsListByShareGroupId(Long shareGroupId, Member member, Pageable pageable) {
        validateShareGroupAndProfile(shareGroupId, member);
        return photoEsClientRepository.findPhotoEsByShareGroupId(shareGroupId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhotoEs> getPhotoEsListByShareGroupIdAndFaceTag(Long shareGroupId, Long faceTag, Member member, Pageable pageable) throws IOException {
        validateShareGroupAndProfile(shareGroupId, member);
        return photoEsClientRepository.findPhotoEsByShareGroupIdAndFaceTag(shareGroupId, faceTag, pageable);
    }

    private void validateShareGroupAndProfile(Long shareGroupId, Member member) {
        // 해당 공유 그룹이 존재하는지 확인
        shareGroupService.findShareGroup(shareGroupId);
        // 멤버가 해당 공유 그룹에 속해있는지 확인
        shareGroupService.findProfile(shareGroupId, member.getId());
    }
}
