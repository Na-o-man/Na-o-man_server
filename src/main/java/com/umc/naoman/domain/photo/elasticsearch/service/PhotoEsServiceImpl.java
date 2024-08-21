package com.umc.naoman.domain.photo.elasticsearch.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.photo.elasticsearch.document.PhotoEs;
import com.umc.naoman.domain.photo.elasticsearch.repository.PhotoEsClientRepository;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoEsServiceImpl implements PhotoEsService {

    private final PhotoEsClientRepository photoEsClientRepository;
    private final ShareGroupService shareGroupService;

    @Override
    @Transactional(readOnly = true)
    public Page<PhotoEs> getPhotoEsListByShareGroupIdAndFaceTag(Long shareGroupId, Long profileId, Member member, Pageable pageable) {
        validateShareGroupAndProfile(shareGroupId, member);
        Long memberId = shareGroupService.findProfile(profileId).getMember().getId();
        return photoEsClientRepository.findPhotoEsByShareGroupIdAndFaceTag(shareGroupId, memberId, pageable);
    }

    @Override
    @Transactional
    public Page<PhotoEs> getAllPhotoEsListByShareGroupId(Long shareGroupId, Member member, Pageable pageable) {
        validateShareGroupAndProfile(shareGroupId, member);
        Page<PhotoEs> photoEsList = photoEsClientRepository.findPhotoEsByShareGroupId(shareGroupId, pageable);

        final ShareGroup shareGroup = shareGroupService.findShareGroup(shareGroupId);
        if (shareGroup.getImage() == null) {
            photoEsList.stream()
                    .filter(photoEs -> photoEs.getFaceTag().size() >= shareGroup.getMemberCount())
                    .findFirst()
                    .ifPresent(photoEs -> shareGroup.updateImage(photoEs.getUrl()));
        }

        return photoEsList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhotoEs> getEtcPhotoEsListByShareGroupId(Long shareGroupId, Member member, Pageable pageable) {
        validateShareGroupAndProfile(shareGroupId, member);
        return photoEsClientRepository.findPhotoEsByShareGroupIdAndNotFaceTag(shareGroupId, pageable);
    }

    private void validateShareGroupAndProfile(Long shareGroupId, Member member) {
        // 해당 공유 그룹이 존재하는지 확인
        shareGroupService.findShareGroup(shareGroupId);
        // 멤버가 해당 공유 그룹에 속해있는지 확인
        shareGroupService.findProfile(shareGroupId, member.getId());
    }
}
