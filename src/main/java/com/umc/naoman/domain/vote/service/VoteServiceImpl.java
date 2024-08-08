package com.umc.naoman.domain.vote.service;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.agenda.repository.AgendaPhotoRepository;
import com.umc.naoman.domain.agenda.service.AgendaService;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.domain.vote.converter.VoteConverter;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteListRequest;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteIdList;
import com.umc.naoman.domain.vote.entity.Vote;
import com.umc.naoman.domain.vote.repository.VoteRepository;
import com.umc.naoman.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.naoman.global.error.code.AgendaErrorCode.AGENDA_PHOTO_NOT_FOUND;
import static com.umc.naoman.global.error.code.VoteErrorCode.DUPLICATE_VOTE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    private final ShareGroupService shareGroupService;
    private final AgendaService agendaService;
    private final AgendaPhotoRepository agendaPhotoRepository;
    private final VoteRepository voteRepository;
    private final VoteConverter voteConverter;

    @Override
    @Transactional
    public VoteIdList generateVoteList(Long agendaId, GenerateVoteListRequest request, Member member) {
        Agenda agenda = agendaService.findAgenda(agendaId); // 안건의 존재 여부 확인
        Profile profile = shareGroupService.findProfile(agenda.getShareGroup().getId(), member.getId());

        List<Vote> voteList = request.getVoteInfoList().stream()
                .map(voteInfo -> generateVote(voteInfo, profile))
                .collect(Collectors.toList());

        voteRepository.saveAll(voteList);
        return voteConverter.toVoteIdList(voteList);
    }

    private Vote generateVote(GenerateVoteRequest request, Profile profile) {
        // AgendaPhoto findAgendaPhoto(Long agendaPhotoId)로 함수화 예정
        AgendaPhoto agendaPhoto = agendaPhotoRepository.findById(request.getAgendaPhotoId())
                .orElseThrow(() -> new BusinessException(AGENDA_PHOTO_NOT_FOUND));

        checkDuplicateVote(profile, agendaPhoto);
        return voteConverter.toEntity(request.getComment(), profile, agendaPhoto);
    }

    // 해당 사진에 대한 투표를 이미 했는지 확인한다
    private void checkDuplicateVote(Profile profile, AgendaPhoto agendaPhoto) {
        if (voteRepository.existsByProfileIdAndAgendaPhotoId(profile.getId(), agendaPhoto.getId())) {
            throw new BusinessException(DUPLICATE_VOTE);
        }
    }
}
