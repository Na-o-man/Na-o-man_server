package com.umc.naoman.domain.notification.controller;

import com.umc.naoman.domain.notification.converter.NotificationConverter;
import com.umc.naoman.domain.notification.dto.NotificationRequest;
import com.umc.naoman.domain.notification.dto.NotificationResponse;
import com.umc.naoman.domain.notification.entity.Notification;
import com.umc.naoman.domain.notification.service.NotificationService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.NotificationResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/token")
    public ResultResponse<Void> registrationFcmToken(@RequestBody NotificationRequest.FcmTokenDTO fcmTokenDTO){

        return ResultResponse.of(NotificationResultCode.REGISTER_FCM_TOKEN,null);
    }

    @GetMapping("/my")
    public ResultResponse<NotificationResponse.NotificationInfoListDTO> getNotifications(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                         @RequestParam("size") Integer size){
        //로그인 처리 후 id 가져와야 됨
        Page<Notification> notificationPage =  notificationService.getNotificationList(null, page,size);
        return ResultResponse.of(NotificationResultCode.GET_MY_NOTIFICATION,
                NotificationConverter.toNotificationInfoDTO(notificationPage));
    }

    @GetMapping("/unread")
    public ResultResponse<NotificationResponse.UnreadNotificationDTO> getIsUnread(){
        //로그인 처리 후 id 가져와야 됨
        List<Notification> notificationList =  notificationService.isUnreadNotification(null);
        return ResultResponse.of(NotificationResultCode.CHECK_MY_UNREAD_NOTIFICATION,
                NotificationConverter.toUnreadNotificationDTO(notificationList));
    }

    @PostMapping("/acknowledgements")
    public ResultResponse<NotificationResponse.NotificationUpdateCountDTO> readNotification(){
        //로그인 처리 후 id 가져와야 됨
        List<Notification> notificationList = notificationService.setMyNotificationRead(null);
        return ResultResponse.of(NotificationResultCode.READ_ALL_MY_NOTIFICATION,
                NotificationConverter.toNotificationUpdateCountDTO(notificationList));
    }

    @DeleteMapping("/{notificationId}")
    public ResultResponse<NotificationResponse.NotificationUpdateCountDTO> deleteNotification(@PathVariable Long notificationId){
        //로그인 처리 후 id 가져와야 됨
        int deletedCount = notificationService.deleteNotification(null,notificationId);
        return ResultResponse.of(NotificationResultCode.DELETE_MY_NOTIFICATION,
                NotificationConverter.toNotificationUpdateCountDTO(deletedCount));
    }
    @DeleteMapping
    public ResultResponse<NotificationResponse.NotificationUpdateCountDTO> deleteAllNotification(@PathVariable Long notificationId){
        //로그인 처리 후 id 가져와야 됨
        int deletedCount = notificationService.deleteNotification(null);
        return ResultResponse.of(NotificationResultCode.DELETE_MY_NOTIFICATION,
                NotificationConverter.toNotificationUpdateCountDTO(deletedCount));
    }
}
