package com.umc.naoman.domain.vote.converter;

import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteId;
import com.umc.naoman.domain.vote.entity.Vote;
import org.springframework.stereotype.Component;

@Component
public class VoteConverter {
    public Vote toEntity(GenerateVoteRequest request, Profile profile, AgendaPhoto agendaPhoto) {
        return Vote.builder()
                .comment(request.getComment())
                .profile(profile)
                .agendaPhoto(agendaPhoto)
                .build();
    }

    public VoteId toVoteId(Long voteId) {
        return new VoteId(voteId);
    }
}
