package com.umc.naoman.domain.vote.converter;

import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.vote.dto.VoteResponse;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteId;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteIdList;
import com.umc.naoman.domain.vote.entity.Vote;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VoteConverter {
    public Vote toEntity(String comment, Profile profile, AgendaPhoto agendaPhoto) {
        return Vote.builder()
                .comment(comment)
                .profile(profile)
                .agendaPhoto(agendaPhoto)
                .build();
    }

    public VoteIdList toVoteIdList(List<Vote> voteList) {
        List<Long> voteIdList = voteList.stream()
                .map(vote -> vote.getId())
                .collect(Collectors.toList());

        return new VoteIdList(voteIdList);
    }

    public VoteId toVoteId(Long voteId) {
        return new VoteId(voteId);
    }
}
