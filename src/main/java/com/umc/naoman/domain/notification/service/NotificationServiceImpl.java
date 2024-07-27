package com.umc.naoman.domain.notification.service;

import com.umc.naoman.domain.notification.entity.Notification;
import com.umc.naoman.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getNotificationList(Long memberId, Integer page, Integer size) {
        return  notificationRepository.findAllByMemberId(memberId, PageRequest.of(page,size, Sort.by("createdAt").descending()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> isUnreadNotification(Long memberId) {
        return notificationRepository.findAllByMemberIdAndIsCheckedFalse(memberId);
    }

    @Override
    public List<Notification> setMyNotificationRead(Long memberId) {
        List<Notification> notificationList =  notificationRepository.findAllByMemberIdAndIsCheckedFalse(memberId);
        notificationList.forEach((notification -> notification.setIsChecked(true)));
        return notificationList;
    }

    @Override
    public int deleteNotification(Long memberId) {
        return notificationRepository.deleteByMemberId(memberId);
    }

    @Override
    public int deleteNotification(Long memberId, Long notificationId) {
        return  notificationRepository.deleteByMemberIdAndNotificationId(memberId,notificationId);
    }
}
