package com.umc.naoman.domain.shareGroup.repository;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByShareGroupId(Long shareGroupId);
    Optional<Profile> findByShareGroupIdAndMemberId(Long shareGroupId, Long memberId);
    List<Profile> findByMemberId(Long memberId);
    boolean existsByShareGroupIdAndMemberId(Long shareGroupId, Long memberId);
    List<Profile> findByMember(Member member);
}
