package com.umc.naoman.domain.member.converter;

import com.umc.naoman.domain.member.dto.MemberResponse;
import com.umc.naoman.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public static MemberResponse.MemberInfo toMemberInfo(Member member) {
        return MemberResponse.MemberInfo.builder()
                .name(member.getName())
                .email(member.getEmail())
                .image(member.getImage())
                .build();
    }
}
