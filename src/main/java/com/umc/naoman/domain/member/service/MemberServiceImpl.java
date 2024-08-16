package com.umc.naoman.domain.member.service;

import com.umc.naoman.domain.member.converter.MemberConverter;
import com.umc.naoman.domain.member.dto.MemberRequest.LoginRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.MarketingAgreedRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.SignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.HasSamplePhoto;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberId;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberInfo;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.entity.SocialType;
import com.umc.naoman.domain.member.repository.MemberRepository;
import com.umc.naoman.domain.member.service.redis.RefreshTokenService;
import com.umc.naoman.domain.photo.service.PhotoService;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.Role;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.security.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.umc.naoman.global.error.code.MemberErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final RefreshTokenService refreshTokenService;
    private final PhotoService photoService;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final ShareGroupService shareGroupService;

    private final JwtUtils jwtUtils;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long REFRESH_TOKEN_VALIDITY_IN_SECONDS;

    @Override
    @Transactional
    public LoginInfo signup(String tempMemberInfo, MarketingAgreedRequest request) {
        Claims payload = jwtUtils.getPayload(tempMemberInfo);
        SignupRequest signupRequest = memberConverter.toSignupRequest(payload, request.getMarketingAgreed());

        return signup(signupRequest);
    }

    @Override
    @Transactional
    public LoginInfo signup(SignupRequest request) {
        if (memberRepository.existsBySocialTypeAndAuthId(request.getSocialType(), request.getAuthId())) {
            throw new BusinessException(MEMBER_ALREADY_SIGNUP);
        }

        Member member = memberConverter.toEntity(request);
        memberRepository.save(member);

        Long memberId = member.getId();
        // 회원가입 완료 후 로그인 처리를 위해 access token, refresh token 발급
        // 별도 권한 정책이 없으므로 default 처리
        String role = "ROLE_DEFAULT";

        return createJwtAndGetLoginInfo(memberId, role);
    }


    @Override
    public LoginInfo login(LoginRequest request) {
        Member member = findMember(request.getSocialType(), request.getAuthId());
        Long memberId = member.getId();
        String role = "ROLE_DEFAULT";
        return createJwtAndGetLoginInfo(memberId, role);
    }

    private LoginInfo createJwtAndGetLoginInfo(Long memberId, String role) {
        String accessToken = jwtUtils.createJwt(memberId, role, ACCESS_TOKEN_VALIDITY_IN_SECONDS);
        String refreshToken = jwtUtils.createJwt(memberId, role, REFRESH_TOKEN_VALIDITY_IN_SECONDS);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);

        return memberConverter.toLoginInfo(memberId, accessToken, refreshToken);
    }

    @Override
    public CheckMemberRegistration checkRegistration(LoginRequest request) {
        boolean isRegistered = memberRepository.existsBySocialTypeAndAuthId(request.getSocialType(), request.getAuthId());
        return memberConverter.toCheckMemberRegistration(isRegistered);
    }

    @Override
    public HasSamplePhoto hasSamplePhoto(Member member) {
        boolean hasSamplePhoto = photoService.hasSamplePhoto(member);
        return memberConverter.toHasSamplePhoto(hasSamplePhoto);
    }

    @Override
    public MemberInfo getMyInfo(Member member) {
        return memberConverter.toMemberInfo(member);
    }

    @Override
    public MemberId getMyMemberId(Member member) {
        return memberConverter.toMemberId(member.getId());
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND_BY_MEMBER_ID));
    }

    @Override
    public Member findMember(SocialType socialType, String authId) {
        return memberRepository.findBySocialTypeAndAuthId(socialType, authId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND_BY_AUTH_ID_AND_SOCIAL_TYPE));
    }

    @Override
    @Transactional
    public Member deleteMember(Member member) {
        Long memberId = member.getId();
        List<Profile> profileList = shareGroupService.findProfileListByMemberId(memberId);
        for (Profile profile : profileList) {
            Long shareGroupId = profile.getShareGroup().getId();
            if (profile.getRole() == Role.CREATOR) {
                // 특정 공유 그룹의 사진을 모두 삭제
                photoService.deletePhotoListByShareGroupId(shareGroupId);
                // 공유 그룹 삭제
                profile.getShareGroup().delete();
            } else { // creator가 아닌 경우
                photoService.deletePhotoListByFaceTag(memberId);
                profile.delete();
            }
        }
        // 회원 샘플 사진 삭제
        photoService.deleteSamplePhotoList(member);
        member.delete();
        return member;
    }
}
