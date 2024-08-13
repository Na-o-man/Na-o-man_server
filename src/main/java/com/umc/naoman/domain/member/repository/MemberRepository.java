package com.umc.naoman.domain.member.repository;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialTypeAndAuthId(SocialType socialType, String authId);
    Boolean existsBySocialTypeAndAuthId(SocialType socialType, String authId);
}
