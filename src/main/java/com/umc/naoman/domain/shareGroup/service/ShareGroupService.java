package com.umc.naoman.domain.shareGroup.service;

import com.umc.naoman.domain.shareGroup.dto.ShareGroupRequest;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;

public interface ShareGroupService {
    ShareGroup createShareGroup(ShareGroupRequest.createShareGroupRequest request);
}
