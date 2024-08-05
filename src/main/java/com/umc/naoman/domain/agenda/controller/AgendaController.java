package com.umc.naoman.domain.agenda.controller;

import com.umc.naoman.domain.agenda.converter.AgendaConverter;
import com.umc.naoman.domain.agenda.dto.AgendaRequest;
import com.umc.naoman.domain.agenda.dto.AgendaResponse;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.service.AgendaService;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.global.error.ErrorResponse;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.AgendaResultCode;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AgendaController {

    private final AgendaService agendaService;
    private final AgendaConverter agendaConverter;

    @PostMapping("/agendas")
    @Operation(summary = "안건 생성 API", description = "[request]\n shareGroupId, title, 안건에 올릴 PhotoId 리스트" +
            "\n[response]\n 생성된 안건의 agendaId, 생성시간 createdAt")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SA001",description = "안건 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EG005",description = "해당 shareGroupId를 가진 그룹이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ES3005",description = "해당 photoId를 가진 사진이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EG006",description = "그룹에 속한 회원의 프로필이 없습니다..",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResultResponse<AgendaResponse.AgendaInfo> createAgenda(@RequestBody @Valid AgendaRequest.CreateAgendaRequest request,
                                                                  @LoginMember Member member) {
        Agenda agenda = agendaService.createAgenda(member,request);
        return ResultResponse.of(AgendaResultCode.CREATE_AGENDA, agendaConverter.toAgendaInfo(agenda));
    }
}

