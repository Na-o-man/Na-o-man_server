package com.umc.naoman.domain.notification.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.notification.converter.NotificationConverter;
import com.umc.naoman.domain.notification.dto.NotificationRequest;
import com.umc.naoman.domain.notification.dto.NotificationResponse;
import com.umc.naoman.domain.notification.entity.Notification;
import com.umc.naoman.domain.notification.service.NotificationService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.NotificationResultCode;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/token")
    public ResultResponse<Void> registerFcmToken(@RequestBody NotificationRequest.FcmToken fcmToken,
                                                 @LoginMember Member member){

        return ResultResponse.of(NotificationResultCode.REGISTER_FCM_TOKEN,null);
    }

    @GetMapping("/my")
    @Parameters(value = {
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 알림 개수를 입력해주세요.")
    })
    public ResultResponse<NotificationResponse.PagedNotificationInfo> getNotifications(@LoginMember Member member,
                                                                                       @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                                                       @Parameter(hidden = true) Pageable pageable){
        Page<Notification> notificationPage =  notificationService.getNotificationList(member, pageable);
        return ResultResponse.of(NotificationResultCode.GET_MY_NOTIFICATION,
                NotificationConverter.toNotificationInfo(notificationPage));
    }

    @GetMapping("/unread")
    public ResultResponse<NotificationResponse.UnreadNotification> getIsUnread(@LoginMember Member member){
        List<Notification> notificationList =  notificationService.isUnreadNotification(member);
        return ResultResponse.of(NotificationResultCode.CHECK_MY_UNREAD_NOTIFICATION,
                NotificationConverter.toUnreadNotification(notificationList));
    }

    @PostMapping("/acknowledgements")
    public ResultResponse<NotificationResponse.NotificationAcknowledgeCount> setMyNotificationRead(@LoginMember Member member){
        List<Notification> notificationList = notificationService.setMyNotificationRead(member);
        return ResultResponse.of(NotificationResultCode.READ_ALL_MY_NOTIFICATION,
                NotificationConverter.toNotificationAcknowledgedCount(notificationList));
    }

    @DeleteMapping("/{notificationId}")
    public ResultResponse<NotificationResponse.NotificationAcknowledgeCount> deleteNotification(@PathVariable Long notificationId,
                                                                                                @LoginMember Member member){
        long deletedCount = notificationService.deleteNotification(member,notificationId);
        return ResultResponse.of(NotificationResultCode.DELETE_MY_NOTIFICATION,
                NotificationConverter.toNotificationAcknowledgedCount(deletedCount));
    }
    @DeleteMapping
    public ResultResponse<NotificationResponse.NotificationDeletedCount> deleteAllNotification(@LoginMember Member member){
        long deletedCount = notificationService.deleteNotificationAll(member);
        return ResultResponse.of(NotificationResultCode.DELETE_MY_NOTIFICATION,
                NotificationConverter.toNotificationDeletedCount(deletedCount));
    }
}
