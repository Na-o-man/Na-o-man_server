package com.umc.naoman.domain.vote.service;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteListRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse.AgendaPhotoVoteDetails;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteIdList;

import java.util.List;

public interface VoteService {
    VoteIdList generateVoteList(Long agendaId, GenerateVoteListRequest request, Member member);
    List<AgendaPhotoVoteDetails> getVoteList(Long agendaId, Member member);
}
