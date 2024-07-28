package com.umc.naoman.domain.member.controller;

import com.umc.naoman.domain.member.dto.MemberRequest.AndroidLoginRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.AndroidSignupRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.WebSignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.service.MemberService;
import com.umc.naoman.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.umc.naoman.global.result.code.MemberResultCode.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증,인가 관련 API", description = "회원의 회원가입 및 로그인 등을 처리하는 API입니다.")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    @GetMapping("/check-registration")
    @Operation(summary = "회원가입 여부 조회 API", description = "이메일을 통해, 해당 이메일을 가진 회원의 가입 여부를 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "email", description = "회원가입 여부를 확인할 이메일을 입력해 주세요.")
    })
    public ResultResponse<CheckMemberRegistration> checkSignup(@RequestParam("email") @Valid @Email String email) {
        return ResultResponse.of(CHECK_MEMBER_REGISTRATION, memberService.checkRegistration(email));
    }

    @PostMapping("/signup/android")
    @Operation(summary = "회원가입 API", description = "안드로이드 클라이언트가 사용하는 회원가입 API입니다.")
    public ResultResponse<LoginInfo> signup(@Valid @RequestBody AndroidSignupRequest request) {
        return ResultResponse.of(SIGNUP, memberService.signup(request));
    }

    @PostMapping("/signup/web")
    @Operation(summary = "회원가입 요청 API", description = "웹 클라이언트가 사용하는 회원가입 요청 API입니다.")
    @Parameters(value = {
            @Parameter(name = "temp-member-info", description = "리다이렉션 시에 쿠키로 넘겨준 사용자 정보가 담긴 jwt를 헤더로 넘겨주세요.",
                    in = ParameterIn.HEADER)
    })
    public ResultResponse<LoginInfo> signup(@RequestHeader("temp-member-info") String tempMemberInfo,
                                                 @Valid @RequestBody WebSignupRequest request) {
        return ResultResponse.of(SIGNUP, memberService.signup(tempMemberInfo, request));
    }

    @PostMapping("/login/android")
    @Operation(summary = "로그인 API", description = "안드로이드 클라이언트가 로그인하는 API입니다.")
    public ResultResponse<LoginInfo> login(@Valid @RequestBody AndroidLoginRequest request) {
        return ResultResponse.of(LOGIN, memberService.login(request));
    }
}
