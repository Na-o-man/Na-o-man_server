package com.umc.naoman.domain.notification.dto;

import lombok.Getter;

public class NotificationRequest {

    @Getter
    public static class FcmTokenDTO{
        String token;
    }

}
