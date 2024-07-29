package com.umc.naoman.domain.notification.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.notification.entity.Notification;
import com.umc.naoman.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        notificationList.forEach((notification -> notification.notificationAcknowledge(true)));
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

    @Override
    public void makeNewNotificationSameShareGroup(Notification notification) {
        //notification은 actor(해당 알람을 촉발시킨 맴버)가 생성하였으므로, actor는 읽음 처리 되어 있다. 또한 actor에게는 알림이 가면 안된다
        // 해당 공유그룹에 속한 맴버는 같은 알람을 받고 읽지 않음 처리. 또한 push도 보내야 한다

        notification.postMessage(); //오버라이딩에 의해 적절한 알람 메시지 만들어짐(템플릿 메소드 패턴)
        notificationRepository.save(notification);//엑터 알람 저장

        List<Member> memberList = new ArrayList<>(); //같은 공유 그룹에 속한 맴버 검색
        //같은 공유 그룹에 있으면 알림을 생성.
        memberList.stream()
                .filter(member -> !member.getId().equals(notification.getActor().getId()))
                .map(notification::makeOtherNotification)
                .forEach(notificationRepository::save);

        //todo fcm으로 push 처리
    }
}
