package com.umc.naoman.domain.vote.service;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.agenda.service.AgendaPhotoService;
import com.umc.naoman.domain.agenda.service.AgendaService;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.entity.Role;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.domain.vote.converter.VoteConverter;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteListRequest;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse.AgendaPhotoVoteDetails;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteIdList;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteInfo;
import com.umc.naoman.domain.vote.entity.Vote;
import com.umc.naoman.domain.vote.repository.VoteRepository;
import com.umc.naoman.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.naoman.global.error.code.VoteErrorCode.DUPLICATE_VOTE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    private final ShareGroupService shareGroupService;
    private final AgendaService agendaService;
    private final AgendaPhotoService agendaPhotoService;
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
        AgendaPhoto agendaPhoto = agendaPhotoService.findAgendaPhoto(request.getAgendaPhotoId());
        checkDuplicateVote(profile, agendaPhoto);

        return voteConverter.toEntity(request.getComment(), profile, agendaPhoto);
    }

    // 해당 사진에 대한 투표를 이미 했는지 확인한다
    private void checkDuplicateVote(Profile profile, AgendaPhoto agendaPhoto) {
        if (voteRepository.existsByProfileIdAndAgendaPhotoId(profile.getId(), agendaPhoto.getId())) {
            throw new BusinessException(DUPLICATE_VOTE);
        }
    }

    @Override
    public List<AgendaPhotoVoteDetails> getVoteList(Long agendaId, Member member) {
        Agenda agenda = agendaService.findAgenda(agendaId); // 안건 존재 여부 확인
        // 안건과 연관관계에 있는 공유그룹을 이용해 요청한 회원의 프로필을 가져온다
        // 해당 회원이 해당 공유 그룹의 그룹원인지도 확인
        Profile viewerProfile = shareGroupService.findProfile(agenda.getShareGroup().getId(), member.getId());

        List<AgendaPhoto> agendaPhotoList = agendaPhotoService.findAgendaPhotoList(agendaId);
        return agendaPhotoList.stream()
                .map(agendaPhoto -> getVoteList(agendaPhoto.getId(), viewerProfile))
                .toList();
    }

    // 특정 안건 중 특정 사진에 대한 투표 목록 조회
    private AgendaPhotoVoteDetails getVoteList(Long agendaPhotoId, Profile viewerProfile) {
        List<Vote> voteList = voteRepository.findByAgendaPhotoId(agendaPhotoId);

        List<VoteInfo> voteInfoList = voteList.stream()
                .map(vote -> voteConverter.toVoteInfo(vote, viewerProfile))
                .collect(Collectors.toList());

        return voteConverter.toAgendaPhotoVoteDetails(agendaPhotoId, voteInfoList);
    }
}
