package com.umc.naoman.domain.member.controller;

import com.umc.naoman.domain.member.converter.MemberConverter;
import com.umc.naoman.domain.member.dto.MemberResponse;
import com.umc.naoman.domain.member.dto.MemberResponse.HasSamplePhoto;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberId;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberInfo;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.service.MemberService;
import com.umc.naoman.global.error.ErrorResponse;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.MemberResultCode;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import static com.umc.naoman.global.result.code.MemberResultCode.*;

@RestController
@RequestMapping("/members")
@Tag(name = "01. 회원 API", description = "회원 도메인의 API입니다.")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberConverter memberConverter;

    @GetMapping("/{memberId}") // memberId를 사용해 특정 회원 정보 조회
    @Operation(summary = "특정 회원 정보 조회 API", description = "[PathVariable]\n memberId\n[request]\n" +
            "[response]\n uesrname, email, 소셜 프로필 이미지 url")
    @ApiResponses({
            @ApiResponse(responseCode = "SM005", description = "특정 회원 정보 조회 성공."),
            @ApiResponse(responseCode = "EM001", description = "해당 memberId를 가진 회원이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResultResponse<MemberInfo> getMemberInfo(@PathVariable(name = "memberId") Long memberId) {
        Member member = memberService.findMember(memberId);
        return ResultResponse.of(MemberResultCode.MEMBER_INFO,
                memberConverter.toMemberInfo(member));
    }

    @GetMapping("/my")
    @Operation(summary = "내 회원정보 조회 API", description = "자신의 회원 정보를 조회하는 API입니다.")
    public ResultResponse<MemberInfo> getMyInfo(@LoginMember Member member) {
        return ResultResponse.of(CHECK_MEMBER_REGISTRATION, memberService.getMyInfo(member));
    }

    @GetMapping("/my-memberId")
    @Operation(summary = "내 memberId 조회 API", description = "자신의 memberId를 조회하는 API입니다.")
    public ResultResponse<MemberId> getMyMemberId(@LoginMember Member member) {
        return ResultResponse.of(GET_MY_MEMBERID, memberService.getMyMemberId(member));
    }

    @GetMapping("/terms/{memberId}")
    @Operation(summary = "마케팅 약관 동의 여부 조회 API", description = "[PathVariable]\n memberId\n[request]\n" +
            "[response]\n 마케팅 동의 여부 -> 동의 => true, 비동의 => false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SM006", description = "마케팅 약관 동의 여부 조회 성공."),
            @ApiResponse(responseCode = "EM001", description = "해당 memberId를 가진 회원이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResultResponse<MemberResponse.MarketingAgreed> getMarketingAgreed(@PathVariable(name = "memberId") Long memberId) {
        Member member = memberService.findMember(memberId);
        return ResultResponse.of(MemberResultCode.CHECK_MARKETING_AGREED, memberConverter.toMarketingAgreed(member));
    }

    @GetMapping("/samplePhoto")
    @Operation(summary = "샘플 사진 업로드 여부 조회", description = "자신이 샘플 사진을 업로드했는지 여부를 확인하는 API입니다.")
    public ResultResponse<HasSamplePhoto> hasSamplePhoto(@LoginMember Member member) {
        return ResultResponse.of(CHECK_HAS_SAMPLE_PHOTO, memberService.hasSamplePhoto(member));
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴 API", description = "[request]\n" +
            "[response]\n 탈퇴 memberId, 탈퇴 시간")
    @ApiResponses({
            @ApiResponse(responseCode = "SM007", description = "회원 탈퇴 성공."),
            @ApiResponse(responseCode = "EM001", description = "해당 memberId를 가진 회원이 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResultResponse<MemberResponse.DeleteMemberInfo> deleteMember(@LoginMember Member member){
        Member deleteMember = memberService.deleteMember(member);
        return ResultResponse.of(MemberResultCode.DELETE_MEMBER, memberConverter.toDeleteMemberInfo(deleteMember));
    }

}
