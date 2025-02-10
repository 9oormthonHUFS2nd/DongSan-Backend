package com.dongsan.core.domains.walkway.usecase;

import com.dongsan.core.common.annotation.UseCase;
import com.dongsan.core.common.error.CoreErrorCode;
import com.dongsan.core.common.error.CoreException;
import com.dongsan.core.domains.member.MemberReader;
import com.dongsan.core.domains.walkway.domain.LikedWalkway;
import com.dongsan.core.domains.walkway.domain.Walkway;
import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import com.dongsan.core.domains.walkway.service.WalkwayReader;
import com.dongsan.core.domains.walkway.service.WalkwayWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class WalkwayService {

    private final WalkwayWriter walkwayWriter;
    private final WalkwayReader walkwayReader;
    private final MemberReader memberReader;

    @Transactional
    public Walkway createWalkway(Walkway walkway) {
        return walkwayWriter.saveWalkway(walkway);
    }

    @Transactional(readOnly = true)
    public Walkway getWalkway(Long walkwayId) {
        return walkwayReader.getWalkway(walkwayId);
    }

    @Transactional
    public void updateWalkway(Walkway updateWalkway) {
        // 산책로 불러오기
        Walkway walkway = walkwayReader.getWalkway(updateWalkway.walkwayId());

        // 산책로 등록자 검증
        walkway.validateOwner(updateWalkway.author().authorId());

        walkwayWriter.updateWalkway(walkway);
    }

    @Transactional(readOnly = true)
    public List<Walkway> searchWalkway(Long userId, String sortType, Double latitude, Double longitude, Double distance, Long lastId, int size) {
        if (lastId != null && walkwayReader.existsWalkway(lastId)) {
            throw new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND);
        }

        WalkwaySort sort = WalkwaySort.typeOf(sortType);

        return walkwayReader.searchWalkway(userId, lastId, longitude, latitude, distance, size, sort);
    }

    @Transactional(readOnly = true)
    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return walkwayReader.existsLikedWalkway(memberId, walkwayId);
    }

    @Transactional(readOnly = true)
    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return walkwayReader.existsLikedWalkways(memberId, walkwayIds);
    }

    @Transactional
    public void createLikedWalkway(LikedWalkway likedWalkway) {
        boolean isLiked = walkwayReader.existsLikedWalkway(likedWalkway.memberId(), likedWalkway.walkwayId());

        if (!isLiked) {
            walkwayWriter.saveLikedWalkway(likedWalkway);
        }
    }

    @Transactional
    public void deleteLikedWalkway(LikedWalkway likedWalkway) {
        boolean isLiked = walkwayReader.existsLikedWalkway(likedWalkway.memberId(), likedWalkway.walkwayId());

        if (isLiked) {
            walkwayWriter.deleteLikedWalkway(likedWalkway);
        }
    }

    // TODO: hasNext 관련 로직 controller로 이동 or 반환을 DTO로 변경
    @Transactional(readOnly = true)
    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, Long walkwayId) {
        LocalDateTime lastCreatedAt = null;
        if(walkwayId != null){
            // lastId 검증
            // 1. 존재하는 산책로인지
            Walkway walkway = walkwayReader.getWalkway(walkwayId);
            // 2. 내가 작성한 산책로인지
            walkway.validateOwner(memberId);
            lastCreatedAt = walkway.createdAt();
        }
        List<Walkway> walkways = walkwayReader.getUserLikedWalkway(memberId, size+1, lastCreatedAt);
        boolean hasNext = walkways.size() > size;
        if(hasNext){
            walkways.remove(walkways.size()-1);
        }
        return walkways;
    }

    @Transactional(readOnly = true)
    public List<Walkway> getUserWalkway(Long memberId, Integer size, Long walkwayId) {
        LocalDateTime lastCreatedAt = null;
        if(walkwayId != null){
            // lastId 검증
            // 1. 존재하는 산책로인지
            Walkway walkway = walkwayReader.getWalkway(walkwayId);
            // 2. 내가 작성한 산책로인지
            walkway.validateOwner(memberId);
            lastCreatedAt = walkway.createdAt();
        }
        List<Walkway> walkways = walkwayReader.getUserWalkWay(memberId, size+1, lastCreatedAt);
        boolean hasNext = walkways.size() > size;
        if(hasNext){
            walkways.remove(walkways.size()-1);
        }
        return walkways;
    }
}
