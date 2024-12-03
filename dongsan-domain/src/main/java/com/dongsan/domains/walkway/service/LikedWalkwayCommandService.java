package com.dongsan.domains.walkway.service;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.repository.LikedWalkwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikedWalkwayCommandService {
    private final LikedWalkwayRepository likedWalkwayRepository;

    public LikedWalkway createLikedWalkway(LikedWalkway likedWalkway) {
        return likedWalkwayRepository.save(likedWalkway);
    }

    public Boolean existsLikedWalkwayByMemberAndWalkway(Member member, Walkway walkway) {
        return likedWalkwayRepository.existsByMemberAndWalkway(member, walkway);
    }
}
