package com.umc.naoman.global.security.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.repository.MemberRepository;
import com.umc.naoman.global.security.model.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * JWT를 헤더로 세팅하여 요청이 들어왔을 때,
 * JwtAuthenticationFilter -> JwtUtils의 getAuthentication(String token)
 * -> loadUserByUsername(String username) 호출을 통해 DB에 저장된 회원인지 확인하는 클래스
 */
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public MemberDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 회원이 존재하지 않습니다."));

        return new MemberDetails(member);
    }
}
