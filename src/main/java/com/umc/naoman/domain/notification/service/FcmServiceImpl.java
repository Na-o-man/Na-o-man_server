package com.umc.naoman.domain.notification.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.notification.entity.DeviceToken;
import com.umc.naoman.domain.notification.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService{
    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public void saveFcmToken(Member member, String fcmToken) {
        DeviceToken deviceToken = DeviceToken.builder()
                                    .member(member)
                                    .fcmToken(fcmToken)
                                    .build();
        fcmTokenRepository.save(deviceToken);
    }
}
