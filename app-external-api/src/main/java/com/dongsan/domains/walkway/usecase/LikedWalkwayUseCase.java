package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.LikedWalkwayMapper;
import com.dongsan.domains.walkway.service.LikedWalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LikedWalkwayUseCase {
    private final LikedWalkwayCommandService likedWalkwayCommandService;
    private final MemberQueryService memberQueryService;
    private final WalkwayQueryService walkwayQueryService;

    public void createLikedWalkway(Long memberId, Long walkwayId) {
        Member member = memberQueryService.getMember(memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        boolean isLiked = likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway);
        if (!isLiked) {
            LikedWalkway likedWalkway = LikedWalkwayMapper.toLikedWalkway(member, walkway);
            likedWalkwayCommandService.createLikedWalkway(likedWalkway);
            walkway.addLikedWalkway(likedWalkway);
        }
    }

    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        Member member = memberQueryService.getMember(memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        boolean isLiked = likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway);
        if (isLiked) {
            likedWalkwayCommandService.deleteLikedWalkway(member, walkway);
            walkway.removeLikedWalkway();
        }
    }
}
