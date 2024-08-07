package com.umc.naoman.global.security.model;

import com.umc.naoman.domain.member.entity.SocialType;
import com.umc.naoman.global.security.attribute.OAuthAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {
    private OAuthAttribute oAuthAttribute;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                            String nameAttributeKey, OAuthAttribute oAuthAttribute) {
        super(authorities,attributes,nameAttributeKey);
        this.oAuthAttribute = oAuthAttribute;
    }

    public OAuthAttribute getOAuthAttribute() {
        return oAuthAttribute;
    }

    public SocialType getProvider() {
        return this.oAuthAttribute.getProvider();
    }

    public String getAuthId() {
        return this.oAuthAttribute.getAuthId();
    }
}
