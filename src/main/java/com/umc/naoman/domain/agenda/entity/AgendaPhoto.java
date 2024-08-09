package com.umc.naoman.domain.agenda.entity;

import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.vote.entity.Vote;
import com.umc.naoman.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendas_photos")
@SQLRestriction("deleted_at is NULL")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AgendaPhoto extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agenda_photo_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @OneToMany(mappedBy = "agendaPhoto")
    @Builder.Default
    private List<Vote> voteList = new ArrayList<>();

    public void delete() {
        //agendaPhoto 삭제
        for (Vote vote : voteList) {
            vote.delete();
        }
        super.delete();
    }
}