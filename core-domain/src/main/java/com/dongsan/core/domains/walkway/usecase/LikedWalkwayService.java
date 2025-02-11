package com.dongsan.core.domains.walkway.usecase;

import com.dongsan.core.domains.member.MemberReader;
import com.dongsan.core.domains.walkway.service.LikedWalkwayWriter;
import com.dongsan.core.domains.walkway.service.LikedWalkwayReader;
import com.dongsan.core.domains.walkway.service.WalkwayReader;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.core.domains.walkway.mapper.LikedWalkwayMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LikedWalkwayService {
    private final LikedWalkwayWriter likedWalkwayWriter;
    private final MemberReader memberReader;
    private final WalkwayReader walkwayQueryService;
    private final LikedWalkwayReader likedWalkwayQueryService;

    @Transactional
    public void createLikedWalkway(Long memberId, Long walkwayId) {
        Member member = memberReader.getMember(memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        boolean isLiked = likedWalkwayWriter.existsLikedWalkwayByMemberAndWalkway(member, walkway);
        if (!isLiked) {
            LikedWalkway likedWalkway = LikedWalkwayMapper.toLikedWalkway(member, walkway);
            likedWalkwayWriter.createLikedWalkway(likedWalkway);
            walkway.addLikedWalkway(likedWalkway);
        }
    }

    @Transactional
    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        Member member = memberReader.getMember(memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        boolean isLiked = likedWalkwayWriter.existsLikedWalkwayByMemberAndWalkway(member, walkway);
        if (isLiked) {
            likedWalkwayWriter.deleteLikedWalkway(member, walkway);
            walkway.removeLikedWalkway();
        }
    }

    public List<Boolean> existsLikedWalkways(Long memberId, List<Walkway> walkways) {
        return walkways.stream()
                .map(walkway -> likedWalkwayQueryService.existByMemberIdAndWalkwayId(memberId, walkway.getId()))
                .collect(Collectors.toList());
    }
}
