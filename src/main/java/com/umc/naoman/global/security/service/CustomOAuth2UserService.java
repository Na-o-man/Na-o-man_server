package com.umc.naoman.global.security.service;

import com.umc.naoman.global.security.attribute.OAuthAttribute;
import com.umc.naoman.global.security.model.CustomOAuth2User;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * OAuth 방식에 따라, 액세스 토큰을 바탕으로 사용자 정보를 조회하는 함수 loadUser()가 선언된 클래스
 */
@Service
@NoArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Resource Server에서 사용자 정보를 조회한다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        final OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, originAttributes);

        if (oAuthAttribute == null) {
            return null;
        }
        // 현재 서비스 정책 상 Member 엔티티 관련 권한은 존재하지 않으므로 emptySet()으로 처리
        Set<GrantedAuthority> authorities = Collections.emptySet();

        return new CustomOAuth2User(authorities, oAuthAttribute.getAttributes(),
                oAuthAttribute.getUsernameAttributeKey(), oAuthAttribute);
    }
}
