package com.umc.naoman.domain.member.service;

import com.umc.naoman.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    Member findMember(Long memberId);
    Member findMember(String email);
    // MyPageInfo getMyPageInfo(Member member);
}
