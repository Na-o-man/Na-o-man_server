package com.umc.naoman.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedNotificationInfo {
        List<NotificationInfo> notificationInfoList;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationInfo {
        String body;
        Boolean isChecked;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt;
        String url;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IsUnread {
        Boolean isUnread;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationAcknowledgeCount {
        Long acknowledgedCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationDeletedCount {
        Long deletedCount;
    }
}
