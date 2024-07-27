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

    public static NotificationResponse.NotificationDTO notificationInfo(Notification notification){
        return NotificationResponse.NotificationDTO.builder()
                .body(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .isChecked(notification.isChecked())
                .url("Todo")
                .build();
    }
    public static NotificationResponse.NotificationInfoListDTO toNotificationInfoDTO(
            Page<Notification> notificationList){
        List<NotificationResponse.NotificationDTO> notificationDTOList
                = notificationList.stream().map(NotificationConverter::notificationInfo).collect(Collectors.toList());

        return NotificationResponse.NotificationInfoListDTO.builder()
                .isLast(notificationList.isLast())
                .isFirst(notificationList.isFirst())
                .totalPage(notificationList.getTotalPages())
                .totalElements(notificationList.getTotalElements())
                .notificationInfoList(notificationDTOList)
                .build();
    }

    public static NotificationResponse.UnreadNotificationDTO toUnreadNotificationDTO(List<Notification> notificationList){
        return NotificationResponse.UnreadNotificationDTO.builder()
                .isUnread(!notificationList.isEmpty())
                .build();
    }

    public static NotificationResponse.NotificationUpdateCountDTO toNotificationUpdateCountDTO(List<Notification> notificationList){
        return NotificationResponse.NotificationUpdateCountDTO.builder()
                .updateCount(notificationList.size())
                .build();
    }
    public static NotificationResponse.NotificationUpdateCountDTO toNotificationUpdateCountDTO(int updateCount){
        return NotificationResponse.NotificationUpdateCountDTO.builder()
                .updateCount(updateCount)
                .build();
    }

    public static DeviceToken toDeviceToken(Member member, NotificationRequest.FcmTokenDTO fcmTokenDTO){
        return DeviceToken.builder()
                .member(member)
                .fcmToken(fcmTokenDTO.getToken())
                .build();
    }

}
