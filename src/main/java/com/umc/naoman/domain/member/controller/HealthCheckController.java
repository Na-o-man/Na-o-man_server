package com.umc.naoman.domain.member.controller;

import com.umc.naoman.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.umc.naoman.global.result.code.GlobalResultCode.HEALTH_CHECK;

@RestController
@Tag(name = "헬스 체크 API", description = "서버의 상태 검사 API입니다.")
public class HealthCheckController {
    @GetMapping("/")
    @Operation(summary = "헬스 체크 API", description = "서버가 정상적으로 동작하는지 검사하는 API입니다.")
    public ResultResponse<Object> healthCheck() {
        return ResultResponse.of(HEALTH_CHECK);
    }
}
