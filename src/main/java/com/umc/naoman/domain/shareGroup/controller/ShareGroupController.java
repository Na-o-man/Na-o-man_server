package com.umc.naoman.domain.shareGroup.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.converter.ShareGroupConverter;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.ShareGroupResultCode;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shareGroups")
public class ShareGroupController {

    private final ShareGroupService shareGroupService;

    @PostMapping
    @Operation(summary = "공유그룹 생성 API", description = "새로운 공유그룹을 생성하는 API입니다.")
    public ResultResponse<ShareGroupResponse
            .ShareGroupInfo> createShareGroup(@Valid @RequestBody ShareGroupRequest.createShareGroupRequest request,
                                              @LoginMember Member member) {

        ShareGroup shareGroup = shareGroupService.createShareGroup(request, member);
        return ResultResponse.of(ShareGroupResultCode.CREATE_SHARE_GROUP, ShareGroupConverter.toShareGroupInfoDTO(shareGroup));
    }

    @GetMapping("/{shareGroupId}")
    @Operation(summary = "공유그룹 조회 API", description = "shareGroupId로 특정 공유그룹을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "특정 공유그룹 id를 입력해 주세요.")
    })
    public ResultResponse<ShareGroupResponse.ShareGroupDetailInfo> getShareGroupInfo(@PathVariable(name = "shareGroupId") Long shareGroupId) {

        ShareGroup shareGroup = shareGroupService.findShareGroup(shareGroupId);
        List<Profile> profileList = shareGroupService.findProfileList(shareGroupId);

        return ResultResponse.of(ShareGroupResultCode.SHARE_GROUP_INFO,
                ShareGroupConverter.toShareGroupDetailInfoDTO(shareGroup, profileList));
    }

    @GetMapping
    @Operation(summary = "초대 코드로 공유그룹 조회 API", description = "inviteCode로 특정 공유그룹을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "inviteCode", description = "참여하려는 공유그룹의 초대 코드")
    })
    public ResultResponse<ShareGroupResponse.ShareGroupDetailInfo> getShareGroupByInviteCode(@RequestParam String inviteCode) {

        ShareGroup shareGroup = shareGroupService.findShareGroupByInviteCode(inviteCode);
        List<Profile> profileList = shareGroupService.findProfileList(shareGroup.getId());

        return ResultResponse.of(ShareGroupResultCode.SHARE_GROUP_INFO,
                ShareGroupConverter.toShareGroupDetailInfoDTO(shareGroup, profileList));
    }

    @PostMapping("/join")
    @Operation(summary = "공유그룹 참여 API", description = "특정 공유그룹에 참여하는 API입니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupId> joinShareGroup(@Valid @RequestBody ShareGroupRequest.JoinShareGroupRequest request,
                                                                                @LoginMember Member member) {
        ShareGroup shareGroup = shareGroupService.joinShareGroup(request.getShareGroupId(), request.getProfileId(), member);
        return ResultResponse.of(ShareGroupResultCode.JOIN_SHARE_GROUP,
                ShareGroupConverter.toJoinShareGroupInfoDTO(shareGroup));
    }

}
