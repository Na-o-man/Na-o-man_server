package com.umc.naoman.domain.shareGroup.controller;

import com.umc.naoman.domain.shareGroup.converter.ShareGroupConverter;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.ShareGroupResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/shareGroups")
public class ShareGroupController {

    private final ShareGroupService shareGroupService;

    @PostMapping
    public ResultResponse<ShareGroupResponse
            .ShareGroupInfo> createShareGroup(@RequestBody ShareGroupRequest.createShareGroupRequest request) {

        ShareGroup shareGroup = shareGroupService.createShareGroup(request);
        return ResultResponse.of(ShareGroupResultCode.CREATE_SHARE_GROUP, ShareGroupConverter.toShareGroupInfoDTO(shareGroup));
    }

}
