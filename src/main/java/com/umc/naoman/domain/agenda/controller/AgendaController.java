package com.umc.naoman.domain.agenda.controller;

import com.umc.naoman.domain.agenda.converter.AgendaConverter;
import com.umc.naoman.domain.agenda.dto.AgendaRequest;
import com.umc.naoman.domain.agenda.dto.AgendaResponse;
import com.umc.naoman.domain.agenda.dto.AgendaResponse.AgendaDetailInfo;
import com.umc.naoman.domain.agenda.dto.AgendaResponse.PagedAgendaDetailInfo;
import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.service.AgendaService;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.global.error.ErrorResponse;
import com.umc.naoman.global.result.ResultResponse;
import com.umc.naoman.global.result.code.AgendaResultCode;
import com.umc.naoman.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.umc.naoman.global.result.code.AgendaResultCode.GET_AGENDA_LIST;

@RestController
@RequestMapping("/agendas")
@Tag(name = "04. 안건 관련 API", description = "안건 관련 API입니다.")
@RequiredArgsConstructor
@Slf4j
public class AgendaController {

    private final AgendaService agendaService;
    private final AgendaConverter agendaConverter;

    @PostMapping
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

    @GetMapping()
    @Operation(summary = "특정 공유 그룹의 안건 목록 조회 API", description = "특정 공유 그룹의 안건 목록을 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "특정 안건 id를 입력해 주세요."),
            @Parameter(name = "page", description = "조회할 페이지 번호를 입력해주세요.(0번부터)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 안건 개수를 입력해 주세요.")
    })
    public ResultResponse<PagedAgendaDetailInfo> getAgendaList(@RequestParam("shareGroupId") Long shareGroupId,
                                                               @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                               @Parameter(hidden = true) Pageable pageable,
                                                               @LoginMember Member member) {
        PagedAgendaDetailInfo pagedAgendaDetailInfo = agendaService.getAgendaList(shareGroupId, member, pageable);
        return ResultResponse.of(GET_AGENDA_LIST, pagedAgendaDetailInfo);
    }


    @GetMapping("/{agendaId}")
    @Operation(summary = "안건 상세 조회 API", description = "agendaId로 안건 상세를 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "agendaId", description = "특정 안건 id를 입력해 주세요.")
    })
    public ResultResponse<AgendaDetailInfo> getAgendaDetail(@PathVariable(name = "agendaId") Long agendaId,
                                                            @LoginMember Member member) {
        Agenda agenda = agendaService.getAgendaDetailInfo(agendaId, member);

        return ResultResponse.of(AgendaResultCode.AGENDA_DETAIL,
                agendaConverter.toAgendaDetailInfo(agenda));
    }

    @DeleteMapping("/{agendaId}")
    @Operation(summary = "안건 삭제 API", description = "agendaId로 안건 삭제하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "agendaId", description = "특정 안건 id를 입력해 주세요.")
    })
    public ResultResponse<AgendaResponse.AgendaInfo> deleteAgenda(@PathVariable(name = "agendaId") Long agendaId,
                                                                           @LoginMember Member member) {
        Agenda agenda = agendaService.deleteAgenda(agendaId);

        return ResultResponse.of(AgendaResultCode.AGENDA_DETAIL, agendaConverter.toAgendaInfo(agenda));
    }
}

