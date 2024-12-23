package com.dongsan.domains.user.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.service.LikedWalkwayQueryService;
import com.dongsan.domains.walkway.service.WalkwayQueryService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UserWalkwayUseCase {
    private final WalkwayQueryService walkwayQueryService;
    private final LikedWalkwayQueryService likedWalkwayQueryService;

    public List<Walkway> getUserUploadWalkway(Long memberId, Integer size, Long walkwayId) {
        LocalDateTime lastCreated = null;
        if(walkwayId != null){
            // walkwayId 검증
            // 1. 존재하는 산책로인지
            Walkway walkway = walkwayQueryService.getWalkway(walkwayId);
            // 2. 내가 작성한 산책로인지
            walkwayQueryService.isOwnerOfWalkway(walkway.getId(), memberId);
            lastCreated = walkway.getCreatedAt();
        }
        List<Walkway> walkways = walkwayQueryService.getUserWalkWay(memberId, size, lastCreated);
        return walkways;
    }

    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, Long walkwayId) {
        LocalDateTime lastCreated = null;
        if(walkwayId != null){
            // walkwayId 검증
            // 1. 존재하는 산책로인지
            Walkway walkway = walkwayQueryService.getWalkway(walkwayId);
            // 2. 내가 작성한 산책로인지
            walkwayQueryService.isOwnerOfWalkway(walkway.getId(), memberId);
        }
        List<Walkway> walkways = likedWalkwayQueryService.getUserLikedWalkway(memberId, size, walkwayId)
                .stream()
                .map(LikedWalkway::getWalkway)
                .toList();
        return walkways;
    }
}
