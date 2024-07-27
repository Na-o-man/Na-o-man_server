package com.umc.naoman.domain.notification.service;

import com.umc.naoman.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    Page<Notification> getNotificationList(Long memberId, Integer page, Integer size);
    List<Notification> isUnreadNotification(Long memberId);
    List<Notification>  setMyNotificationRead(Long memberId);
    int deleteNotification(Long memberId);
    int deleteNotification(Long memberId, Long notificationId);

}
