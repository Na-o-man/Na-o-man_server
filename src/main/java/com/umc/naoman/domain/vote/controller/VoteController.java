package com.umc.naoman.domain.vote.controller;

import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteListRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteIdList;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteInfo;
import com.umc.naoman.domain.vote.service.VoteService;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.umc.naoman.global.result.code.VoteResultCode.GENERATE_VOTE;

@RestController
@Tag(name = "05. 투표 API", description = "안건에 대한 투표 행위를 관리하는 API입니다.")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping("/agendas/{agendaId}/vote")
    @Operation(summary = "안건에 투표하기 API", description = "특정 안건에 대하여 투표하는 API입니다. 한 번에 여러 사진에 투표가 가능합니다.")
    @Parameters(value = {
            @Parameter(name = "agendaId", description = "투표를 진행할 안건의 agendaId를 입력해 주세요.")
    })
    public ResultResponse<VoteIdList> generateVoteList(@PathVariable("agendaId") Long agendaId,
                                                       @Valid @RequestBody GenerateVoteListRequest request,
                                                       @LoginMember Member member) {
        return ResultResponse.of(GENERATE_VOTE, voteService.generateVoteList(agendaId, request, member));
    }

    @GetMapping("/agendas/{agendaId}/vote")
    @Operation(summary = "특정 안건의 투표 현황 조회 API", description = "특정 안건에 대한 투표 현항을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "agendaId", description = "투표 현황을 조회할 안건의 agendaId를 입력해 주세요.")
    })
    public ResultResponse<VoteInfo> getVoteList(@PathVariable("agendaId") Long agendaId,
                                                @LoginMember Member member) {
//        return ResultResponse.of(GENERATE_VOTE, voteService.getVoteLIst(agendaId, member));
        return null;
    }

}
