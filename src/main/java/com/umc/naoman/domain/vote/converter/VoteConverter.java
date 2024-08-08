package com.umc.naoman.domain.vote.converter;

import com.umc.naoman.domain.agenda.entity.AgendaPhoto;
import com.umc.naoman.domain.shareGroup.converter.ShareGroupConverter;
import com.umc.naoman.domain.shareGroup.entity.Profile;
import com.umc.naoman.domain.vote.dto.VoteResponse;
import com.umc.naoman.domain.vote.dto.VoteResponse.AgendaPhotoVoteDetails;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteId;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteIdList;
import com.umc.naoman.domain.vote.dto.VoteResponse.VoteInfo;
import com.umc.naoman.domain.vote.entity.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VoteConverter {
    private final ShareGroupConverter shareGroupConverter;

    public Vote toEntity(String comment, Profile profile, AgendaPhoto agendaPhoto) {
        return Vote.builder()
                .comment(comment)
                .profile(profile)
                .agendaPhoto(agendaPhoto)
                .build();
    }

    public VoteId toVoteId(Long voteId) {
        return new VoteId(voteId);
    }

    public VoteIdList toVoteIdList(List<Vote> voteList) {
        List<Long> voteIdList = voteList.stream()
                .map(vote -> vote.getId())
                .collect(Collectors.toList());

        return new VoteIdList(voteIdList);
    }

    public VoteInfo toVoteInfo(Vote vote, Profile viewerProfile) {
        Long voterProfileId = vote.getProfile().getId();
        Long viewerProfileId = viewerProfile.getId();
        boolean isMine = voterProfileId.equals(viewerProfileId);

        return VoteInfo.builder()
                .voteId(vote.getId())
                .comment(vote.getComment())
                .profileInfo(shareGroupConverter.toProfileInfo(vote.getProfile()))
                .agendaPhotoId(vote.getAgendaPhoto().getId())
                .isMine(isMine)
                .votedAt(vote.getVotedAt())
                .build();
    }

    public AgendaPhotoVoteDetails toAgendaPhotoVoteDetails(Long agendaPhotoId, List<VoteInfo> voteInfoList) {
        return AgendaPhotoVoteDetails.builder()
                .agendaPhotoId(agendaPhotoId)
                .voteInfoList(voteInfoList)
                .voteCount(voteInfoList.size())
                .build();
    }
}
