package com.umc.naoman.domain.notification.entity;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.notification.service.JosamoaSingleton;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "agendas_notifications")
@SQLRestriction("deleted_at is NULL")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AgendaNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;


    /*
    *[그룹명]의 [유저명]이 [안경명] 투표를 열었습니다.
     */
    @Override
    public void postMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append(agenda.getShareGroup().getName());
        sb.append("의 ");
        sb.append(JosamoaSingleton.setJosa(getActor().getName(),"이가"));
        sb.append(" ");
        sb.append(agenda.getTitle());
        sb.append(" 투표를 열었습니다.");
        message =  sb.toString();
    }

    @Override
    public Notification makeOtherNotification(Member member){
        return AgendaNotification.builder()
                .id(this.getId())
                .message(this.getMessage())
                .member(member)
                .isChecked(false)
                .actor(this.getActor())
                .agenda(this.getAgenda())
                .build();
    }

    @Override
    public String makeNotificationInfoURL() {
        //해당 아젠다로 이동
        // todo 아젠다 컨틀롤러 만들어 지면 MvcUriComponentsBuilder 로 리팩토링 예정
        return "/agendas/" + agenda.getId();
    }
}
