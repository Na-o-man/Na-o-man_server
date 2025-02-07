package com.umc.naoman.domain.shareGroup.repository;

import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShareGroupRepository extends JpaRepository<ShareGroup, Long> {
    Optional<ShareGroup> findByInviteCode(String inviteCode);
    Page<ShareGroup> findByIdIn(List<Long> shareGroupIds, Pageable pageable);
}
