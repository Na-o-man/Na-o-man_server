package com.umc.naoman.domain.shareGroup.entity;

import com.umc.naoman.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "share_groups")
@SQLRestriction("deleted_at is NULL")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShareGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_group_id")
    private Long id;
    private String name;
    private String image;
    @Column(name = "member_count", nullable = false)
    private int memberCount;
    @Column(name = "invite_code", nullable = false)
    private String inviteCode;
    @OneToMany(mappedBy = "shareGroup")
    private List<Profile> profiles = new ArrayList<>();

    public void delete() {
        super.delete();
        for (Profile profile : profiles) {
            profile.delete();
        }
    }
}
