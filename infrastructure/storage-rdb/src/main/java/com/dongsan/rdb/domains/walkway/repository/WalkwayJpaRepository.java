package com.dongsan.rdb.domains.walkway.repository;

import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayJpaRepository extends JpaRepository<WalkwayEntity, Long> {
    boolean existsByIdAndMemberId(Long walkwayId, Long memberId);
}
