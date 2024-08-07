package com.umc.naoman.domain.vote.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.vote.dto.VoteRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteId;
import com.umc.naoman.domain.vote.service.VoteService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.umc.naoman.global.result.code.VoteResultCode.GENERATE_VOTE;

@RestController
@Tag(name = "안건에 대한 투표 관련 API", description = "안건에 대한 투표 행위를 관리하는 API입니다.")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping("/agendas/{agendaId}/vote")
    public ResultResponse<VoteId> generateVote(@PathVariable("agendaId") Long agendaId,
                                               @Valid @RequestBody VoteRequest.GenerateVoteRequest request,
                                               @LoginMember Member member) {
        return ResultResponse.of(GENERATE_VOTE, voteService.generateVote(agendaId, request, member));
    }

}
