package com.umc.naoman.domain.notification.entity;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "photos_notifications")
@SQLRestriction("deleted_at is NULL")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PhotoNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_group_id")
    private ShareGroup shareGroup;
    @Column(name = "photo_count", nullable = false)
    private int photoCount;


    /*
     *[그룹명]에 [유저명]님이 n장의 사진을 업로드 했습니다.
     */
    @Override
    public void postMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append(shareGroup.getName());
        sb.append("에");
        sb.append(getActor().getName());
        sb.append("님이 ");
        sb.append(photoCount);
        sb.append("장의 사진을 업로드 했습니다.");
        message =  sb.toString();
    }

    @Override
    public Notification makeOtherNotification(Member member){
        return PhotoNotification.builder()
                .id(this.getId())
                .message(this.getMessage())
                .member(member)
                .isChecked(false)
                .actor(this.getActor())
                .shareGroup(getShareGroup())
                .photoCount(getPhotoCount())
                .build();
    }
}
