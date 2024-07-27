package com.umc.naoman.domain.shareGroup.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

public abstract class ShareGroupRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createShareGroupRequest extends ShareGroupRequest {
        @NotNull(message = "멤버 수를 입력해야 합니다.")
        Integer memberCount;
        @NotEmpty(message = "멤버 이름 리스트는 비어 있을 수 없습니다.")
        List<String> memberNameList;
        @NotEmpty(message = "모임의 성격을 하나 이상 선택해야 합니다.")
        List<String> meetingTypeList;
        @NotNull(message = "모임 장소를 입력해야 합니다.")
        String place;
    }
}
