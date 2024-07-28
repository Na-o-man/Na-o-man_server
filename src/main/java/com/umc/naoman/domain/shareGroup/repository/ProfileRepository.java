package com.umc.naoman.domain.shareGroup.repository;

import com.umc.naoman.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByShareGroupId(Long shareGroupId);
}
