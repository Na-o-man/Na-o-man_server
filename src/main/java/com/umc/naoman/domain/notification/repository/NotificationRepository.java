package com.umc.naoman.domain.notification.repository;

import com.umc.naoman.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findAllByMemberId(Long memberId, Pageable pageable);
    List<Notification> findAllByMemberIdAndIsCheckedFalse(Long memberId);
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.member.id = :memberId AND n.id = :notificationId")
    long deleteByMemberIdAndNotificationId(@Param("memberId") Long memberId, @Param("notificationId") Long notificationId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.member.id = :memberId")
    long deleteByMemberId(@Param("memberId") Long memberId);

}
