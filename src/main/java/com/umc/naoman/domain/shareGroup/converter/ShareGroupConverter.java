package com.umc.naoman.domain.shareGroup.converter;

import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;

import java.time.LocalDateTime;

public class ShareGroupConverter {

    public static ShareGroup toEntity(ShareGroupRequest.createShareGroupRequest request) {
        return ShareGroup.builder()
                .memberCount(request.getMemberNameList().size())  // 변경 가능성 있음. memberCount 대신 nameList의 size 사용
                .build();
    }

    private static final String baseUrl = "https://na0man/invite/"; //baseUrl 상수

    public static ShareGroupResponse.ShareGroupInfo toShareGroupInfoDTO(ShareGroup shareGroup) {
        return ShareGroupResponse.ShareGroupInfo.builder()
                .shareGroupId(shareGroup.getId())
                .name("임시 공유 그룹 이름") //임시 공유 그룹 이름 (고유 코드 출력)
                .inviteUrl(baseUrl + shareGroup.getInviteCode())
                .createdAt(shareGroup.getCreatedAt())
                .build();
    }

}
