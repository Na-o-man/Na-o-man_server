package com.umc.naoman.domain.notification.repository;

import com.umc.naoman.domain.notification.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<DeviceToken, Long> {
}
