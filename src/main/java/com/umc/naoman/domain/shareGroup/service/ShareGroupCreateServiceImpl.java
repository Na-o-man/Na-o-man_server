package com.umc.naoman.domain.shareGroup.service;

import com.umc.naoman.domain.shareGroup.converter.ShareGroupConverter;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.validation.exception.ShareGroupException;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.Role;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.repository.ProfileRepository;
import com.umc.naoman.domain.shareGroup.repository.ShareGroupRepository;
import com.umc.naoman.global.error.code.ShareGroupErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ShareGroupCreateServiceImpl implements ShareGroupCreateService {

    private final ShareGroupRepository shareGroupRepository;
    private final ProfileRepository profileRepository;

    @Override
    public ShareGroup createShareGroup(ShareGroupRequest.createShareGroupRequest request) {

        validateCreateShareGroupRequest(request);

        // 초대링크를 위한 12자리 고유번호 생성 (UUID)
        String inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();


        // 변환 로직
        ShareGroup newShareGroup = ShareGroupConverter.toShareGroup(request);
        newShareGroup.setInviteCode(generateInviteLink(inviteCode));

        // 생성된 공유 그룹 저장
        ShareGroup savedShareGroup = shareGroupRepository.save(newShareGroup);

        // 첫 번째 멤버에 대한 프로필 생성 (CREATOR 역할)
        String creatorName = request.getMemberNameList().get(0);
        Profile creatorProfile = Profile.builder()
                .name(creatorName)
                .role(Role.CREATOR) // 생성자 역할 설정
                .shareGroup(savedShareGroup)
                .joinedAt(LocalDateTime.now()) // 가입시간: 현재
                .build();
        profileRepository.save(creatorProfile);

        // 나머지 멤버에 대한 프로필 생성 (PARTICIPANT 역할)
        for (int i = 1; i < request.getMemberNameList().size(); i++) {
            String memberName = request.getMemberNameList().get(i);
            Profile profile = Profile.builder()
                    .name(memberName)
                    .role(Role.PARTICIPANT) // 기본 역할 설정
                    .shareGroup(savedShareGroup)
                    .build();
            profileRepository.save(profile); // 만들어진 각 profile 저장
        }

        // 리턴
        return savedShareGroup;
    }

    private String generateInviteLink(String inviteCode) {
        // 앱의 베이스 URL을 설정합니다. 예: "https://discord.cc"
        String baseUrl = "https://na0man/invite/";
        return baseUrl + inviteCode;
    }

    private void validateCreateShareGroupRequest(ShareGroupRequest.createShareGroupRequest request) {
        if (request.getMemberNameList().isEmpty()) {
            throw new ShareGroupException(ShareGroupErrorCode.EMPTY_MEMBER_NAME_LIST);
        }
        if (request.getMeetingTypeList().isEmpty()) {
            throw new ShareGroupException(ShareGroupErrorCode.EMPTY_MEETING_TYPE_LIST);
        }
        if (request.getPlace() == null || request.getPlace().trim().isEmpty()) {
            throw new ShareGroupException(ShareGroupErrorCode.NULL_PLACE);
        }
        if (request.getMemberNameList().size() != request.getMemberCount()) {
            throw new ShareGroupException(ShareGroupErrorCode.MEMBER_COUNT_MISMATCH);
        }
    }
}
