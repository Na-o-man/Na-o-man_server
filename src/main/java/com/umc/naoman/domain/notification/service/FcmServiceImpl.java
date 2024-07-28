package com.umc.naoman.domain.notification.service;

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
    public void saveFcmToken(Long memberId, String fcmToken) {
        //멤버 리파지토리 개발 후 수정해야됨
        DeviceToken deviceToken = DeviceToken.builder()
                                    .member(null)
                                    .fcmToken(fcmToken)
                                    .build();
        fcmTokenRepository.save(deviceToken);
    }
}
