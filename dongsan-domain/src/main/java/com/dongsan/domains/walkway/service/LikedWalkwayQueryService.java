package com.dongsan.domains.walkway.service;

import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.repository.LikedWalkwayQueryDSLRepository;
import com.dongsan.domains.walkway.repository.LikedWalkwayRepository;
import com.dongsan.error.code.LikedWalkwayErrorCode;
import com.dongsan.error.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikedWalkwayQueryService {
    private final LikedWalkwayRepository likedWalkwayRepository;
    private final LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;

    public List<LikedWalkway> getUserLikedWalkway(Long memberId, Integer size, Long walkwayId) {
        // walkwayId가 null 이면 첫 페이지 라는 뜻
        Long likedWalkwayId = null;
        if(walkwayId != null){
            likedWalkwayId = findByMemberIdAndWalkwayId(memberId, walkwayId).getId();
        }
        return likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, likedWalkwayId);
    }

    public LikedWalkway findByMemberIdAndWalkwayId(Long memberId, Long walkwayId){
        return likedWalkwayRepository.findByMemberIdAndWalkwayId(memberId, walkwayId)
                .orElseThrow(
                        () -> new CustomException(LikedWalkwayErrorCode.LIKED_WALKWAY_NOT_FOUND));
    }

}
