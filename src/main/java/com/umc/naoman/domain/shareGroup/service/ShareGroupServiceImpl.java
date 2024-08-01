package com.umc.naoman.domain.shareGroup.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.repository.MemberRepository;
import com.umc.naoman.domain.shareGroup.converter.ShareGroupConverter;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse.PagedShareGroupInfo;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.Role;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.repository.ProfileRepository;
import com.umc.naoman.domain.shareGroup.repository.ShareGroupRepository;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.ShareGroupErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShareGroupServiceImpl implements ShareGroupService {

    private final ShareGroupRepository shareGroupRepository;
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final ShareGroupConverter shareGroupConverter;

    @Transactional
    @Override
    public ShareGroup createShareGroup(ShareGroupRequest.createShareGroupRequest request, Member member) {
        // 초대링크를 위한 고유번호 생성 (UUID)
        String inviteCode = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        // 변환 로직
        ShareGroup newShareGroup = shareGroupConverter.toEntity(request);
        newShareGroup.setInviteCode(inviteCode);

        // 생성된 공유 그룹 저장
        ShareGroup savedShareGroup = shareGroupRepository.save(newShareGroup);

        // 모든  멤버에 대한 프로필 생성 (PARTICIPANT 역할)
        for (int i = 0; i < request.getMemberNameList().size(); i++) {
            String memberName = request.getMemberNameList().get(i);
            Role role = (i == 0) ? Role.CREATOR : Role.PARTICIPANT; // 첫 번째 멤버는 CREATOR, 나머지는 PARTICIPANT

            Profile profile = Profile.builder()
                    .name(memberName)
                    .role(role) // 역할 설정
                    .shareGroup(savedShareGroup)
                    .joinedAt(i == 0 ? LocalDateTime.now() : null) // 첫 번째 멤버만 joinedAt 설정
                    .member(i == 0 ? member : null) // 첫 번째 멤버에만 현재 사용자의 member 설정
                    .build();
            profileRepository.save(profile); // 만들어진 각 profile 저장
        }

        // 리턴
        return savedShareGroup;
    }

    @Transactional
    @Override
    public ShareGroup joinShareGroup(Long shareGroupId, Long profileId, Member member) {

        ShareGroup shareGroup = findShareGroup(shareGroupId);

        //repo에서 Profile 객체 꺼내오기
        Profile profile = findProfile(profileId);

        //공유그룹에 해당 profile이 존재하지 않으면
        if (!profile.getShareGroup().getId().equals(shareGroupId)) {
            throw new BusinessException(ShareGroupErrorCode.INVALID_PROFILE_FOR_GROUP);
        }

        //해당 멤버(본인)을 선택한 profile에 세팅, 저장
        profile.setInfo(member);
        profileRepository.save(profile);

        return shareGroup;
    }

    @Override
    public Page<ShareGroup> getMyShareGroupList(Member member, Pageable pageable) {
        // 멤버를 통해 profile을 가져와서, 해당 profile의 shareGroupId를 추출
        List<Long> shareGroupIdList = profileRepository.findByMemberId(member.getId())
                .stream()
                .map(profile -> profile.getShareGroup().getId())
                .collect(Collectors.toList()); // 리스트로 수집

        // 추출한 공유 그룹 ID 리스트를 통해 해당 공유 그룹들을 페이징 처리하여 가져오기
        return shareGroupRepository.findByIdIn(shareGroupIdList, pageable);
    }

    @Override
    public ShareGroup findShareGroup(Long shareGroupId) {
        return shareGroupRepository.findById(shareGroupId)
                .orElseThrow(() -> new BusinessException(ShareGroupErrorCode.SHARE_GROUP_NOT_FOUND));
    }

    @Override
    public ShareGroup findShareGroup(String inviteCode) {
        return shareGroupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new BusinessException(ShareGroupErrorCode.SHARE_GROUP_NOT_FOUND));
    }

    @Override
    public List<Profile> findProfileList(Long shareGroupId) {
        return profileRepository.findByShareGroupId(shareGroupId);
    }

    @Override
    public Profile findProfile(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ShareGroupErrorCode.PROFILE_NOT_FOUND));
    }

}