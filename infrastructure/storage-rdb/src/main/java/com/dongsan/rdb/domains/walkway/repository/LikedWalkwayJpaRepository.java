package com.dongsan.rdb.domains.walkway.repository;

import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.entity.LikedWalkwayEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedWalkwayJpaRepository extends JpaRepository<LikedWalkwayEntity, Long> {
    Optional<LikedWalkwayEntity> findByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    Boolean existsByMemberAndWalkway(MemberEntity memberEntity, WalkwayEntity walkwayEntity);

    void deleteByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    Boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId);
}
