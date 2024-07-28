package com.umc.naoman.domain.member.converter;

import com.umc.naoman.domain.member.dto.MemberResponse;
import com.umc.naoman.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public static MemberResponse.MemberInfoByMemberIdDTO toMemberInfoByMemberIdDTO(Member member) {
        return MemberResponse.MemberInfoByMemberIdDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .image(member.getImage())
                .build();
    }
}
