package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.LikedWalkwayMapper;
import com.dongsan.domains.walkway.service.LikedWalkwayCommandService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import com.dongsan.error.code.MemberErrorCode;
import com.dongsan.error.code.WalkwayErrorCode;
import com.dongsan.error.exception.CustomException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LikedWalkwayUseCase {
    private final LikedWalkwayCommandService likedWalkwayCommandService;
    private final MemberQueryService memberQueryService;
    private final WalkwayQueryService walkwayQueryService;

    public void createLikedWalkway(Long memberId, Long walkwayId) {
        Member member = memberQueryService.readMember(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Walkway walkway = walkwayQueryService.getWalkway(walkwayId)
                .orElseThrow(() -> new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND));

        boolean isLiked = likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway);
        if (isLiked) {
            return;
        }

        LikedWalkway likedWalkway = LikedWalkwayMapper.toLikedWalkway(member, walkway);

        likedWalkwayCommandService.createLikedWalkway(likedWalkway);
    }

    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        Member member = memberQueryService.readMember(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Walkway walkway = walkwayQueryService.getWalkway(walkwayId)
                .orElseThrow(() -> new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND));

        boolean isLiked = likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway);
        if (isLiked) {
            likedWalkwayCommandService.deleteLikedWalkway(member, walkway);
        }
    }
}
