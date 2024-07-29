package com.umc.naoman.domain.notification.converter;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.notification.dto.NotificationRequest;
import com.umc.naoman.domain.notification.dto.NotificationResponse;
import com.umc.naoman.domain.notification.entity.DeviceToken;
import com.umc.naoman.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationConverter {

    public static NotificationResponse.NotificationInfo notificationInfo(Notification notification){
        return NotificationResponse.NotificationInfo.builder()
                .body(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .isChecked(notification.isChecked())
                .url(notification.makeNotificationInfoURL()) //다형성으로 각기 다른 알림이 적절한 URL 만들도록 오버라이딩.
                .build();
    }
    public static NotificationResponse.PagedNotificationInfo toNotificationInfo(
            Page<Notification> notificationList){
        List<NotificationResponse.NotificationInfo> notificationInfoList
                = notificationList.stream().map(NotificationConverter::notificationInfo).collect(Collectors.toList());

        return NotificationResponse.PagedNotificationInfo.builder()
                .isLast(notificationList.isLast())
                .isFirst(notificationList.isFirst())
                .totalPage(notificationList.getTotalPages())
                .totalElements(notificationList.getTotalElements())
                .notificationInfoList(notificationInfoList)
                .build();
    }

    public static NotificationResponse.UnreadNotification toUnreadNotification(List<Notification> notificationList){
        return NotificationResponse.UnreadNotification.builder()
                .isUnread(!notificationList.isEmpty())
                .build();
    }

    public static NotificationResponse.NotificationAcknowledgeCount toNotificationAcknowledgedCount(List<Notification> notificationList){
        return NotificationResponse.NotificationAcknowledgeCount.builder()
                .acknowledgedCount((long)notificationList.size())
                .build();
    }
    public static NotificationResponse.NotificationAcknowledgeCount toNotificationAcknowledgedCount(Long updateCount){
        return NotificationResponse.NotificationAcknowledgeCount.builder()
                .acknowledgedCount(updateCount)
                .build();
    }

    public static NotificationResponse.NotificationDeletedCount toNotificationDeletedCount(Long updateCount){
        return NotificationResponse.NotificationDeletedCount.builder()
                .deletedCount(updateCount)
                .build();
    }

    public static DeviceToken toDeviceToken(Member member, NotificationRequest.FcmToken fcmToken){
        return DeviceToken.builder()
                .member(member)
                .fcmToken(fcmToken.getToken())
                .build();
    }

}
