package com.umc.naoman.domain.member.service.redis;

import com.umc.naoman.domain.member.entity.redis.RefreshToken;

public interface RefreshTokenService {
    RefreshToken findByRefreshToken(String refreshToken);
    void saveRefreshToken(Long memberId, String refreshToken);
}
