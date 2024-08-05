package com.umc.naoman.domain.notification.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.notification.converter.NotificationConverter;
import com.umc.naoman.domain.notification.dto.NotificationRequest;
import com.umc.naoman.domain.notification.dto.NotificationResponse;
import com.umc.naoman.domain.notification.entity.Notification;
import com.umc.naoman.domain.notification.service.FcmService;
import com.umc.naoman.domain.notification.service.NotificationService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.NotificationResultCode;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "알림 관련 API", description = " 알림 전체 목록 조희, fcm 토큰 업로드, 알림 1개 삭제, 알림 전체 삭제, 읽지 않은 알림 유무,  내 알람 전체 읽음 처리를 하는 API입니다.")
public class NotificationController {
    private final NotificationService notificationService;
    private final FcmService fcmService;
    private final NotificationConverter notificationConverter;

    @PostMapping("/token")
    @Operation(
            summary = "fcm 토큰 업로드",
            description = "안드로이드가 fcm 토큰을 서버에 업로드하는 API입니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = NotificationRequest.FcmToken.class)
                    )
            )
    )
    public ResultResponse<Void> registerFcmToken(@RequestBody NotificationRequest.FcmToken fcmToken,
                                                 @LoginMember Member member){
        fcmService.saveFcmToken(member, fcmToken.getToken());
        return ResultResponse.of(NotificationResultCode.REGISTER_FCM_TOKEN,null);
    }

    @GetMapping("/my")
    @Parameters(value = {
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 알림 개수를 입력해주세요.")
    })
    @Operation(summary = "자신의 모든 알림 조회", description = "자신의 모든 알림을 조회하는 API입니다.")
    public ResultResponse<NotificationResponse.PagedNotificationInfo> getNotifications(@LoginMember Member member,
                                                                                       @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                                                       @Parameter(hidden = true) Pageable pageable){
        Page<Notification> notificationPage =  notificationService.getNotificationList(member, pageable);
        return ResultResponse.of(NotificationResultCode.GET_MY_NOTIFICATION,
                notificationConverter.toNotificationInfo(notificationPage));
    }

    @GetMapping("/unread")
    @Operation(summary = "읽지 않은 알림 유무 조회",
            description = "읽지 않은 알림 유무 조회하는 API입니다.",
            parameters = {})
    public ResultResponse<NotificationResponse.IsUnread> getIsUnread(@LoginMember Member member){
        List<Notification> notificationList =  notificationService.isUnreadNotification(member);
        return ResultResponse.of(NotificationResultCode.CHECK_MY_UNREAD_NOTIFICATION,
                notificationConverter.toIsUnread(notificationList));
    }

    @PostMapping("/acknowledgements")
    @Operation(summary = "모든 알림 읽음 처리",
            description = "자신의 모든 알림을 읽음 처리하는 API입니다.",
            parameters = {})
    public ResultResponse<NotificationResponse.NotificationAcknowledgeCount> setMyNotificationRead(@LoginMember Member member){
        List<Notification> notificationList = notificationService.setMyNotificationRead(member);
        return ResultResponse.of(NotificationResultCode.READ_ALL_MY_NOTIFICATION,
                notificationConverter.toNotificationAcknowledgedCount(notificationList));
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "특정 알림 삭제",
            description = "특정 알림을 삭제하는 API입니다.",
            parameters = {
            @Parameter(name = "notificationId", description = "삭제할 알림의 Id", required = true, schema = @Schema(type = "long"))
    })
    public ResultResponse<NotificationResponse.NotificationAcknowledgeCount> deleteNotification(@PathVariable Long notificationId,
                                                                                                @LoginMember Member member){
        long deletedCount = notificationService.deleteNotification(member,notificationId);
        return ResultResponse.of(NotificationResultCode.DELETE_MY_NOTIFICATION,
                notificationConverter.toNotificationAcknowledgedCount(deletedCount));
    }
    @DeleteMapping
    @Operation(summary = "모든 알림 삭제",
            description = "자신의 모든 알림을 삭제하는 API입니다.",
            parameters = {})
    public ResultResponse<NotificationResponse.NotificationDeletedCount> deleteAllNotification(@LoginMember Member member){
        long deletedCount = notificationService.deleteNotificationAll(member);
        return ResultResponse.of(NotificationResultCode.DELETE_MY_NOTIFICATION,
                notificationConverter.toNotificationDeletedCount(deletedCount));
    }
}
