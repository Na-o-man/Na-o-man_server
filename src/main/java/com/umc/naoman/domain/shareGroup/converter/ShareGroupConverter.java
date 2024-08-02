package com.umc.naoman.domain.shareGroup.converter;

import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse.PagedShareGroupInfo;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse.ShareGroupDetailInfo;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse.ShareGroupInfo;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShareGroupConverter {
    private static final String BASE_URL = "https://na0man/invite/"; //baseUrl 상수

    public ShareGroup toEntity(ShareGroupRequest.createShareGroupRequest request) {
        return ShareGroup.builder()
                .memberCount(request.getMemberNameList().size())  // 변경 가능성 있음. memberCount 대신 nameList의 size 사용
                .name("임시 공유 그룹 이름") //임시 공유 그룹 이름
                .build();
    }

    // 그룹 생성 시 반환하는 정보 DTO
    public ShareGroupInfo toShareGroupInfo(ShareGroup shareGroup) {
        return ShareGroupInfo.builder()
                .shareGroupId(shareGroup.getId())
                .name(shareGroup.getName())
                .image(shareGroup.getImage())
                .memberCount(shareGroup.getMemberCount())
                .inviteUrl(BASE_URL + shareGroup.getInviteCode())
                .createdAt(shareGroup.getCreatedAt())
                .build();
    }

    // 그룹 삭제 시 반환하는 정보 DTO
    public ShareGroupResponse.ShareGroupId toDeleteShareGroupInfo(Long shareGroupId) {
        return ShareGroupResponse.ShareGroupId.builder()
                .shareGroupId(shareGroupId)
                .build();
    }

    // 그룹 Id만 반환 (그룹 참여 시 반환하는 DTO)
    public ShareGroupResponse.ShareGroupId toJoinShareGroupInfo(ShareGroup shareGroup) {
        return ShareGroupResponse.ShareGroupId.builder()
                .shareGroupId(shareGroup.getId())
                .build();
    }

    // 조회 시, 디테일한 그룹 정보를 반환하는 DTO
    public ShareGroupDetailInfo toShareGroupDetailInfo(ShareGroup shareGroup, List<Profile> profiles) {
        List<ShareGroupResponse.ProfileInfo> profileInfoList = profiles.stream()
                .map(this::toProfileInfo)
                .toList();

        return ShareGroupDetailInfo.builder()
                .shareGroupId(shareGroup.getId())
                .name(shareGroup.getName()) //임시 공유 그룹 이름 (고유 코드 출력)
                .image(shareGroup.getImage())
                .memberCount(shareGroup.getMemberCount())
                .inviteUrl(BASE_URL + shareGroup.getInviteCode())
                .profileInfoList(profileInfoList) //프로필 리스트 출력
                .createdAt(shareGroup.getCreatedAt())
                .build();
    }

    // profile 정보 반환 DTO
    public ShareGroupResponse.ProfileInfo toProfileInfo(Profile profile) {
        return ShareGroupResponse.ProfileInfo.builder()
                .profileId(profile.getId())
                .name(profile.getName())
                .image(profile.getImage())
                .memberId(profile.getMember() != null ? profile.getMember().getId() : null)
                .build();
    }

    // 공유 그룹 목록 반환 DTO
    public PagedShareGroupInfo toPagedShareGroupInfo(Page<ShareGroup> shareGroupList) {
        // 각 공유 그룹에 대한 상세 정보를 가져오기 (DetailInfo response 재사용)
        List<ShareGroupInfo> shareGroupInfoList = shareGroupList
                .stream()
                .map(this::toShareGroupInfo)
                .toList();

        return PagedShareGroupInfo.builder()
                .shareGroupInfoList(shareGroupInfoList) // 만든 info 리스트
                .page(shareGroupList.getNumber())
                .totalElements(shareGroupList.getTotalElements())
                .isFirst(shareGroupList.isFirst())
                .isLast(shareGroupList.isLast())
                .build();
    }


}
