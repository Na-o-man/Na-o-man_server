package com.umc.naoman.domain.notification.service;

import com.umc.naoman.domain.member.entity.Member;

public interface FcmService {
    void saveFcmToken(Member member, String fcmToken);
}
