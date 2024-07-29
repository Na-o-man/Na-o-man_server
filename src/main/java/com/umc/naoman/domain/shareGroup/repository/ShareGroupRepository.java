package com.umc.naoman.domain.shareGroup.repository;

import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareGroupRepository extends JpaRepository<ShareGroup, Long> {
}
