package com.umc.naoman.domain.member.service;

import com.umc.naoman.domain.member.converter.MemberConverter;
import com.umc.naoman.domain.member.dto.MemberRequest.LoginRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.MarketingAgreedRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.SignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberInfo;
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
    public Member findMember(String authId, SocialType socialType) {
        return memberRepository.findByAuthIdAndSocialType(authId, socialType)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND_BY_AUTH_ID_AND_SOCIAL_TYPE));
    }

    @Override
    public CheckMemberRegistration checkRegistration(LoginRequest request) {
        boolean isRegistered = memberRepository.existsBySocialTypeAndAuthId(request.getSocialType(), request.getAuthId());
        return new CheckMemberRegistration(isRegistered);
    }

    @Override
    @Transactional
    public LoginInfo signup(SignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(MEMBER_ALREADY_SIGNUP);
        }

        Member member = memberConverter.toEntity(request);
        memberRepository.save(member);

        // 회원가입 완료 후 로그인 처리를 위해 access token, refresh token 발급
        // 별도 권한 정책이 없으므로 default 처리
        String role = "ROLE_DEFAULT";
        String email = member.getEmail();
        Long memberId = member.getId();
        String accessToken = jwtUtils.createJwt(email, role, ACCESS_TOKEN_VALIDITY_IN_SECONDS);
        String refreshToken = jwtUtils.createJwt(email, role, REFRESH_TOKEN_VALIDITY_IN_SECONDS);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);
        return memberConverter.toLoginInfo(memberId, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public LoginInfo signup(String tempMemberInfo, MarketingAgreedRequest request) {
        Claims payload = jwtUtils.getPayload(tempMemberInfo);
        SignupRequest signupRequest = memberConverter.toSignupRequest(payload, request.getMarketingAgreed());

        return signup(signupRequest);
    }

    @Override
    public LoginInfo login(LoginRequest request) {
        Member member = findMember(request.getAuthId(), request.getSocialType());

        Long memberId = member.getId();
        String email = member.getEmail();
        String role = "ROLE_DEFAULT";
        String accessToken = jwtUtils.createJwt(email, role, ACCESS_TOKEN_VALIDITY_IN_SECONDS);
        String refreshToken = jwtUtils.createJwt(email, role, REFRESH_TOKEN_VALIDITY_IN_SECONDS);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);

        return memberConverter.toLoginInfo(memberId, accessToken, refreshToken);
    }

    @Override
    public MemberInfo getMyInfo(Member member) {
        return memberConverter.toMemberInfo(member);
    }
}
