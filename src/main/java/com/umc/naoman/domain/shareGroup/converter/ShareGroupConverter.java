package com.umc.naoman.domain.shareGroup.converter;

import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;

import java.time.LocalDateTime;

public class ShareGroupConverter {

    public static ShareGroup toShareGroup(ShareGroupRequest.createShareGroupRequest request) {
        return ShareGroup.builder()
                .memberCount(request.getMemberCount())
                .meetingTypeList(request.getMeetingTypeList())
                .memberNameList(request.getMemberNameList())
                .place(request.getPlace())
                .build();
    }

    public static ShareGroupResponse.createShareGroupResponse toCreateShareGroupResponseDTO(ShareGroup shareGroup) {
        return ShareGroupResponse.createShareGroupResponse.builder()
                .shareGroupId(shareGroup.getId())
                .name(shareGroup.getPlace()) //임시 공유 그룹 이름
                .inviteCode(shareGroup.getInviteCode())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
