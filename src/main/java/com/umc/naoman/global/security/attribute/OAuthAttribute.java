package com.umc.naoman.global.security.attribute;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.entity.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttribute {
    private final String email;
    private final String name;
    private final String image;
    private final SocialType provider;
    private final String authId;
    private final String usernameAttributeKey;
    private final Map<String, Object> attributes;

    public static OAuthAttribute of(String provider, Map<String, Object> attributes) {
        if (isEqualProvider(provider, SocialType.KAKAO)) {
            return ofKakao(attributes);
        } else if (isEqualProvider(provider, SocialType.GOOGLE)) {
            return ofGoogle(attributes);
        }

        return null;
    }

    private static OAuthAttribute ofKakao(Map<String, Object> attributes) {
        // 카카오의 경우 사용자 정보 데이터의 형태가 이중 Map이므로, 이를 풀어서 사용한다.
        String authId = String.valueOf(attributes.get(SocialType.KAKAO.getUsernameAttributeKey()));
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttribute.builder()
                .email(String.valueOf(kakaoAccount.get("email")))
                .name(String.valueOf(profile.get("nickname")))
                .image(String.valueOf(profile.get("profile_image_url")))
                .provider(SocialType.KAKAO)
                .authId(authId)
                .usernameAttributeKey(SocialType.KAKAO.getUsernameAttributeKey())
                .attributes(attributes)
                .build();
    }

    private static OAuthAttribute ofGoogle(Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .email(String.valueOf(attributes.get("email")))
                .name(String.valueOf(attributes.get("name")))
                .image(String.valueOf(attributes.get("picture")))
                .provider(SocialType.GOOGLE)
                .authId(String.valueOf(attributes.get(SocialType.GOOGLE.getUsernameAttributeKey())))
                .usernameAttributeKey(SocialType.GOOGLE.getUsernameAttributeKey())
                .attributes(attributes)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .image(image)
                .socialType(provider)
                .authId(authId)
                .build();
    }

    private static boolean isEqualProvider(String provider, SocialType socialType) {
        return provider.toUpperCase().equals(socialType.toString());
    }
}
