package com.umc.naoman.domain.notification.entity;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.notification.service.JosamoaSingleton;
import com.umc.naoman.domain.shareGroup.entity.ShareGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "delete_photo_notifications")
@SQLRestriction("deleted_at is NULL")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeletePhotoNotification  extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_group_id")
    private ShareGroup shareGroup;
    @Column(name = "photo_count", nullable = false)
    private int photoCount;

    /*
    *[유저명]이 [그룹명]의 사진 [삭제 장수]장을 삭제했습니다.
     */
    @Override
    public void postMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append(JosamoaSingleton.setJosa(getActor().getName(),"이가"));
        sb.append(" ");
        sb.append(shareGroup.getName());
        sb.append("의 사진 ");
        sb.append(photoCount);
        sb.append("장을 삭제했습니다.");
        message =  sb.toString();
    }
    @Override
    public Notification makeOtherNotification(Member member){
        return DeletePhotoNotification.builder()
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
