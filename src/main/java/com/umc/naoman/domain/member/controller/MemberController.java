package com.umc.naoman.domain.member.controller;


import com.umc.naoman.domain.member.converter.MemberConverter;
import com.umc.naoman.domain.member.dto.MemberResponse;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.service.MemberService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.MemberResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}") // memberId를 사용해 특정 회원 정보 조회
    public ResultResponse<MemberResponse.MemberInfo> getMemberInfo(@PathVariable(name = "memberId") Long memberId) {
        Member member = memberService.findMember(memberId);
        return ResultResponse.of(MemberResultCode.MEMBER_INFO,
                MemberConverter.toMemberInfo(member));
    }
}
