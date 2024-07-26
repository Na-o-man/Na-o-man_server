package com.umc.naoman.domain.member.service.redis;

import com.umc.naoman.domain.member.entity.redis.RefreshToken;
import com.umc.naoman.domain.member.repository.redis.RefreshTokenRepository;
import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    @Override
    @Transactional
    public void saveRefreshToken(Long memberId, String refreshToken) {
        RefreshToken updatedRefreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(originalRefreshToken -> originalRefreshToken.update(refreshToken))
                .orElse(new RefreshToken(memberId, refreshToken));

        refreshTokenRepository.save(updatedRefreshToken);
    }
}
