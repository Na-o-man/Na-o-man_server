package com.umc.naoman.domain.agenda.entity;

import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "agendas")
@SQLRestriction("deleted_at is NULL")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Agenda extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agenda_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_group_id")
    private ShareGroup shareGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
