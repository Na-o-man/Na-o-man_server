package com.umc.naoman.domain.vote.service;

import com.umc.naoman.domain.agenda.entity.Agenda;
import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.agenda.repository.AgendaPhotoRepository;
import com.umc.naoman.domain.agenda.service.AgendaService;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.shareGroup.service.ShareGroupService;
import com.umc.naoman.domain.vote.converter.VoteConverter;
import com.umc.naoman.domain.vote.dto.VoteRequest.GenerateVoteRequest;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteId;
import com.umc.naoman.domain.vote.entity.Vote;
import com.umc.naoman.domain.vote.repository.VoteRepository;
import com.umc.naoman.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * 1. agendaId로 안건을 갖고 온다. (안건의 존재 여부 확인)
     * 2. 갖고 온 안건에서 shareGroup 객체를 꺼낸다.
     * 3. shareGroup.findProfile(Long shareGroupId, Long memberId)을 통해 투표를 요청한 사람의 프로필 id를 알아낸다. (프로필 존재 여부 확인)
     * 4. request.getAgendaPhotoId() 값을 통해 회원이 투표하려고 한 agendaPhoto를 갖고 온다. (agendaPhoto 존재 여부 확인)
     * 5. 이미 해당 안건의 해당 사진에 투표한 기록이 있는지 확인(중복 투표 방지)
     * @param agendaId
     * @param request
     * @param member
     * @return
     */
    @Override
    @Transactional
    public VoteId generateVote(Long agendaId, GenerateVoteRequest request, Member member) {
        Agenda agenda = agendaService.findAgenda(agendaId);
        Profile profile = shareGroupService.findProfile(agenda.getShareGroup().getId(), member.getId());
        // 추후 findAgendaPhoto(Long agendaPhotoId)로 함수화 요청 예정.
        AgendaPhoto agendaPhoto = agendaPhotoRepository.findById(request.getAgendaPhotoId())
                .orElseThrow(() -> new BusinessException(AGENDA_PHOTO_NOT_FOUND));

        checkDuplicateVote(profile, agendaPhoto);
        Vote vote = voteConverter.toEntity(request, profile, agendaPhoto);

        voteRepository.save(vote);
        return voteConverter.toVoteId(vote.getId());
    }

    private void checkDuplicateVote(Profile profile, AgendaPhoto agendaPhoto) {
        if (voteRepository.existsByProfileIdAndAgendaPhotoId(profile.getId(), agendaPhoto.getId())) {
            throw new BusinessException(DUPLICATE_VOTE);
        }
    }


}
