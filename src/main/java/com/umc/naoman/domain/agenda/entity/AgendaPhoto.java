package com.umc.naoman.domain.agenda.entity;

import com.umc.naoman.domain.photo.entity.Photo;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import com.umc.naoman.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

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
    @ManyToOne
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
}
