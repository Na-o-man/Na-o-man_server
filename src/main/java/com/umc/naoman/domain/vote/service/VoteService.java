package com.umc.naoman.domain.vote.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteListRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteIdList;

public interface VoteService {
    VoteIdList generateVoteList(Long agendaId, GenerateVoteListRequest request, Member member);
}
