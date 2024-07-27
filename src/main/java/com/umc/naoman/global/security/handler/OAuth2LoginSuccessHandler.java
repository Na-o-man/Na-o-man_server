package com.umc.naoman.global.security.handler;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.service.MemberService;
import com.umc.naoman.domain.member.service.redis.RefreshTokenService;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.security.attribute.OAuthAttribute;
import com.umc.naoman.global.security.model.CustomOAuth2User;
import com.umc.naoman.global.security.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.umc.naoman.global.security.util.CookieUtils;
import com.umc.naoman.global.security.util.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository
            oAuth2AuthorizationRequestBasedOnCookieRepository;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long REFRESH_TOKEN_VALIDITY_IN_SECONDS;
    @Value("${jwt.temp-member-info-validity-in-seconds}")
    private Long TEMP_MEMBER_INFO_VALIDITY_IN_SECONDS;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String TEMP_MEMBER_INFO_KEY = "temp_member_info";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        try { // 이미 회원가입된 회원인 경우
            Member member = memberService.findMember(oAuth2User.getEmail());
            handleExistingMemberLogin(request, response, oAuth2User, member);
        } catch (BusinessException e) { // 회원가입되어 있지 않은 경우
            handleMemberSignup(request, response, oAuth2User.getOAuthAttribute());
        }
    }

    private void handleExistingMemberLogin(HttpServletRequest request, HttpServletResponse response,
                                           OAuth2User oAuth2User, Member member) throws IOException {
        String role;
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        if (authorities.isEmpty()) { // role 자체가 할당되지 않은 경우 default 처리
            role = "ROLE_DEFAULT";
        } else {
            role = oAuth2User.getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority();
        }

        // 로그인 성공 처리를 위해 access token, refresh token 발급
        String accessToken = jwtUtils.createJwt(member.getEmail(), role, ACCESS_TOKEN_VALIDITY_IN_SECONDS);
        CookieUtils.addCookie(response, AUTHORIZATION_HEADER, accessToken, ACCESS_TOKEN_VALIDITY_IN_SECONDS.intValue());

        String refreshToken = jwtUtils.createJwt(member.getEmail(), role, REFRESH_TOKEN_VALIDITY_IN_SECONDS);
        refreshTokenService.saveRefreshToken(member.getId(), refreshToken);
        CookieUtils.addCookie(response, REFRESH_TOKEN_KEY, refreshToken, REFRESH_TOKEN_VALIDITY_IN_SECONDS.intValue());

        clearAuthenticationAttributes(request, response);
        // 프론트엔드 홈 화면으로 리다이렉션
        response.sendRedirect("http://localhost:3000");
    }

    private void handleMemberSignup(HttpServletRequest request, HttpServletResponse response, OAuthAttribute oAuthAttribute)
            throws IOException {
        // Resource Server로부터 조회한 사용자 정보를 JWT로 직렬화 후, 쿠키로 담는다.
        String tempUserInfo = jwtUtils.createJwt(oAuthAttribute, TEMP_MEMBER_INFO_VALIDITY_IN_SECONDS);
        CookieUtils.addCookie(response, TEMP_MEMBER_INFO_KEY, tempUserInfo, TEMP_MEMBER_INFO_VALIDITY_IN_SECONDS.intValue());

        clearAuthenticationAttributes(request, response);
        // 약관 동의 화면으로 리다이렉션
        response.sendRedirect("http://localhost:3000/terms");
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        oAuth2AuthorizationRequestBasedOnCookieRepository.removeAuthorizationRequest(request, response);
    }
}
