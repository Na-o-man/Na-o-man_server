package com.umc.naoman.domain.shareGroup.converter;

import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ShareGroupConverter {

    public static ShareGroup toEntity(ShareGroupRequest.createShareGroupRequest request) {
        return ShareGroup.builder()
                .memberCount(request.getMemberNameList().size())  // 변경 가능성 있음. memberCount 대신 nameList의 size 사용
                .build();
    }

    private static final String baseUrl = "https://na0man/invite/"; //baseUrl 상수

    // 그룹 생성 시 반환하는 정보 DTO
    public static ShareGroupResponse.ShareGroupInfo toShareGroupInfoDTO(ShareGroup shareGroup) {
        return ShareGroupResponse.ShareGroupInfo.builder()
                .shareGroupId(shareGroup.getId())
                .name("임시 공유 그룹 이름") //임시 공유 그룹 이름 (고유 코드 출력)
                .inviteUrl(baseUrl + shareGroup.getInviteCode())
                .createdAt(shareGroup.getCreatedAt())
                .build();
    }

    // 조회 시, 디테일한 그룹 정보를 반환하는 DTO
    public static ShareGroupResponse.ShareGroupInfo toShareGroupDetailInfoDTO(ShareGroup shareGroup, List<Profile> profiles) {
        List<ShareGroupResponse.ProfileInfo> profileInfoList = profiles.stream()
                .map(ShareGroupConverter::toProfileInfo)
                .toList();

        return ShareGroupResponse.ShareGroupInfo.builder()
                .shareGroupId(shareGroup.getId())
                .name("임시 공유 그룹 이름") //임시 공유 그룹 이름 (고유 코드 출력)
                .image(shareGroup.getImage())
                .memberCount(shareGroup.getMemberCount())
                .profileInfoList(profileInfoList) //프로필 리스트 출력
                .createdAt(shareGroup.getCreatedAt())
                .build();
    }

    public static ShareGroupResponse.ProfileInfo toProfileInfo(Profile profile) {
        return ShareGroupResponse.ProfileInfo.builder()
                .profileId(profile.getId())
                .name(profile.getName())
                .image(profile.getImage())
                .memberId(profile.getMember() != null ? profile.getMember().getId() : null)
                .build();
    }

}
