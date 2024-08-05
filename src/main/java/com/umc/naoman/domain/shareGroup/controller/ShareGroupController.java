package com.umc.naoman.domain.shareGroup.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.converter.ShareGroupConverter;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest.createShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.service.OpenAiService;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.ShareGroupResultCode;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final ShareGroupConverter shareGroupConverter;

    @PostMapping
    @Operation(summary = "공유그룹 생성 API", description = "새로운 공유그룹을 생성하는 API입니다.")
    public ResultResponse<ShareGroupResponse
            .ShareGroupInfo> createShareGroup(@Valid @RequestBody createShareGroupRequest request,
                                              @LoginMember Member member) {
        ShareGroup shareGroup = shareGroupService.createShareGroup(request, member);
        return ResultResponse.of(ShareGroupResultCode.CREATE_SHARE_GROUP, shareGroupConverter.toShareGroupInfo(shareGroup));
    }

    @GetMapping("/{shareGroupId}")
    @Operation(summary = "공유그룹 조회 API", description = "shareGroupId로 특정 공유그룹을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "특정 공유그룹 id를 입력해 주세요.")
    })
    public ResultResponse<ShareGroupResponse.ShareGroupDetailInfo> getShareGroupDetailInfo(@PathVariable(name = "shareGroupId") Long shareGroupId) {
        ShareGroup shareGroup = shareGroupService.findShareGroup(shareGroupId);
        List<Profile> profileList = shareGroupService.findProfileList(shareGroupId);

        return ResultResponse.of(ShareGroupResultCode.SHARE_GROUP_INFO,
                shareGroupConverter.toShareGroupDetailInfo(shareGroup, profileList));
    }

    @GetMapping
    @Operation(summary = "초대 코드로 공유그룹 조회 API", description = "inviteCode로 특정 공유그룹을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "inviteCode", description = "참여하려는 공유그룹의 초대 코드")
    })
    public ResultResponse<ShareGroupResponse.ShareGroupDetailInfo> getShareGroupByInviteCode(@RequestParam String inviteCode) {
        ShareGroup shareGroup = shareGroupService.findShareGroup(inviteCode);
        List<Profile> profileList = shareGroupService.findProfileList(shareGroup.getId());

        return ResultResponse.of(ShareGroupResultCode.SHARE_GROUP_INFO,
                shareGroupConverter.toShareGroupDetailInfo(shareGroup, profileList));
    }

    @GetMapping("/my")
    @Operation(summary = "내가 참여한 공유그룹 목록 조회 API", description = "내가 참여한 공유그룹 목록을 페이징 처리하여 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 공유그룹 개수를 입력해주세요.")
    })
    public ResultResponse<ShareGroupResponse.PagedShareGroupInfo> getMyShareGroupList(@LoginMember Member member,
                                                                                      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                                                                      @Parameter(hidden = true) Pageable pageable) {
        Page<ShareGroup> shareGroupList = shareGroupService.getMyShareGroupList(member, pageable);
        return ResultResponse.of(ShareGroupResultCode.SHARE_GROUP_INFO_LIST,
                shareGroupConverter.toPagedShareGroupInfo(shareGroupList));
    }

    @GetMapping("/{shareGroupId}/invite")
    @Operation(summary = "공유그룹 초대 코드 조회 API", description = "공유그룹의 초대 코드를 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "조회할 공유그룹 id를 입력해 주세요.")
    })
    public ResultResponse<ShareGroupResponse.InviteInfo> getInviteCode(@PathVariable Long shareGroupId,
                                                                       @LoginMember Member member) {
        ShareGroup shareGroup = shareGroupService.getInviteInfo(shareGroupId, member);
        return ResultResponse.of(ShareGroupResultCode.GET_INVITE_CODE,
                shareGroupConverter.toInviteInfo(shareGroup));
    }

    @PostMapping("/join")
    @Operation(summary = "공유그룹 참여 API", description = "특정 공유그룹에 참여하는 API입니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupId> joinShareGroup(@Valid @RequestBody ShareGroupRequest.JoinShareGroupRequest request,
                                                                          @LoginMember Member member) {
        ShareGroup shareGroup = shareGroupService.joinShareGroup(request.getShareGroupId(), request.getProfileId(), member);
        return ResultResponse.of(ShareGroupResultCode.JOIN_SHARE_GROUP,
                shareGroupConverter.toShareGroupId(shareGroup));
    }

    @DeleteMapping("/{shareGroupId}")
    @Operation(summary = "공유그룹 삭제 API", description = "공유그룹을 삭제하는 API입니다. 공유그룹 생성자만 삭제할 수 있습니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "삭제할 공유그룹 id를 입력해 주세요.")
    })
    public ResultResponse<ShareGroupResponse.ShareGroupId> deleteShareGroup(@PathVariable Long shareGroupId,
                                                                            @LoginMember Member member) {
        ShareGroup deletedShareGroup = shareGroupService.deleteShareGroup(shareGroupId, member);
        return ResultResponse.of(ShareGroupResultCode.DELETE_SHARE_GROUP,
                shareGroupConverter.toShareGroupId(deletedShareGroup));
    }

}
