package com.dongsan.domains.walkway.repository;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.walkway.entity.LikedWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedWalkwayRepository extends JpaRepository<LikedWalkway, Long> {
    Optional<LikedWalkway> findByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    Boolean existsByMemberAndWalkway(Member member, Walkway walkway);
}
