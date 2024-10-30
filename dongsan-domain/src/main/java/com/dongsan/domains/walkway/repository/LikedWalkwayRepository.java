package com.dongsan.domains.walkway.repository;

import com.dongsan.domains.walkway.entity.LikedWalkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedWalkwayRepository extends JpaRepository<LikedWalkway, Long> {
    Boolean existsByWalkway_IdAndMember_Id(Long walkwayId, Long memberId);
}
