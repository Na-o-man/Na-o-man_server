package com.umc.naoman.domain.shareGroup.entity;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.vote.entity.Vote;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@SQLRestriction("deleted_at is NULL")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 2000)
    private String image;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_group_id")
    private ShareGroup shareGroup;
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "profile")
    @Builder.Default
    private List<Vote> voteList = new ArrayList<>();

    public void delete() {
        for (Vote vote: voteList) {
            vote.delete();
        }
        this.deletedAt = LocalDateTime.now();
    }

    public void setInfo(Member member) {
        this.member = member;
        this.image = member.getImage();
        this.joinedAt = LocalDateTime.now();
    }
}
