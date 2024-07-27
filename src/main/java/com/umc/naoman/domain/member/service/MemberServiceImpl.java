package com.umc.naoman.domain.member.service;

import com.umc.naoman.domain.member.converter.MemberConverter;
import com.umc.naoman.domain.member.dto.MemberRequest.AndroidSignupRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.WebSignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.entity.SocialType;
import com.umc.naoman.domain.member.repository.MemberRepository;
import com.umc.naoman.domain.member.service.redis.RefreshTokenService;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.security.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.umc.naoman.global.error.code.MemberErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final JwtUtils jwtUtils;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long REFRESH_TOKEN_VALIDITY_IN_SECONDS;

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND_BY_MEMBER_ID));
    }

    @Override
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND_BY_EMAIL));
    }

    @Override
    public CheckMemberRegistration checkRegistration(String email) {
        boolean isRegistered = memberRepository.existsByEmail(email);
        return new CheckMemberRegistration(isRegistered);
    }

    @Override
    @Transactional
    public LoginInfo signup(AndroidSignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(MEMBER_ALREADY_SIGNUP);
        }

        Member member = memberConverter.toEntity(request);
        memberRepository.save(member);

        // 회원가입 완료 후 로그인 처리를 위해 access token, refresh token 발급
        // 별도 권한 정책이 없으므로 default 처리
        String role = "ROLE_DEFAULT";
        String accessToken = jwtUtils.createJwt(member.getEmail(), role, ACCESS_TOKEN_VALIDITY_IN_SECONDS);
        String refreshToken = jwtUtils.createJwt(member.getEmail(), role, REFRESH_TOKEN_VALIDITY_IN_SECONDS);
        refreshTokenService.saveRefreshToken(member.getId(), refreshToken);
        return memberConverter.toLoginInfo(member.getId(), accessToken, refreshToken);
    }

    @Override
    @Transactional
    public LoginInfo signup(String tempMemberInfo, WebSignupRequest request) {
        Claims payload = jwtUtils.getPayload(tempMemberInfo);
        AndroidSignupRequest androidSignupRequest = AndroidSignupRequest.builder()
                .email(payload.get("email", String.class))
                .name(payload.get("name", String.class))
                .image(payload.get("image", String.class))
                .socialType(SocialType.valueOf(payload.get("socialType", String.class)))
                .marketingAgreed(request.getMarketingAgreed())
                .build();

        return signup(androidSignupRequest);
    }
}
