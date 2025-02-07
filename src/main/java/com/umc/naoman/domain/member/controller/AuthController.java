package com.umc.naoman.domain.member.controller;

import com.umc.naoman.domain.member.dto.MemberRequest.LoginRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.MarketingAgreedRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.SignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.entity.SocialType;
import com.umc.naoman.domain.member.service.MemberService;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.umc.naoman.global.error.code.MemberErrorCode.TEMP_MEMBER_INFO_COOKIE_NOT_FOUND;
import static com.umc.naoman.global.result.code.MemberResultCode.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "00. 인증,인가 관련 API", description = "회원의 회원가입 및 로그인 등을 처리하는 API입니다.")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/signup/web")
    @Operation(summary = "회원가입 API(웹)", description = "웹 클라이언트가 사용하는 회원가입 API입니다.")
    @Parameters(value = {
            @Parameter(name = "temp-member-info", description = "리다이렉션 시에 쿠키로 넘겨준 사용자 정보가 담긴 jwt를 입력해 주세요.",
                    hidden = true, in = ParameterIn.COOKIE)
    })
    public ResultResponse<LoginInfo> signup(@CookieValue(value = "temp-member-info", required = false) Cookie tempMemberInfoCookie,
                                            @Valid @RequestBody MarketingAgreedRequest request,
                                            HttpServletResponse response) {
        // 추후에 핸들러 처리로 바꿀까 생각 중
        if (tempMemberInfoCookie == null) {
            throw new BusinessException(TEMP_MEMBER_INFO_COOKIE_NOT_FOUND);
        }
        return ResultResponse.of(SIGNUP, memberService.signup(tempMemberInfoCookie.getValue(), request, response));
    }

    @PostMapping("/signup/android")
    @Operation(summary = "회원가입 API(안드로이드)", description = "안드로이드 클라이언트가 사용하는 회원가입 API입니다.")
    public ResultResponse<LoginInfo> signup(@Valid @RequestBody SignupRequest request) {
        return ResultResponse.of(SIGNUP, memberService.signup(request));
    }

    @PostMapping("/login/android")
    @Operation(summary = "로그인 API", description = "안드로이드 클라이언트가 로그인하는 API입니다.")
    public ResultResponse<LoginInfo> login(@Valid @RequestBody LoginRequest request) {
        return ResultResponse.of(LOGIN, memberService.login(request));
    }

    @PostMapping("/check-registration")
    @Operation(summary = "회원가입 여부 조회 API", description = "authId와 플랫폼명을 통해, 해당 정보와 일치하는 회원의 가입 여부를 조회하는 API입니다.")
    public ResultResponse<CheckMemberRegistration> checkSignup(@Valid @RequestBody LoginRequest request) {
        return ResultResponse.of(CHECK_MEMBER_REGISTRATION, memberService.checkRegistration(request));
    }
}
