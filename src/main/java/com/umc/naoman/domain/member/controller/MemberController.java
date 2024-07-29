package com.umc.naoman.domain.member.controller;

import com.umc.naoman.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@Tag(name = "회원 API", description = "회원 도메인의 API입니다.")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
}
