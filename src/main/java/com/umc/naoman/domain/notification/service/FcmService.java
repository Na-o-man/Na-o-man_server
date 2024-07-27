package com.umc.naoman.domain.notification.service;

public interface FcmService {
    void saveFcmToken(Long memberId, String fcmToken);
}
