package com.umc.naoman.domain.shareGroup.entity;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "share_groups")
@SQLRestriction("deleted_at is NULL")
@Getter
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
    @Builder.Default
    private List<Profile> profileList = new ArrayList<>();
    @OneToMany(mappedBy = "shareGroup")
    @Builder.Default
    private List<Agenda> agendaList = new ArrayList<>();

    public void delete() {
        for (Profile profile : profileList) {
            profile.delete();
        }
        for (Agenda agenda : agendaList) {
            agenda.delete();
        }
        super.delete();
    }

    public void updateImage(String image) {
        this.image = image;
    }
}
