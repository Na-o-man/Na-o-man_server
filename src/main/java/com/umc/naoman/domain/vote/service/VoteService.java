package com.umc.naoman.domain.vote.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteId;

public interface VoteService {
    VoteId generateVote(Long agendaId, GenerateVoteRequest request, Member member);
}
