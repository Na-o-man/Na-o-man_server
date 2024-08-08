package com.umc.naoman.domain.shareGroup.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShareGroupService {
    ShareGroup createShareGroup(ShareGroupRequest.createShareGroupRequest request, Member member);
    ShareGroup joinShareGroup(Long shareGroupId, Long profileId, Member member);
    ShareGroup getInviteInfo(Long shareGroupId, Member member);
    ShareGroup deleteShareGroup(Long shareGroupId, Member member);
    Page<ShareGroup> getMyShareGroupList(Member member, Pageable pageable);
    ShareGroup findShareGroup(Long shareGroupId);
    ShareGroup findShareGroup(String inviteCode);
    List<Profile> findProfileListByShareGroupId(Long shareGroupId);
    List<Profile> findProfileListByMemberId(Long memberId);
    Profile findProfile(Long profileId);
    Profile findProfile(Long shareGroupId, Long memberId);
    boolean doesProfileExist(Long shareGroupId, Long memberId);
}
