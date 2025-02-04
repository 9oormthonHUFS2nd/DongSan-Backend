package com.dongsan.core.domains.walkway.usecase;

import com.dongsan.core.domains.user.MemberQueryService;
import com.dongsan.core.domains.walkway.service.LikedWalkwayCommandService;
import com.dongsan.core.domains.walkway.service.LikedWalkwayQueryService;
import com.dongsan.core.domains.walkway.service.WalkwayQueryService;
import com.dongsan.core.common.annotation.UseCase;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.core.domains.walkway.mapper.LikedWalkwayMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class LikedWalkwayUseCase {
    private final LikedWalkwayCommandService likedWalkwayCommandService;
    private final MemberQueryService memberQueryService;
    private final WalkwayQueryService walkwayQueryService;
    private final LikedWalkwayQueryService likedWalkwayQueryService;

    @Transactional
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

    @Transactional
    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        Member member = memberQueryService.getMember(memberId);
        Walkway walkway = walkwayQueryService.getWalkway(walkwayId);

        boolean isLiked = likedWalkwayCommandService.existsLikedWalkwayByMemberAndWalkway(member, walkway);
        if (isLiked) {
            likedWalkwayCommandService.deleteLikedWalkway(member, walkway);
            walkway.removeLikedWalkway();
        }
    }

    public List<Boolean> existsLikedWalkways(Long memberId, List<Walkway> walkways) {
        return walkways.stream()
                .map(walkway -> likedWalkwayQueryService.existByMemberIdAndWalkwayId(memberId, walkway.getId()))
                .collect(Collectors.toList());
    }
}
