package com.umc.naoman.domain.shareGroup.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;

import java.util.List;

public interface ShareGroupService {
    ShareGroup createShareGroup(ShareGroupRequest.createShareGroupRequest request, Member member);
    ShareGroup findShareGroup(Long shareGroupId);
    ShareGroup findShareGroupByInviteCode(String inviteCode);
    List<Profile> findProfileList(Long shareGroupId);
    ShareGroup joinShareGroup(Long shareGroupId, Long profileId, Member member);
    Profile findProfile(Long profileId);
}
